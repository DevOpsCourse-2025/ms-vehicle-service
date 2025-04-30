package com.chiops.vehicle.controllers;

import com.chiops.vehicle.libs.dtos.VehicleAssignmentDTO;
import com.chiops.vehicle.libs.dtos.VehicleDTO;
import com.chiops.vehicle.libs.dtos.VehicleWithImageDTO;
import com.chiops.vehicle.libs.exceptions.entities.ErrorResponse;
import com.chiops.vehicle.libs.exceptions.exception.BadRequestException;
import com.chiops.vehicle.libs.exceptions.exception.InternalServerException;
import com.chiops.vehicle.libs.exceptions.exception.MethodNotAllowedException;
import com.chiops.vehicle.libs.exceptions.exception.NotFoundException;
import com.chiops.vehicle.services.ImageStoreService;
import com.chiops.vehicle.services.VehicleImageDecodingService;
import com.chiops.vehicle.services.VehicleService;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;
@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/vehicle")
@Secured(SecurityRule.IS_ANONYMOUS)
public class VehicleController {

    private final VehicleService vehicleService;
    private final ImageStoreService imageStoreService;
    private final VehicleImageDecodingService decodingService;

    public VehicleController(VehicleService vehicleService,
                              ImageStoreService imageStoreService, 
                              VehicleImageDecodingService decodingService) {
        this.vehicleService = vehicleService;
        this.imageStoreService = imageStoreService;
        this.decodingService = decodingService;
    }

    @Post(value = "/create", consumes = MediaType.APPLICATION_JSON)
    public VehicleDTO createVehicle(@Body VehicleWithImageDTO vehicleWithImageDTO) {
        try {
        return decodingService.createVehicleFromEncodedImage(vehicleWithImageDTO);
    } catch (BadRequestException e) {
        throw new BadRequestException("Bad request while trying to create the vehicle: " + e.getMessage());
    } catch (InternalServerException e) {
        throw new InternalServerException("Internal server error while trying to create the vehicle: " + e.getMessage());
    }
}

    @Put(value = "/update", consumes = MediaType.APPLICATION_JSON)
    public VehicleDTO updateVehicle(@Body VehicleDTO vehicle) {
        try {
            return vehicleService.updateVehicle(vehicle);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to update the vehicle: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to update the vehicle: " + e.getMessage());
        }
    }

    @Delete("/delete/{vin}")
    public void deleteVehicle(@PathVariable String vin) {
        try {
            vehicleService.deleteVehicle(vin);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to delete the vehicle: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to delete the vehicle: " + e.getMessage());
        }
    }

    @Get(value = "/get/{vin}", consumes = MediaType.APPLICATION_JSON)
    public VehicleDTO getVehicleByVin(@PathVariable String vin) {
        try {
            return vehicleService.getVehicleByVin(vin);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to get the vehicle by VIN: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to get the vehicle by VIN: " + e.getMessage());
        }
    }

    @Get(value = "/model/{model}", consumes = MediaType.APPLICATION_JSON)
    public List<VehicleDTO> getAllVehiclesByModel(@QueryValue String model) {
        try {
            return vehicleService.getVehiclesByModelName(model);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to get vehicles by model: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to get vehicles by model: " + e.getMessage());
        }
    }

    @Get(value = "/getall", consumes = MediaType.APPLICATION_JSON)
    public List<VehicleDTO> getAllVehicles() {
        try {
            return vehicleService.getAllVehicles();
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to get all vehicles: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to get all vehicles: " + e.getMessage());
        }
    }

    @Get("/view/{filename}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<?> viewImage(@PathVariable String filename) {
        try {
            return imageStoreService.view(filename);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to view the vehicle image: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to view the vehicle image: " + e.getMessage());
        }
    }

    @Error(status = HttpStatus.NOT_FOUND, global = true)
    public HttpResponse<ErrorResponse> handleNotFound(HttpRequest<?> request) {
        throw new NotFoundException("Endpoint " + request.getPath() + " not found");
    }

    @Error(status = HttpStatus.METHOD_NOT_ALLOWED, global = true)
    public HttpResponse<ErrorResponse> handleMethodNotAllowed(HttpRequest<?> request) {
        throw new MethodNotAllowedException("Method " + request.getMethod() + " is not allowed for " + request.getPath());
    }
}
