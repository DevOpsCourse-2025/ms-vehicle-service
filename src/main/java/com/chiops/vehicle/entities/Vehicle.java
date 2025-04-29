package com.chiops.vehicle.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(nullable = false, unique = true)
    private String vin;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "identification_id")
    private VehicleIdentification identification;

    private LocalDate registrationDate;

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private VehicleAssignment vehicleAssignment;

    public Vehicle() {
    }

    public Vehicle(String vin, Model model, VehicleIdentification identification, LocalDate registrationDate, VehicleAssignment vehicleAssignment) {
        this.vin = vin;
        this.model = model;
        this.identification = identification;
        this.registrationDate = registrationDate;
        this.vehicleAssignment = vehicleAssignment;
    }

    public VehicleAssignment getVehicleAssignment() {
        return vehicleAssignment;
    }

    public void setVehicleAssignment(VehicleAssignment vehicleAssignment) {
        this.vehicleAssignment = vehicleAssignment;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public VehicleIdentification getIdentification() {
        return identification;
    }

    public void setIdentification(VehicleIdentification identification) {
        this.identification = identification;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
