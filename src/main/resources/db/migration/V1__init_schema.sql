CREATE TABLE companies (
                           id         UUID PRIMARY KEY,
                           name       VARCHAR(255) NOT NULL,
                           website    VARCHAR(255),
                           street     VARCHAR(255),
                           postal_code VARCHAR(20),
                           city       VARCHAR(255),
                           created_at TIMESTAMP    NOT NULL,
                           updated_at TIMESTAMP
);

CREATE UNIQUE INDEX ux_companies_name ON companies (LOWER(TRIM(name)));

CREATE TABLE job_postings (
                              id           UUID PRIMARY KEY,
                              company_id   UUID          NOT NULL REFERENCES companies (id),
                              title        VARCHAR(255)  NOT NULL,
                              description  TEXT,
                              location     VARCHAR(255),
                              url          VARCHAR(1000) NOT NULL,
                              status       VARCHAR(50)   NOT NULL,
                              published_at DATE,
                              archived_at  DATE,
                              created_at   TIMESTAMP     NOT NULL,
                              updated_at   TIMESTAMP
);

CREATE INDEX ix_job_postings_company_id ON job_postings (company_id);

CREATE TABLE note_categories (
                                 id         UUID PRIMARY KEY,
                                 name       VARCHAR(255) NOT NULL,
                                 created_at TIMESTAMP    NOT NULL,
                                 updated_at TIMESTAMP
);

CREATE UNIQUE INDEX ux_note_categories_name ON note_categories (LOWER(TRIM(name)));

CREATE TABLE applications (
                              id             UUID PRIMARY KEY,
                              job_posting_id UUID         NOT NULL UNIQUE REFERENCES job_postings (id),
                              status         VARCHAR(50)  NOT NULL,
                              priority       VARCHAR(50)  NOT NULL,
                              applied_at     DATE,
                              applied_via    VARCHAR(255),
                              portal_url     VARCHAR(1000),
                              closed_at      DATE,
                              created_at     TIMESTAMP    NOT NULL,
                              updated_at     TIMESTAMP
);

CREATE TABLE notes (
                                   id               UUID PRIMARY KEY,
                                   application_id   UUID         REFERENCES applications (id) ON DELETE SET NULL,
                                   note_category_id UUID         REFERENCES note_categories (id) ON DELETE SET NULL,
                                   title            VARCHAR(255) NOT NULL,
                                   content          TEXT,
                                   created_at       TIMESTAMP    NOT NULL,
                                   updated_at       TIMESTAMP
);

CREATE INDEX ix_notes_application_id ON notes (application_id);
CREATE INDEX ix_notes_note_category_id ON notes (note_category_id);
