# Deploying to Render (free) + Neon Postgres (free)

The app runs as a Docker container on Render's free web-service tier and stores all
data (accounts, profiles, generation history) in a free Neon Postgres database.
Local development is unaffected: without the `postgres` profile the app keeps using
the file H2 database in `./data/`.

## 1. Create the Neon database

1. Sign up at [neon.tech](https://neon.tech) (free plan, no credit card).
2. Create a project (any name, e.g. `namegenerator`). Pick a region close to your
   Render region.
3. From the project dashboard, copy the **connection string**. It looks like:

   ```
   postgresql://USER:PASSWORD@ep-xxx-pooler.REGION.aws.neon.tech/neondb?sslmode=require
   ```

4. Convert it to JDBC form by prefixing `jdbc:` and moving the credentials into
   query parameters:

   ```
   jdbc:postgresql://ep-xxx-pooler.REGION.aws.neon.tech/neondb?sslmode=require&user=USER&password=PASSWORD
   ```

   This whole string becomes the `SPRING_DATASOURCE_URL` secret in step 3.

## 2. Push the repository to GitHub

The repo currently has no remote. Before pushing, make sure no secrets are tracked:

```bash
git rm --cached src/main/resources/application-local.properties   # if still tracked
git status   # data/, application-local.properties must NOT appear
```

Then create a GitHub repository and push:

```bash
git remote add origin git@github.com:YOUR_USER/NameGenerator.git
git add . && git commit -m "Prepare Render + Neon deployment"
git push -u origin main
```

## 3. Create the Render service

1. Sign up at [render.com](https://render.com) and connect your GitHub account.
2. Choose **New → Blueprint** and select the repository. Render reads
   [render.yaml](../render.yaml) and proposes the `namegenerator` web service
   (Docker runtime, free plan).
3. When prompted for environment variables, set:
   - `GOOGLE_API_KEY` — your Gemini API key.
   - `SPRING_DATASOURCE_URL` — the JDBC URL from step 1.

   `REMEMBER_ME_KEY` is generated automatically by the blueprint; it signs the
   remember-me cookie so logins survive instance restarts.

   (`SPRING_PROFILES_ACTIVE=postgres` is already set by the blueprint.)
4. Deploy. The first build takes several minutes (Maven downloads inside Docker).
   On boot, Hibernate creates the schema in Neon and `data-postgresql.sql` seeds
   the dictionaries idempotently.
5. Open `https://namegenerator.onrender.com` (or your service URL) — you should
   land on the sign-in page. Register an account and run a generation.

## Notes and limits

- **Cold starts**: the free service spins down after ~15 minutes idle; the next
  request takes 30–60 s. Data is safe in Neon either way.
- **Neon autosuspend**: the free database also suspends when idle and resumes on
  the first query (adds a second or two to a cold start).
- **Redeploys**: every push to the connected branch triggers a rebuild. The seed
  script is idempotent, so restarts never duplicate dictionary rows.
- **Memory**: the JVM is capped at 75% of the container's RAM via
  `JAVA_TOOL_OPTIONS` in the [Dockerfile](../Dockerfile), which fits the 512 MB
  free instance.
