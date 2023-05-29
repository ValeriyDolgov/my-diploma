CREATE TABLE "users"
(
    "id"                   BIGSERIAL PRIMARY KEY,
    "surname"              varchar,
    "name"                 varchar,
    "patronymic"           varchar,
    "email"                varchar,
    "birthday"             date,
    "password"             varchar,
    "sex"                  varchar,
    "country"              varchar,
    "city"                 varchar,
    "reset_password_token" varchar,
    "nickname"             varchar,
    "is_active"            boolean,
    "is_ceo"               boolean,
    "roles"                varchar(2048),
    "employee_id"          bigint
);

CREATE TABLE "city"
(
    "city_id"    SERIAL PRIMARY KEY,
    "city_name"  varchar,
    "country_id" int
);

CREATE TABLE "country"
(
    "country_id"   SERIAL PRIMARY KEY,
    "country_name" varchar
);

CREATE TABLE "role"
(
    "role_id" SERIAL PRIMARY KEY,
    "name"    varchar
);

CREATE TABLE "user_role"
(
    "user_id" bigint,
    "role_id" bigint
);

CREATE TABLE "employee"
(
    "id"                   BIGSERIAL PRIMARY KEY,
    "date_of_hire"         date,
    "date_of_dismissal"    date,
    "reason_for_dismissal" varchar,
    "avatar"               varchar,
    "date_of_creation"     timestamp,
    "date_of_update"       timestamp,
    "is_on_remote"         boolean,
    "note"                 text,
    "timezone"             varchar,
    "position"             varchar(2048)
);

CREATE TABLE "position"
(
    "id"    SERIAL PRIMARY KEY,
    "name"  varchar,
    "level" varchar
);

CREATE TABLE "employee_position"
(
    "employee_id" bigint,
    "position_id" int
);

CREATE TABLE "contact_info"
(
    "id"              BIGSERIAL PRIMARY KEY,
    "employee_id"     bigint,
    "office_number"   varchar,
    "personal_number" varchar,
    "office_email"    varchar,
    "personal_email"  varchar,
    "telegram_acc"    varchar,
    "skype_acc"       varchar
);

CREATE TABLE "relative"
(
    "id"           BIGSERIAL PRIMARY KEY,
    "surname"      varchar,
    "name"         varchar,
    "patronymic"   varchar,
    "relation"     varchar,
    "phone_number" varchar
);

CREATE TABLE "employee_relative"
(
    "employee_id" bigint,
    "relative_id" bigint
);

CREATE TABLE "address"
(
    "id"                BIGSERIAL PRIMARY KEY,
    "country"           varchar,
    "region"            varchar,
    "city"              varchar,
    "street"            varchar,
    "house_number"      varchar,
    "apartement_number" int,
    "is_current"        boolean,
    "is_registered"     boolean
);

CREATE TABLE "employee_address"
(
    "employee_id" bigint,
    "address_id"  bigint
);

CREATE TABLE "vacation"
(
    "id"                  BIGSERIAL PRIMARY KEY,
    "employee_id"         bigint,
    "replacement_user_id" bigint,
    "start_date"          date,
    "end_date"            date,
    "update_date"         timestamp
);

CREATE TABLE "skill"
(
    "id"    BIGSERIAL PRIMARY KEY,
    "name"  varchar,
    "level" varchar
);

CREATE TABLE "file_data"
(
    "file_id"    BIGSERIAL PRIMARY KEY,
    "filename"   varchar,
    "size"       bigint,
    "url"        varchar,
    "user_id"    bigint,
    "article_id" bigint
);

CREATE TABLE "article"
(
    "id"               BIGSERIAL PRIMARY KEY,
    "title"            varchar(400),
    "slug"             varchar,
    "description"      varchar(1000),
    "text"             text,
    "date_of_creation" date,
    "date_of_update"   date,
    "user_id"          bigint,
    "collaborators"    varchar(255),
    "is_moderated"     boolean,
    "is_published"     boolean
);

CREATE TABLE "employee_skill"
(
    "employee_id" bigint,
    "skill_id"    bigint
);

CREATE TABLE "organization"
(
    "id"   BIGSERIAL PRIMARY KEY,
    "name" varchar
);

CREATE TABLE "employee_knowledge"
(
    "id"                          BIGSERIAL PRIMARY KEY,
    "db"                          varchar,
    "patterns_principles_methods" varchar,
    "rest"                        varchar,
    "methods_and_organizations"   date,
    "services"                    varchar,
    "programming_languages"       varchar,
    "frameworks"                  varchar,
    "libraries"                   varchar
);

CREATE TABLE "knowledges"
(
    "id"       BIGSERIAL PRIMARY KEY,
    "type"     varchar,
    "value"    varchar,
    "language" varchar
);

CREATE TABLE "companies"
(
    "company_id"         BIGSERIAL PRIMARY KEY,
    "name"               varchar,
    "contracts"          varchar,
    "counterparty"       varchar,
    "found_resource"     varchar,
    "is_kazakhstan"      boolean,
    "mail"               varchar,
    "nuances"            varchar,
    "official_site"      varchar,
    "responsible"        varchar,
    "status"             varchar,
    "update_date"        timestamp,
    "city_city_id"       int,
    "country_country_id" int
);

CREATE TABLE "projects"
(
    "project_id"            BIGSERIAL PRIMARY KEY,
    "equipment_transaction" varchar,
    "feedback"              varchar,
    "is_active"             boolean,
    "modules_count"         int,
    "name"                  varchar,
    "terms_of_payment"      varchar,
    "company_id"            bigint
);

CREATE TABLE "employee_project"
(
    "employee_id" bigint,
    "project_id"  bigint
);

CREATE TABLE "projects_partners_employees"
(
    "project_project_id"    int,
    "partners_employees_id" int
);

CREATE TABLE "projects_project_team"
(
    "project_project_id" int,
    "project_team_id"    int
);

CREATE TABLE "partners_employee"
(
    "id"        BIGSERIAL PRIMARY KEY,
    "fio"       varchar,
    "is_active" boolean,
    "mail"      varchar,
    "phone_num" varchar,
    "position"  varchar,
    "telegram"  varchar
);

CREATE TABLE "contact_person"
(
    "contact_id"     BIGSERIAL PRIMARY KEY,
    "decision_maker" boolean,
    "dob"            timestamp,
    "fio"            varchar,
    "is_active"      boolean,
    "linked_in"      varchar,
    "mail"           varchar,
    "phone_number"   varchar,
    "position"       varchar,
    "skype"          varchar,
    "telegram"       varchar,
    "company_id"     int
);

CREATE TABLE "departments"
(
    "id"                    BIGSERIAL PRIMARY KEY,
    "name"                  varchar,
    "head_of_department_id" bigint
);

CREATE TABLE "user_department"
(
    "user_id"       bigint,
    "department_id" bigint
);

ALTER TABLE "city"
    ADD FOREIGN KEY ("country_id") REFERENCES "country" ("country_id");

ALTER TABLE "employee_project"
    ADD FOREIGN KEY ("employee_id") REFERENCES "employee" ("id");

ALTER TABLE "employee_project"
    ADD FOREIGN KEY ("project_id") REFERENCES "projects" ("project_id");

ALTER TABLE "projects_partners_employees"
    ADD FOREIGN KEY ("project_project_id") REFERENCES "projects" ("project_id");

ALTER TABLE "projects_partners_employees"
    ADD FOREIGN KEY ("partners_employees_id") REFERENCES "partners_employee" ("id");

ALTER TABLE "projects_project_team"
    ADD FOREIGN KEY ("project_project_id") REFERENCES "projects" ("project_id");

ALTER TABLE "projects_project_team"
    ADD FOREIGN KEY ("project_team_id") REFERENCES "employee" ("id");

ALTER TABLE "contact_person"
    ADD FOREIGN KEY ("company_id") REFERENCES "companies" ("company_id");

ALTER TABLE "projects_partners_employees"
    ADD FOREIGN KEY ("project_project_id") REFERENCES "projects" ("project_id");

ALTER TABLE "projects_partners_employees"
    ADD FOREIGN KEY ("partners_employees_id") REFERENCES "partners_employee" ("id");

ALTER TABLE "projects"
    ADD FOREIGN KEY ("company_id") REFERENCES "companies" ("company_id");

ALTER TABLE "companies"
    ADD FOREIGN KEY ("city_city_id") REFERENCES "city" ("city_id");

ALTER TABLE "companies"
    ADD FOREIGN KEY ("country_country_id") REFERENCES "country" ("country_id");

ALTER TABLE "user_role"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_role"
    ADD FOREIGN KEY ("role_id") REFERENCES "role" ("role_id");

ALTER TABLE "file_data"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "employee_position"
    ADD FOREIGN KEY ("employee_id") REFERENCES "employee" ("id");

ALTER TABLE "employee_position"
    ADD FOREIGN KEY ("position_id") REFERENCES "position" ("id");

ALTER TABLE "contact_info"
    ADD FOREIGN KEY ("employee_id") REFERENCES "employee" ("id");

ALTER TABLE "users"
    ADD FOREIGN KEY ("employee_id") REFERENCES "employee" ("id");

ALTER TABLE "employee_relative"
    ADD FOREIGN KEY ("employee_id") REFERENCES "employee" ("id");

ALTER TABLE "employee_relative"
    ADD FOREIGN KEY ("relative_id") REFERENCES "relative" ("id");

ALTER TABLE "employee_address"
    ADD FOREIGN KEY ("employee_id") REFERENCES "employee" ("id");

ALTER TABLE "employee_address"
    ADD FOREIGN KEY ("address_id") REFERENCES "address" ("id");

ALTER TABLE "vacation"
    ADD FOREIGN KEY ("employee_id") REFERENCES "employee" ("id");

ALTER TABLE "vacation"
    ADD FOREIGN KEY ("replacement_user_id") REFERENCES "users" ("id");

ALTER TABLE "employee_skill"
    ADD FOREIGN KEY ("employee_id") REFERENCES "employee" ("id");

ALTER TABLE "employee_skill"
    ADD FOREIGN KEY ("skill_id") REFERENCES "skill" ("id");

ALTER TABLE "departments"
    ADD FOREIGN KEY ("head_of_department_id") REFERENCES "users" ("id");

ALTER TABLE "user_department"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_department"
    ADD FOREIGN KEY ("department_id") REFERENCES "departments" ("id");

ALTER TABLE "article"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");