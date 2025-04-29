package com.chiops.vehicle.services.impl;

import com.chiops.vehicle.entities.Vehicle;
import com.chiops.vehicle.entities.VehicleAssignment;
import com.chiops.vehicle.libs.clients.DriverClient;
import com.chiops.vehicle.libs.dtos.VehicleAssignmentDTO;
import com.chiops.vehicle.libs.exceptions.exception.*;
import com.chiops.vehicle.repositories.VehicleAssignmentRepository;
import com.chiops.vehicle.repositories.VehicleRepository;
import com.chiops.vehicle.services.VehicleAssignmentService;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;

@Singleton
public class VehicleAssignmentServiceImpl implements VehicleAssignmentService {

    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverClient driverClient;

    public VehicleAssignmentServiceImpl(VehicleAssignmentRepository vehicleAssignmentRepository,
                                      VehicleRepository vehicleRepository, DriverClient driverClient) {
        this.vehicleAssignmentRepository = vehicleAssignmentRepository;
        this.vehicleRepository = vehicleRepository;
        this.driverClient = driverClient;
    }

    @Override
    public List<VehicleAssignmentDTO> findByStatus(String status) {
        List<VehicleAssignment> vehicleAssignments = vehicleAssignmentRepository.findByStatus(status);
        if (vehicleAssignments.isEmpty()) {
            throw new NotFoundException("No vehicle assignments found with status: " + status);
        }
        return vehicleAssignments.stream()
                .map(this::vehicleAssignmentToDTO)
                .toList();
    }

    @Override
    public List<VehicleAssignmentDTO> assignmentsHistory() {
        List<VehicleAssignment> vehicleAssignments = vehicleAssignmentRepository.findAll();
        if (vehicleAssignments.isEmpty()) {
            throw new NotFoundException("No vehicle assignments found in the system");
        }
        return vehicleAssignments.stream()
                .map(this::vehicleAssignmentToDTO)
                .toList();
    }

    @Override
    public VehicleAssignmentDTO findByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findByVin(vin)
                .orElseThrow(() -> new NotFoundException("Vehicle with VIN " + vin + " not found"));
    
        if (vehicle.getVehicleAssignment() == null) {
            throw new ConflictException("Vehicle with VIN " + vin + " is not assigned to any driver");
        }
    
        return vehicleAssignmentToDTO(vehicle.getVehicleAssignment());
    }
    
    @Override
    public VehicleAssignmentDTO assignVehicleToDriver(VehicleAssignmentDTO vehicleAssignmentDto) {
        Vehicle vehicle = vehicleRepository.findByVin(vehicleAssignmentDto.getVin())
        .orElseThrow(() -> new NotFoundException("Vehicle with VIN " + vehicleAssignmentDto.getVin() + " not found"));

        
        if (vehicle.getVehicleAssignment() != null && "assigned".equals(vehicle.getVehicleAssignment().getStatus())) {
            throw new ConflictException("Vehicle with VIN " + vehicleAssignmentDto.getVin() + " is already assigned to a driver");
        }

        if (driverClient.getDriverByCurp(vehicleAssignmentDto.getDriverCurp()).isEmpty()) {
            throw new NotFoundException("Driver with CURP " + vehicleAssignmentDto.getDriverCurp() + " not found");
        }

        if (vehicleAssignmentRepository.findByDriverCurpAndStatus(
            vehicleAssignmentDto.getDriverCurp(), "assigned").isPresent()) {
            throw new ConflictException("Driver with CURP " + vehicleAssignmentDto.getDriverCurp() + " is already assigned to a vehicle");
        }

        VehicleAssignment vehicleAssignment = new VehicleAssignment();
        vehicleAssignment.setDriverCurp(vehicleAssignmentDto.getDriverCurp());
        vehicleAssignment.setAssignedAt(LocalDateTime.now());
        vehicleAssignment.setVehicle(vehicle);
        vehicleAssignment.setStatus("assigned");
        vehicleAssignment.setReleasedAt(null);

        vehicleAssignmentRepository.save(vehicleAssignment);
        vehicle.setVehicleAssignment(vehicleAssignment);
        vehicleRepository.update(vehicle);

        return vehicleToDTO(vehicle);
    }

    @Override
    public VehicleAssignmentDTO releaseVehicleFromDriver(VehicleAssignmentDTO vehicleAssignmentDto) {
        Vehicle vehicle = vehicleRepository.findByVin(vehicleAssignmentDto.getVin())
                .orElseThrow(() -> new NotFoundException("Vehicle with VIN " + vehicleAssignmentDto.getVin() + " not found"));
                
        if (vehicle.getVehicleAssignment() == null) {
            throw new ConflictException("Vehicle with VIN " + vehicleAssignmentDto.getVin() + " is not assigned to any driver");
        }

        if (driverClient.getDriverByCurp(vehicleAssignmentDto.getDriverCurp()).isEmpty()) {
            throw new NotFoundException("Driver with CURP " + vehicleAssignmentDto.getDriverCurp() + " not found");
        }

        if (!vehicle.getVehicleAssignment().getDriverCurp().equals(vehicleAssignmentDto.getDriverCurp())) {
            throw new BadRequestException("Driver CURP " + vehicleAssignmentDto.getDriverCurp() + " does not match the assigned driver");
        }

        VehicleAssignment vehicleAssignment = vehicle.getVehicleAssignment();
        vehicleAssignment.setReleasedAt(LocalDateTime.now());
        vehicleAssignment.setStatus("released");

        vehicleAssignmentRepository.update(vehicleAssignment);
        vehicle.setVehicleAssignment(null);
        vehicleRepository.update(vehicle);

        return vehicleAssignmentToDTO(vehicleAssignment);
    }

    @Override
    public VehicleAssignmentDTO changeDriver(VehicleAssignmentDTO vehicleAssignmentDto) {
        Vehicle vehicle = vehicleRepository.findByVin(vehicleAssignmentDto.getVin())
                .orElseThrow(() -> new NotFoundException("Vehicle with VIN " + vehicleAssignmentDto.getVin() + " not found"));

        if (vehicle.getVehicleAssignment() == null) {
            throw new ConflictException("Vehicle with VIN " + vehicleAssignmentDto.getVin() + " is not assigned to any driver");
        }

        if (driverClient.getDriverByCurp(vehicleAssignmentDto.getDriverCurp()).isEmpty()) {
            throw new NotFoundException("Driver with CURP " + vehicleAssignmentDto.getDriverCurp() + " not found");
        }
        
        if (driverClient.getDriverByCurp(vehicleAssignmentDto.getChangedDriverCurp()).isEmpty()) {
            throw new NotFoundException("Changed driver with CURP " + vehicleAssignmentDto.getChangedDriverCurp() + " not found");
        }

        VehicleAssignment oldVehicleAssignment = vehicleAssignmentRepository
                .findByDriverCurpAndStatus(vehicleAssignmentDto.getDriverCurp(), "assigned")
                .orElseThrow(() -> new NotFoundException("Driver with CURP " + vehicleAssignmentDto.getDriverCurp() + " is not assigned to any vehicle"));

        if (oldVehicleAssignment.getDriverCurp().equals(vehicleAssignmentDto.getChangedDriverCurp())
        || vehicleAssignmentRepository.findByDriverCurpAndStatus(vehicleAssignmentDto.getChangedDriverCurp(), "assigned").isPresent()) {
            throw new ConflictException("Driver with CURP " + vehicleAssignmentDto.getChangedDriverCurp() + " is already assigned to this vehicle");
        }

        oldVehicleAssignment.setReleasedAt(LocalDateTime.now());
        oldVehicleAssignment.setStatus("changed");
        oldVehicleAssignment.setChangedDriverCurp(vehicleAssignmentDto.getChangedDriverCurp());

        vehicleAssignmentRepository.update(oldVehicleAssignment);

        VehicleAssignment newVehicleAssignment = new VehicleAssignment();
        newVehicleAssignment.setAssignedAt(LocalDateTime.now());
        newVehicleAssignment.setDriverCurp(vehicleAssignmentDto.getChangedDriverCurp());
        newVehicleAssignment.setReleasedAt(null);
        newVehicleAssignment.setVehicle(vehicle);
        newVehicleAssignment.setStatus("assigned");

        vehicleAssignmentRepository.save(newVehicleAssignment);
        vehicle.setVehicleAssignment(newVehicleAssignment);
        vehicleRepository.update(vehicle);
        
        return vehicleToDTO(vehicle);
    }

    private VehicleAssignmentDTO vehicleToDTO(Vehicle vehicle) {
        VehicleAssignmentDTO dto = new VehicleAssignmentDTO();
        dto.setDriverCurp(vehicle.getVehicleAssignment().getDriverCurp());
        dto.setAssignedAt(vehicle.getVehicleAssignment().getAssignedAt());
        dto.setReleasedAt(vehicle.getVehicleAssignment().getReleasedAt());
        dto.setStatus(vehicle.getVehicleAssignment().getStatus());
        dto.setChangedDriverCurp(vehicle.getVehicleAssignment().getChangedDriverCurp());
        dto.setVin(vehicle.getVin());
        return dto;
    }

    private VehicleAssignmentDTO vehicleAssignmentToDTO(VehicleAssignment vehicleAssignment) {
        VehicleAssignmentDTO dto = new VehicleAssignmentDTO();
        dto.setDriverCurp(vehicleAssignment.getDriverCurp());
        dto.setAssignedAt(vehicleAssignment.getAssignedAt());
        dto.setReleasedAt(vehicleAssignment.getReleasedAt());
        dto.setStatus(vehicleAssignment.getStatus());
        dto.setChangedDriverCurp(vehicleAssignment.getChangedDriverCurp());
        return dto;
    }
}