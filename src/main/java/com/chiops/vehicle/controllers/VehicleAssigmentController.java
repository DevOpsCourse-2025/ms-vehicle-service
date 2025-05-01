package com.chiops.vehicle.controllers;

import com.chiops.vehicle.libs.dtos.VehicleAssignmentDTO;
import com.chiops.vehicle.libs.exceptions.entities.ErrorResponse;
import com.chiops.vehicle.libs.exceptions.exception.BadRequestException;
import com.chiops.vehicle.libs.exceptions.exception.InternalServerException;
import com.chiops.vehicle.libs.exceptions.exception.MethodNotAllowedException;
import com.chiops.vehicle.libs.exceptions.exception.NotFoundException;
import com.chiops.vehicle.services.VehicleAssignmentService;

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

@Controller("/vehicle/assignment")
@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
public class VehicleAssigmentController {

    private final VehicleAssignmentService vehicleAssignmentService;

    public VehicleAssigmentController(VehicleAssignmentService vehicleAssignmentService) {
        this.vehicleAssignmentService = vehicleAssignmentService;
    }

    @Get(value = "/status/{status}", produces = MediaType.APPLICATION_JSON)
    public List<VehicleAssignmentDTO> findByStatus(@PathVariable String status) {
        try {
            return vehicleAssignmentService.findByStatus(status);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to find assignments by status: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to find assignments by status: " + e.getMessage());
        }
    }

    @Get(value = "/history", produces = MediaType.APPLICATION_JSON)
    public List<VehicleAssignmentDTO> assignmentsHistory() {
        try {
            return vehicleAssignmentService.assignmentsHistory();
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to get assignment history: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to get assignment history: " + e.getMessage());
        }
    }

    @Get(value = "/vin/{vin}", produces = MediaType.APPLICATION_JSON)
    public VehicleAssignmentDTO findByVin(@PathVariable String vin) {
        try {
            return vehicleAssignmentService.findByVin(vin);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to find assignment by VIN: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to find assignment by VIN: " + e.getMessage());
        }
    }

    @Post(value = "/assign", produces = MediaType.APPLICATION_JSON)
    public VehicleAssignmentDTO assignVehicleToDriver(@Body VehicleAssignmentDTO vehicleAssignmentDto) {
        try {
            return vehicleAssignmentService.assignVehicleToDriver(vehicleAssignmentDto);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to assign vehicle to driver: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to assign vehicle to driver: " + e.getMessage());
        }
    }

    @Post(value = "/release", produces = MediaType.APPLICATION_JSON)
    public VehicleAssignmentDTO releaseVehicleFromDriver(@Body VehicleAssignmentDTO vehicleAssignmentDto) {
        try {
            return vehicleAssignmentService.releaseVehicleFromDriver(vehicleAssignmentDto);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to release vehicle from driver: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to release vehicle from driver: " + e.getMessage());
        }
    }

    @Put(value = "/change", produces = MediaType.APPLICATION_JSON)
    public VehicleAssignmentDTO changeDriver(@Body VehicleAssignmentDTO vehicleAssignmentDto) {
        try {
            return vehicleAssignmentService.changeDriver(vehicleAssignmentDto);
        } catch (BadRequestException e) {
            throw new BadRequestException("Bad request while trying to change driver: " + e.getMessage());
        } catch (InternalServerException e) {
            throw new InternalServerException("Internal server error while trying to change driver: " + e.getMessage());
        }
    }
}
