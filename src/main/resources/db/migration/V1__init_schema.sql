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

CREATE TABLE job_postings (
                              id           UUID PRIMARY KEY,
                              company_id   UUID          NOT NULL REFERENCES companies (id),
                              title        VARCHAR(255)  NOT NULL,
                              description  TEXT,
                              location     VARCHAR(255),
                              url          VARCHAR(1000) NOT NULL,
                              status       VARCHAR(50)   NOT NULL,
                              published_at DATE,
                              created_at   TIMESTAMP     NOT NULL,
                              updated_at   TIMESTAMP
);

CREATE TABLE note_categories (
                                 id         UUID PRIMARY KEY,
                                 name       VARCHAR(255) NOT NULL UNIQUE,
                                 created_at TIMESTAMP    NOT NULL,
                                 updated_at TIMESTAMP
);

CREATE TABLE applications (
                              id             UUID PRIMARY KEY,
                              job_posting_id UUID         NOT NULL UNIQUE REFERENCES job_postings (id),
                              status         VARCHAR(50)  NOT NULL,
                              priority       VARCHAR(50),
                              applied_at     DATE,
                              applied_via    VARCHAR(255),
                              portal_url     VARCHAR(1000),
                              closed_at      DATE,
                              created_at     TIMESTAMP    NOT NULL,
                              updated_at     TIMESTAMP
);

CREATE TABLE application_notes (
                                   id               UUID PRIMARY KEY,
                                   application_id   UUID         NOT NULL REFERENCES applications (id) ON DELETE CASCADE,
                                   note_category_id UUID         REFERENCES note_categories (id) ON DELETE SET NULL,
                                   title            VARCHAR(255) NOT NULL,
                                   content          TEXT,
                                   created_at       TIMESTAMP    NOT NULL,
                                   updated_at       TIMESTAMP
);
