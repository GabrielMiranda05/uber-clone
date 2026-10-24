CREATE TABLE vehicles (
                          id         UUID        PRIMARY KEY,
                          driver_id  UUID        NOT NULL,
                          plate      VARCHAR(10) NOT NULL,
                          model      VARCHAR(60) NOT NULL,
                          color      VARCHAR(30) NOT NULL,
                          model_year SMALLINT    NOT NULL,
                          CONSTRAINT fk_vehicles_driver FOREIGN KEY (driver_id) REFERENCES driver_profiles (user_id),
                          CONSTRAINT uq_vehicles_driver UNIQUE (driver_id),
                          CONSTRAINT uq_vehicles_plate UNIQUE (plate),
                          CONSTRAINT ck_vehicles_year CHECK (model_year BETWEEN 1990 AND 2100)
);

CREATE TABLE rides (
                       id                    UUID             PRIMARY KEY,
                       passenger_id          UUID             NOT NULL,
                       driver_id             UUID,
                       status                VARCHAR(20)      NOT NULL DEFAULT 'REQUESTED',
                       origin_latitude       DOUBLE PRECISION NOT NULL,
                       origin_longitude      DOUBLE PRECISION NOT NULL,
                       origin_address        VARCHAR(255),
                       destination_latitude  DOUBLE PRECISION NOT NULL,
                       destination_longitude DOUBLE PRECISION NOT NULL,
                       destination_address   VARCHAR(255),
                       distance_meters       INTEGER          NOT NULL,
                       estimated_price_cents BIGINT           NOT NULL,
                       final_price_cents     BIGINT,
                       requested_at          TIMESTAMPTZ      NOT NULL DEFAULT now(),
                       accepted_at           TIMESTAMPTZ,
                       started_at            TIMESTAMPTZ,
                       completed_at          TIMESTAMPTZ,
                       cancelled_at          TIMESTAMPTZ,
                       cancelled_by          VARCHAR(10),
                       cancel_reason         VARCHAR(255),
                       CONSTRAINT fk_rides_passenger FOREIGN KEY (passenger_id) REFERENCES users (id),
                       CONSTRAINT fk_rides_driver FOREIGN KEY (driver_id) REFERENCES driver_profiles (user_id),
                       CONSTRAINT ck_rides_status CHECK (status IN
                                                         ('REQUESTED', 'ACCEPTED', 'DRIVER_EN_ROUTE', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
                       CONSTRAINT ck_rides_cancelled_by CHECK (cancelled_by IN ('PASSENGER', 'DRIVER', 'SYSTEM')),
                       CONSTRAINT ck_rides_coordinates CHECK (
                           origin_latitude BETWEEN -90 AND 90 AND destination_latitude BETWEEN -90 AND 90
                               AND origin_longitude BETWEEN -180 AND 180 AND destination_longitude BETWEEN -180 AND 180),
                       CONSTRAINT ck_rides_distance CHECK (distance_meters >= 0),
                       CONSTRAINT ck_rides_prices CHECK (
                           estimated_price_cents >= 0 AND (final_price_cents IS NULL OR final_price_cents >= 0)),
                       CONSTRAINT ck_rides_driver_required CHECK (
                           status IN ('REQUESTED', 'CANCELLED') OR driver_id IS NOT NULL),
                       CONSTRAINT ck_rides_cancel_consistency CHECK ((status = 'CANCELLED') = (cancelled_at IS NOT NULL))
);

CREATE INDEX idx_rides_passenger ON rides (passenger_id, requested_at DESC);
CREATE INDEX idx_rides_driver ON rides (driver_id, requested_at DESC);

CREATE UNIQUE INDEX uq_rides_active_passenger ON rides (passenger_id)
    WHERE status IN ('REQUESTED', 'ACCEPTED', 'DRIVER_EN_ROUTE', 'IN_PROGRESS');

CREATE UNIQUE INDEX uq_rides_active_driver ON rides (driver_id)
    WHERE status IN ('ACCEPTED', 'DRIVER_EN_ROUTE', 'IN_PROGRESS');
