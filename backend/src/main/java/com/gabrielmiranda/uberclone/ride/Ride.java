package com.gabrielmiranda.uberclone.ride;

import com.gabrielmiranda.uberclone.driver.DriverProfile;
import com.gabrielmiranda.uberclone.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "rides")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ride {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private DriverProfile driver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RideStatus status = RideStatus.REQUESTED;

    @Column(nullable = false)
    private double originLatitude;

    @Column(nullable = false)
    private double originLongitude;

    @Column(length = 255)
    private String originAddress;

    @Column(nullable = false)
    private double destinationLatitude;

    @Column(nullable = false)
    private double destinationLongitude;

    @Column(length = 255)
    private String destinationAddress;

    @Column(nullable = false)
    private int distanceMeters;

    @Column(nullable = false)
    private long estimatedPriceCents;

    private Long finalPriceCents;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant requestedAt;

    private Instant acceptedAt;

    private Instant startedAt;

    private Instant completedAt;

    private Instant cancelledAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CancelledBy cancelledBy;

    @Column(length = 255)
    private String cancelReason;

    public Ride(
        User passenger,
        double originLatitude,
        double originLongitude,
        String originAddress,
        double destinationLatitude,
        double destinationLongitude,
        String destinationAddress,
        int distanceMeters,
        long estimatedPriceCents) {
        this.passenger = passenger;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.originAddress = originAddress;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.destinationAddress = destinationAddress;
        this.distanceMeters = distanceMeters;
        this.estimatedPriceCents = estimatedPriceCents;
    }
}
