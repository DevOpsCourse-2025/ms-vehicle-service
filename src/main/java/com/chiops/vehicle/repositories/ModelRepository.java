package com.chiops.vehicle.repositories;

import java.util.Optional;

import com.chiops.vehicle.entities.Model;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ModelRepository extends CrudRepository<Model, Long> {
    Model saveByName(String name);   
    Optional<Model> findByName(String name);
}
