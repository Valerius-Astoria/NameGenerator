# NameGenerator — Agent Notes

Side project for writers and game developers: design a character’s core identity, then generate a customized name with Gemini. Also a compact Spring practice app — prefer a small surface area over new layers or frameworks.

## Stack (for development)

- Java 26, Spring Boot 4.1, Spring MVC, Spring Data JPA, Thymeleaf, H2, Validation, Lombok
- Gemini via `com.google.genai` (`GeminiNameGenerator`)
- Build/run: `./mvnw spring-boot:run` (tests: `./mvnw test`)

## Domain & flow

All pages except `/login` and `/register` require a signed-in user (email + BCrypt password via Spring Security form login).

1. **`/register`** / **`/login`** — email/password accounts (`User`, table `app_user`)
2. **`/`** → redirect to **`/design`** — capture identity (gender, age, country, ancestries, faiths, notes/background) into session `characterProfile`
3. **`POST /design`** → **`GET /generate/current`** — call Gemini; show native-script name, English form, and annotation
4. **`POST /generate`** — persist profile (owned by the current user) + result, clear session, return to design
5. **`/history`** — the user's past generations as cards; **`/history/{id}`** — full record (ownership-scoped, else 404)

Identity dimensions and seed dictionaries live in `docs/` (`dimensions.md`, `countries.md`, `races.md`, `religions.md`). Seed SQL: `data-h2.sql` (local) / `data-postgresql.sql` (production).

## Package layout

| Package / path | Role |
| --- | --- |
| `controller` | Thin MVC controllers + `@SessionAttributes("characterProfile")` |
| `model` | JPA entities / enums (`CharacterProfile`, `Country`, `Ancestry`, `Faith`, `Gender`) |
| `repository` | Spring Data JPA repositories |
| `web` | Form binders (`*ByIdConverter`), `OptionGroup`, `RegistrationForm`, `HtmlErrorAdvice` |
| `security` | Spring Security config, `AppUserDetailsService`, `CurrentUserService` |
| `gemini` | Prompting + Gemini client wrapper (skill prompt: `resources/prompts/name-generation-skill.md`) |
| `templates/` | Thymeleaf views (`design.html`, `generate.html`) |
| `static/css/` | Shared site styles |

## Conventions

- Keep the app **compact**: reuse controllers/repositories; avoid extra service layers unless logic is clearly shared or non-trivial.
- Write **clear class/method Javadoc** and brief comments where structure or feature intent is not obvious.
- Prefer constructor injection; keep controllers thin (bind form → validate → call Gemini/repo → view or redirect).
- Dictionary entities use `code` / hierarchical `parent` where needed; form posts bind by id via converters.
- Ancestry and faith selects are grouped with `OptionGroup` (parent → children).
- Match existing naming: `*Controller`, `*Repository`, `*ByIdConverter`, Thymeleaf view names without path prefixes.

## Config & secrets

- Default config: `src/main/resources/application.properties` (file H2 at `./data/`, `ddl-auto=update`, idempotent seed script `data-h2.sql` always). The `data/` directory is git-ignored; delete it for a clean database.
- Production: `postgres` profile + `SPRING_DATASOURCE_URL` env (Neon Postgres, seeds via `data-postgresql.sql`). Deployment to Render is described in `docs/deploy.md` (`Dockerfile`, `render.yaml`).
- Gemini: set `GOOGLE_API_KEY` in the environment, or override `gemini.api-key` / `gemini.model` locally.
- **Never commit API keys.** Use env vars or an ignored local profile (e.g. `application-local.properties`). Do not put secrets in `application.properties` or docs.

## When changing features

- New identity fields: update `CharacterProfile`, design form, validation, Gemini prompt, and `docs/dimensions.md` together.
- New dictionary data: update the relevant `docs/*.md` and `data-h2.sql` / `data-postgresql.sql` (and entities only if the shape changes).
- Keep UI and prompts oriented to novelists / game writers — practical names and short annotations, not generic chatbot fluff.
- Generation methodology changes (research steps, output keys, tone) go in `resources/prompts/name-generation-skill.md`, which is sent as the system instruction on every Gemini call.
