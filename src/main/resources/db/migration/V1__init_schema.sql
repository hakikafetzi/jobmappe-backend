CREATE TABLE companies (
                         id         UUID PRIMARY KEY,
                         name       VARCHAR(255) NOT NULL,
                         website    VARCHAR(255),
                         created_at TIMESTAMP    NOT NULL,
                         updated_at TIMESTAMP
);

CREATE TABLE job_postings (
                             id                  UUID PRIMARY KEY,
                             company_id          UUID         NOT NULL REFERENCES company (id),
                             title               VARCHAR(255) NOT NULL,
                             description         TEXT,
                             location            VARCHAR(255),
                             url                 VARCHAR(1000) NOT NULL,
                             status              VARCHAR(50)  NOT NULL,
                             published_at        DATE NOT NULL,
                             created_at          TIMESTAMP    NOT NULL,
                             updated_at          TIMESTAMP
);
