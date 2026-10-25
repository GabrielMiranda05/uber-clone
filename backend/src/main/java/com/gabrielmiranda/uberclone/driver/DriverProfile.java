package com.gabrielmiranda.uberclone.driver;

import com.gabrielmiranda.uberclone.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DriverProfile {

    @Id
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 30)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DriverStatus status = DriverStatus.OFFLINE;

    private Double lastLatitude;

    private Double lastLongitude;

    private Instant lastLocationAt;

    public DriverProfile(User user, String licenseNumber) {
        this.user = user;
        this.licenseNumber = licenseNumber;
    }
}
