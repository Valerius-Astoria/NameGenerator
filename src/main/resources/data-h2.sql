-- Curated seed data for the design form (common options only).
-- Full catalogs remain in docs/countries.md, docs/races.md, docs/religions.md.
-- Uses H2 MERGE ... KEY(code) so the script is idempotent against the persistent file DB.

-- ========== Countries ==========
MERGE INTO country (code, name) KEY(code) VALUES ('AR', 'Argentina');
MERGE INTO country (code, name) KEY(code) VALUES ('AU', 'Australia');
MERGE INTO country (code, name) KEY(code) VALUES ('BR', 'Brazil');
MERGE INTO country (code, name) KEY(code) VALUES ('CA', 'Canada');
MERGE INTO country (code, name) KEY(code) VALUES ('CN', 'China');
MERGE INTO country (code, name) KEY(code) VALUES ('EG', 'Egypt');
MERGE INTO country (code, name) KEY(code) VALUES ('FR', 'France');
MERGE INTO country (code, name) KEY(code) VALUES ('DE', 'Germany');
MERGE INTO country (code, name) KEY(code) VALUES ('IN', 'India');
MERGE INTO country (code, name) KEY(code) VALUES ('ID', 'Indonesia');
MERGE INTO country (code, name) KEY(code) VALUES ('IE', 'Ireland');
MERGE INTO country (code, name) KEY(code) VALUES ('IT', 'Italy');
MERGE INTO country (code, name) KEY(code) VALUES ('JP', 'Japan');
MERGE INTO country (code, name) KEY(code) VALUES ('MX', 'Mexico');
MERGE INTO country (code, name) KEY(code) VALUES ('NG', 'Nigeria');
MERGE INTO country (code, name) KEY(code) VALUES ('PH', 'Philippines');
MERGE INTO country (code, name) KEY(code) VALUES ('PL', 'Poland');
MERGE INTO country (code, name) KEY(code) VALUES ('RU', 'Russia');
MERGE INTO country (code, name) KEY(code) VALUES ('ZA', 'South Africa');
MERGE INTO country (code, name) KEY(code) VALUES ('KR', 'South Korea');
MERGE INTO country (code, name) KEY(code) VALUES ('SU', 'Soviet Union');
MERGE INTO country (code, name) KEY(code) VALUES ('ES', 'Spain');
MERGE INTO country (code, name) KEY(code) VALUES ('SE', 'Sweden');
MERGE INTO country (code, name) KEY(code) VALUES ('TH', 'Thailand');
MERGE INTO country (code, name) KEY(code) VALUES ('TR', 'Türkiye');
MERGE INTO country (code, name) KEY(code) VALUES ('GB', 'United Kingdom');
MERGE INTO country (code, name) KEY(code) VALUES ('US', 'United States of America');
MERGE INTO country (code, name) KEY(code) VALUES ('VN', 'Vietnam');

-- ========== Ancestry (primary) ==========
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('EAST_ASIAN', 'East Asian', NULL);
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('SOUTHEAST_ASIAN', 'Southeast Asian', NULL);
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('SOUTH_ASIAN', 'South Asian', NULL);
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('MIDDLE_EASTERN', 'Middle Eastern', NULL);
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('EUROPEAN', 'European', NULL);
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('LATINO_HISPANIC', 'Latino / Hispanic', NULL);
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('BLACK_AFRICAN_DIASPORA', 'Black / African Diaspora', NULL);
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('JEWISH', 'Jewish', NULL);
MERGE INTO ancestry (code, name, parent_id) KEY(code) VALUES ('UNSPECIFIED', 'Unspecified', NULL);

-- Drop retired Mixed / Multiracial option (and any profile links) on restart
DELETE FROM character_profile_ancestries WHERE ancestries_id IN (SELECT id FROM ancestry WHERE code = 'MIXED_MULTIRACIAL');
DELETE FROM ancestry WHERE code = 'MIXED_MULTIRACIAL';

-- ========== Ancestry (secondary) ==========
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'HAN_CHINESE', 'Han Chinese', id FROM ancestry WHERE code = 'EAST_ASIAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'JAPANESE', 'Japanese', id FROM ancestry WHERE code = 'EAST_ASIAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'KOREAN', 'Korean', id FROM ancestry WHERE code = 'EAST_ASIAN';

MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'VIETNAMESE', 'Vietnamese', id FROM ancestry WHERE code = 'SOUTHEAST_ASIAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'THAI', 'Thai', id FROM ancestry WHERE code = 'SOUTHEAST_ASIAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'FILIPINO', 'Filipino', id FROM ancestry WHERE code = 'SOUTHEAST_ASIAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'INDONESIAN', 'Indonesian', id FROM ancestry WHERE code = 'SOUTHEAST_ASIAN';

MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'HINDUSTANI', 'Hindustani', id FROM ancestry WHERE code = 'SOUTH_ASIAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'TAMIL', 'Tamil', id FROM ancestry WHERE code = 'SOUTH_ASIAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'BENGALI', 'Bengali', id FROM ancestry WHERE code = 'SOUTH_ASIAN';

MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'ARAB', 'Arab', id FROM ancestry WHERE code = 'MIDDLE_EASTERN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'PERSIAN', 'Persian', id FROM ancestry WHERE code = 'MIDDLE_EASTERN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'TURKISH', 'Turkish', id FROM ancestry WHERE code = 'MIDDLE_EASTERN';

MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'ANGLO', 'Anglo', id FROM ancestry WHERE code = 'EUROPEAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'GERMANIC', 'Germanic', id FROM ancestry WHERE code = 'EUROPEAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'ROMANCE', 'Romance', id FROM ancestry WHERE code = 'EUROPEAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'SLAVIC', 'Slavic', id FROM ancestry WHERE code = 'EUROPEAN';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'NORDIC', 'Nordic', id FROM ancestry WHERE code = 'EUROPEAN';

MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'MEXICAN', 'Mexican', id FROM ancestry WHERE code = 'LATINO_HISPANIC';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'CARIBBEAN_LATINO', 'Caribbean Latino', id FROM ancestry WHERE code = 'LATINO_HISPANIC';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'SOUTH_AMERICAN', 'South American', id FROM ancestry WHERE code = 'LATINO_HISPANIC';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'BRAZILIAN', 'Brazilian', id FROM ancestry WHERE code = 'LATINO_HISPANIC';

MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'AFRICAN_AMERICAN', 'African American', id FROM ancestry WHERE code = 'BLACK_AFRICAN_DIASPORA';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'AFRO_CARIBBEAN', 'Afro-Caribbean', id FROM ancestry WHERE code = 'BLACK_AFRICAN_DIASPORA';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'WEST_AFRICAN', 'West African', id FROM ancestry WHERE code = 'BLACK_AFRICAN_DIASPORA';

MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'ASHKENAZI', 'Ashkenazi', id FROM ancestry WHERE code = 'JEWISH';
MERGE INTO ancestry (code, name, parent_id) KEY(code) SELECT 'SEPHARDI', 'Sephardi', id FROM ancestry WHERE code = 'JEWISH';

-- ========== Faith (primary) ==========
MERGE INTO faith (code, name, parent_id) KEY(code) VALUES ('CHRISTIANITY', 'Christianity', NULL);
MERGE INTO faith (code, name, parent_id) KEY(code) VALUES ('ISLAM', 'Islam', NULL);
MERGE INTO faith (code, name, parent_id) KEY(code) VALUES ('JUDAISM', 'Judaism', NULL);
MERGE INTO faith (code, name, parent_id) KEY(code) VALUES ('HINDUISM', 'Hinduism', NULL);
MERGE INTO faith (code, name, parent_id) KEY(code) VALUES ('BUDDHISM', 'Buddhism', NULL);
MERGE INTO faith (code, name, parent_id) KEY(code) VALUES ('SIKHISM', 'Sikhism', NULL);
MERGE INTO faith (code, name, parent_id) KEY(code) VALUES ('NON_RELIGIOUS', 'Non-religious / secular', NULL);
MERGE INTO faith (code, name, parent_id) KEY(code) VALUES ('UNSPECIFIED', 'Unspecified', NULL);

-- ========== Faith (secondary) ==========
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'ROMAN_CATHOLIC', 'Roman Catholic', id FROM faith WHERE code = 'CHRISTIANITY';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'EASTERN_ORTHODOX', 'Eastern Orthodox', id FROM faith WHERE code = 'CHRISTIANITY';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'PROTESTANT', 'Protestant', id FROM faith WHERE code = 'CHRISTIANITY';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'EVANGELICAL', 'Evangelical', id FROM faith WHERE code = 'CHRISTIANITY';

MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'SUNNI', 'Sunni', id FROM faith WHERE code = 'ISLAM';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'SHIA', 'Shia', id FROM faith WHERE code = 'ISLAM';

MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'ORTHODOX_JUDAISM', 'Orthodox Judaism', id FROM faith WHERE code = 'JUDAISM';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'REFORM_JUDAISM', 'Reform Judaism', id FROM faith WHERE code = 'JUDAISM';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'SECULAR_CULTURAL_JEWISH', 'Secular / Cultural Jewish', id FROM faith WHERE code = 'JUDAISM';

MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'HINDU', 'Hindu', id FROM faith WHERE code = 'HINDUISM';

MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'THERAVADA', 'Theravada', id FROM faith WHERE code = 'BUDDHISM';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'MAHAYANA', 'Mahayana', id FROM faith WHERE code = 'BUDDHISM';

MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'SIKH', 'Sikh', id FROM faith WHERE code = 'SIKHISM';

MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'ATHEIST', 'Atheist', id FROM faith WHERE code = 'NON_RELIGIOUS';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'AGNOSTIC', 'Agnostic', id FROM faith WHERE code = 'NON_RELIGIOUS';
MERGE INTO faith (code, name, parent_id) KEY(code) SELECT 'SECULAR', 'Secular / non-practicing', id FROM faith WHERE code = 'NON_RELIGIOUS';
