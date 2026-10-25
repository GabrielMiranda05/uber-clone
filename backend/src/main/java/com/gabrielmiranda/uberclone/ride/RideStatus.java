package com.gabrielmiranda.uberclone.ride;

import java.util.EnumSet;
import java.util.Set;

public enum RideStatus {
    REQUESTED,
    ACCEPTED,
    DRIVER_EN_ROUTE,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public static final Set<RideStatus> ACTIVE =
        EnumSet.of(REQUESTED, ACCEPTED, DRIVER_EN_ROUTE, IN_PROGRESS);

}
