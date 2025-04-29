package com.chiops.vehicle.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "vehicle_assignment")
public class VehicleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id", nullable = false)
    private String driverCurp;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "changed_driver_id")
    private String changedDriverCurp;


    public VehicleAssignment() {
    }

    public VehicleAssignment(String driverCurp, LocalDateTime assignedAt, LocalDateTime releasedAt) {
        this.assignedAt = assignedAt;
        this.driverCurp = driverCurp;
        this.releasedAt = releasedAt;
    }

    public Long getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getDriverCurp() {
        return driverCurp;
    }

    public void setDriverCurp(String driverCurp) {
        this.driverCurp = driverCurp;
    }

    public String getChangedDriverCurp() {
        return changedDriverCurp;
    }

    public void setChangedDriverCurp(String changedDriverCurp) {
        this.changedDriverCurp = changedDriverCurp;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(LocalDateTime releasedAt) {
        this.releasedAt = releasedAt;
    }
}
