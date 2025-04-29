package com.chiops.vehicle.libs.dtos;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Introspected
@Serdeable
public class VehicleAssignmentDTO {

    @NotBlank
    private String driverCurp;

    private LocalDateTime assignedAt;

    private LocalDateTime releasedAt;

    private String status;

    @NotBlank
    private String vin;

    private String changedDriverCurp;

    public VehicleAssignmentDTO() {
    }

    public VehicleAssignmentDTO(String driverCurp, LocalDateTime assignedAt, LocalDateTime releasedAt, String status, String vin) {
        this.driverCurp = driverCurp;
        this.assignedAt = assignedAt;
        this.releasedAt = releasedAt;
        this.status = status;
        this.vin = vin;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
