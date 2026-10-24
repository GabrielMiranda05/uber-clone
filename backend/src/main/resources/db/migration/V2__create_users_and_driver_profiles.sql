CREATE TABLE users (
                       id            UUID         PRIMARY KEY,
                       name          VARCHAR(120) NOT NULL,
                       email         VARCHAR(254) NOT NULL,
                       phone         VARCHAR(20)  NOT NULL,
                       password_hash VARCHAR(100) NOT NULL,
                       role          VARCHAR(20)  NOT NULL,
                       created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
                       updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
                       CONSTRAINT ck_users_role CHECK (role IN ('PASSENGER', 'DRIVER'))
);

CREATE UNIQUE INDEX uq_users_email_lower ON users (lower(email));

CREATE TABLE driver_profiles (
                                 user_id          UUID             PRIMARY KEY REFERENCES users (id),
                                 license_number   VARCHAR(30)      NOT NULL,
                                 status           VARCHAR(10)      NOT NULL DEFAULT 'OFFLINE',
                                 last_latitude    DOUBLE PRECISION,
                                 last_longitude   DOUBLE PRECISION,
                                 last_location_at TIMESTAMPTZ,
                                 CONSTRAINT ck_driver_profiles_status CHECK (status IN ('OFFLINE', 'ONLINE')),
                                 CONSTRAINT ck_driver_profiles_latitude CHECK (last_latitude BETWEEN -90 AND 90),
                                 CONSTRAINT ck_driver_profiles_longitude CHECK (last_longitude BETWEEN -180 AND 180)
);
