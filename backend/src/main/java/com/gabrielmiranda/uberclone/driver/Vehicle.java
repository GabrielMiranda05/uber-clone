package com.gabrielmiranda.uberclone.driver;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Locale;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "vehicles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private DriverProfile driver;

    @Column(nullable = false, length = 10)
    private String plate;

    @Column(nullable = false, length = 60)
    private String model;

    @Column(nullable = false, length = 30)
    private String color;

    @Column(nullable = false)
    private short modelYear;

    public Vehicle(DriverProfile driver, String plate, String model, String color, short modelYear) {
        this.driver = driver;
        this.plate = plate.toUpperCase(Locale.ROOT);
        this.model = model;
        this.color = color;
        this.modelYear = modelYear;
    }
}
