package com.chiops.vehicle.libs.clients;


import com.chiops.vehicle.libs.dtos.DriverDTO;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Client("${services.driver.url}/driver")
public interface DriverClient {

    @Post("/create")
    public DriverDTO createDriver(@Body DriverDTO driverDTO);

    @Put("/update")
    public DriverDTO updateDriver(@Body DriverDTO driverDTO);

    @Delete("/delete/{curp}")
    public void deleteDriver(@PathVariable String curp);

    @Get("/get/{curp}")
    public Optional<DriverDTO> getDriverByCurp(@PathVariable String curp);

    @Get("/getall")
    public List<DriverDTO> getAllDrivers();
}
