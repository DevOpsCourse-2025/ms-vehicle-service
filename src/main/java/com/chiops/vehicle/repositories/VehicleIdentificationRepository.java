package com.chiops.vehicle.repositories;

import com.chiops.vehicle.entities.VehicleIdentification;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface VehicleIdentificationRepository extends CrudRepository<VehicleIdentification, Long> {
}

