package com.chiops.vehicle.libs.dtos;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Serdeable
@Introspected
public class VehicleDTO {

    @NotBlank
    private String vin;

    @NotBlank
    private String plate;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    private LocalDate purchaseDate;

    @NotNull
    private LocalDate registrationDate;

    @NotNull
    private BigDecimal cost;

    private String photoUrl;

    private String assigmentStatus;

    private Long assignmentId;

    public VehicleDTO () {
    }

    public VehicleDTO(String vin, String plate, String brand, String model, LocalDate purchaseDate, LocalDate registrationDate, BigDecimal cost, String photoUrl, String assigmentStatus, Long assignmentId) {
        this.vin = vin;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.purchaseDate = purchaseDate;
        this.registrationDate = registrationDate;
        this.cost = cost;
        this.photoUrl = photoUrl;
        this.assigmentStatus = assigmentStatus;
        this.assignmentId = assignmentId;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAssigmentStatus() {
        return assigmentStatus;
    }

    public void setAssigmentStatus(String assigmentStatus) {
        this.assigmentStatus = assigmentStatus;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
