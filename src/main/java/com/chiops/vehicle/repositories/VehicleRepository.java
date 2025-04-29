package com.chiops.vehicle.repositories;

import com.chiops.vehicle.entities.Model;
import com.chiops.vehicle.entities.Vehicle;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


@Repository
public interface VehicleRepository extends CrudRepository<Vehicle,String> {
    List<Vehicle> findByModel(Model model);
    Optional<Vehicle> findByVin(String vin);
}
