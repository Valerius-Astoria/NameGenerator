-- Curated seed data for the design form (common options only) — PostgreSQL variant.
-- Full catalogs remain in docs/countries.md, docs/races.md, docs/religions.md.
-- Uses INSERT ... ON CONFLICT (code) so the script is idempotent across restarts.

-- ========== Countries ==========
INSERT INTO country (code, name) VALUES
    ('AR', 'Argentina'),
    ('AU', 'Australia'),
    ('BR', 'Brazil'),
    ('CA', 'Canada'),
    ('CN', 'China'),
    ('EG', 'Egypt'),
    ('FR', 'France'),
    ('DE', 'Germany'),
    ('IN', 'India'),
    ('ID', 'Indonesia'),
    ('IE', 'Ireland'),
    ('IT', 'Italy'),
    ('JP', 'Japan'),
    ('MX', 'Mexico'),
    ('NG', 'Nigeria'),
    ('PH', 'Philippines'),
    ('PL', 'Poland'),
    ('RU', 'Russia'),
    ('ZA', 'South Africa'),
    ('KR', 'South Korea'),
    ('SU', 'Soviet Union'),
    ('ES', 'Spain'),
    ('SE', 'Sweden'),
    ('TH', 'Thailand'),
    ('TR', 'Türkiye'),
    ('GB', 'United Kingdom'),
    ('US', 'United States of America'),
    ('VN', 'Vietnam')
ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name;

-- ========== Ancestry (primary) ==========
INSERT INTO ancestry (code, name, parent_id) VALUES
    ('EAST_ASIAN', 'East Asian', NULL),
    ('SOUTHEAST_ASIAN', 'Southeast Asian', NULL),
    ('SOUTH_ASIAN', 'South Asian', NULL),
    ('MIDDLE_EASTERN', 'Middle Eastern', NULL),
    ('EUROPEAN', 'European', NULL),
    ('LATINO_HISPANIC', 'Latino / Hispanic', NULL),
    ('BLACK_AFRICAN_DIASPORA', 'Black / African Diaspora', NULL),
    ('JEWISH', 'Jewish', NULL),
    ('MIXED_MULTIRACIAL', 'Mixed / Multiracial', NULL),
    ('UNSPECIFIED', 'Unspecified', NULL)
ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name;

-- ========== Ancestry (secondary) ==========
INSERT INTO ancestry (code, name, parent_id) SELECT 'HAN_CHINESE', 'Han Chinese', id FROM ancestry WHERE code = 'EAST_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'JAPANESE', 'Japanese', id FROM ancestry WHERE code = 'EAST_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'KOREAN', 'Korean', id FROM ancestry WHERE code = 'EAST_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO ancestry (code, name, parent_id) SELECT 'VIETNAMESE', 'Vietnamese', id FROM ancestry WHERE code = 'SOUTHEAST_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'THAI', 'Thai', id FROM ancestry WHERE code = 'SOUTHEAST_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'FILIPINO', 'Filipino', id FROM ancestry WHERE code = 'SOUTHEAST_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'INDONESIAN', 'Indonesian', id FROM ancestry WHERE code = 'SOUTHEAST_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO ancestry (code, name, parent_id) SELECT 'HINDUSTANI', 'Hindustani', id FROM ancestry WHERE code = 'SOUTH_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'TAMIL', 'Tamil', id FROM ancestry WHERE code = 'SOUTH_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'BENGALI', 'Bengali', id FROM ancestry WHERE code = 'SOUTH_ASIAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO ancestry (code, name, parent_id) SELECT 'ARAB', 'Arab', id FROM ancestry WHERE code = 'MIDDLE_EASTERN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'PERSIAN', 'Persian', id FROM ancestry WHERE code = 'MIDDLE_EASTERN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'TURKISH', 'Turkish', id FROM ancestry WHERE code = 'MIDDLE_EASTERN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO ancestry (code, name, parent_id) SELECT 'ANGLO', 'Anglo', id FROM ancestry WHERE code = 'EUROPEAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'GERMANIC', 'Germanic', id FROM ancestry WHERE code = 'EUROPEAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'ROMANCE', 'Romance', id FROM ancestry WHERE code = 'EUROPEAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'SLAVIC', 'Slavic', id FROM ancestry WHERE code = 'EUROPEAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'NORDIC', 'Nordic', id FROM ancestry WHERE code = 'EUROPEAN' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO ancestry (code, name, parent_id) SELECT 'MEXICAN', 'Mexican', id FROM ancestry WHERE code = 'LATINO_HISPANIC' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'CARIBBEAN_LATINO', 'Caribbean Latino', id FROM ancestry WHERE code = 'LATINO_HISPANIC' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'SOUTH_AMERICAN', 'South American', id FROM ancestry WHERE code = 'LATINO_HISPANIC' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'BRAZILIAN', 'Brazilian', id FROM ancestry WHERE code = 'LATINO_HISPANIC' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO ancestry (code, name, parent_id) SELECT 'AFRICAN_AMERICAN', 'African American', id FROM ancestry WHERE code = 'BLACK_AFRICAN_DIASPORA' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'AFRO_CARIBBEAN', 'Afro-Caribbean', id FROM ancestry WHERE code = 'BLACK_AFRICAN_DIASPORA' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'WEST_AFRICAN', 'West African', id FROM ancestry WHERE code = 'BLACK_AFRICAN_DIASPORA' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO ancestry (code, name, parent_id) SELECT 'ASHKENAZI', 'Ashkenazi', id FROM ancestry WHERE code = 'JEWISH' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO ancestry (code, name, parent_id) SELECT 'SEPHARDI', 'Sephardi', id FROM ancestry WHERE code = 'JEWISH' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

-- ========== Faith (primary) ==========
INSERT INTO faith (code, name, parent_id) VALUES
    ('CHRISTIANITY', 'Christianity', NULL),
    ('ISLAM', 'Islam', NULL),
    ('JUDAISM', 'Judaism', NULL),
    ('HINDUISM', 'Hinduism', NULL),
    ('BUDDHISM', 'Buddhism', NULL),
    ('SIKHISM', 'Sikhism', NULL),
    ('NON_RELIGIOUS', 'Non-religious / secular', NULL),
    ('UNSPECIFIED', 'Unspecified', NULL)
ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name;

-- ========== Faith (secondary) ==========
INSERT INTO faith (code, name, parent_id) SELECT 'ROMAN_CATHOLIC', 'Roman Catholic', id FROM faith WHERE code = 'CHRISTIANITY' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'EASTERN_ORTHODOX', 'Eastern Orthodox', id FROM faith WHERE code = 'CHRISTIANITY' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'PROTESTANT', 'Protestant', id FROM faith WHERE code = 'CHRISTIANITY' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'EVANGELICAL', 'Evangelical', id FROM faith WHERE code = 'CHRISTIANITY' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO faith (code, name, parent_id) SELECT 'SUNNI', 'Sunni', id FROM faith WHERE code = 'ISLAM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'SHIA', 'Shia', id FROM faith WHERE code = 'ISLAM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO faith (code, name, parent_id) SELECT 'ORTHODOX_JUDAISM', 'Orthodox Judaism', id FROM faith WHERE code = 'JUDAISM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'REFORM_JUDAISM', 'Reform Judaism', id FROM faith WHERE code = 'JUDAISM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'SECULAR_CULTURAL_JEWISH', 'Secular / Cultural Jewish', id FROM faith WHERE code = 'JUDAISM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO faith (code, name, parent_id) SELECT 'HINDU', 'Hindu', id FROM faith WHERE code = 'HINDUISM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO faith (code, name, parent_id) SELECT 'THERAVADA', 'Theravada', id FROM faith WHERE code = 'BUDDHISM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'MAHAYANA', 'Mahayana', id FROM faith WHERE code = 'BUDDHISM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO faith (code, name, parent_id) SELECT 'SIKH', 'Sikh', id FROM faith WHERE code = 'SIKHISM' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;

INSERT INTO faith (code, name, parent_id) SELECT 'ATHEIST', 'Atheist', id FROM faith WHERE code = 'NON_RELIGIOUS' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'AGNOSTIC', 'Agnostic', id FROM faith WHERE code = 'NON_RELIGIOUS' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
INSERT INTO faith (code, name, parent_id) SELECT 'SECULAR', 'Secular / non-practicing', id FROM faith WHERE code = 'NON_RELIGIOUS' ON CONFLICT (code) DO UPDATE SET name = EXCLUDED.name, parent_id = EXCLUDED.parent_id;
