package com.chiops.vehicle.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vehicle_identification")
public class VehicleIdentification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;

    private LocalDate purchasedDate;


    private BigDecimal price;

    @OneToOne(mappedBy = "identification")
    private Vehicle vehicle;

    public VehicleIdentification() {
    }

    public VehicleIdentification(Vehicle vehicle,String plate, LocalDate purchasedDate, BigDecimal price) {
        this.plate = plate;
        this.purchasedDate = purchasedDate;
        this.vehicle = vehicle;
        this.price = price;
    }


    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(LocalDate purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
