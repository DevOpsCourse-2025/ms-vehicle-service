package com.chiops.vehicle.repositories;

import com.chiops.vehicle.entities.Vehicle;
import com.chiops.vehicle.entities.VehicleAssignment;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleAssignmentRepository extends CrudRepository<VehicleAssignment, Long> {
    List<VehicleAssignment> findByStatus(String status);
    Optional<VehicleAssignment> findByDriverCurpAndStatus(String driverCurp, String status);
}
