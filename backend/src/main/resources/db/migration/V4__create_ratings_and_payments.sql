CREATE TABLE ratings (
                         id         UUID         PRIMARY KEY,
                         ride_id    UUID         NOT NULL,
                         rater_id   UUID         NOT NULL,
                         rated_id   UUID         NOT NULL,
                         score      SMALLINT     NOT NULL,
                         comment    VARCHAR(500),
                         created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
                         CONSTRAINT fk_ratings_ride FOREIGN KEY (ride_id) REFERENCES rides (id),
                         CONSTRAINT fk_ratings_rater FOREIGN KEY (rater_id) REFERENCES users (id),
                         CONSTRAINT fk_ratings_rated FOREIGN KEY (rated_id) REFERENCES users (id),
                         CONSTRAINT uq_ratings_ride_rater UNIQUE (ride_id, rater_id),
                         CONSTRAINT ck_ratings_score CHECK (score BETWEEN 1 AND 5),
                         CONSTRAINT ck_ratings_not_self CHECK (rater_id <> rated_id)
);

CREATE INDEX idx_ratings_rated ON ratings (rated_id);

CREATE TABLE payments (
                          id           UUID        PRIMARY KEY,
                          ride_id      UUID        NOT NULL,
                          amount_cents BIGINT      NOT NULL,
                          method       VARCHAR(10) NOT NULL,
                          status       VARCHAR(10) NOT NULL DEFAULT 'PENDING',
                          paid_at      TIMESTAMPTZ,
                          created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                          CONSTRAINT fk_payments_ride FOREIGN KEY (ride_id) REFERENCES rides (id),
                          CONSTRAINT uq_payments_ride UNIQUE (ride_id),
                          CONSTRAINT ck_payments_amount CHECK (amount_cents >= 0),
                          CONSTRAINT ck_payments_method CHECK (method IN ('CARD', 'PIX', 'CASH')),
                          CONSTRAINT ck_payments_status CHECK (status IN ('PENDING', 'PAID', 'FAILED')),
                          CONSTRAINT ck_payments_paid_consistency CHECK ((status = 'PAID') = (paid_at IS NOT NULL))
);
