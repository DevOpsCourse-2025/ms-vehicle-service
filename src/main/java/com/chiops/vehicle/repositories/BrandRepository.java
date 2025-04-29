package com.chiops.vehicle.repositories;

import com.chiops.vehicle.entities.Brand;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface BrandRepository extends CrudRepository<Brand, Long> {
    Brand saveByName(String name);
    Brand findByName(String name);
}
