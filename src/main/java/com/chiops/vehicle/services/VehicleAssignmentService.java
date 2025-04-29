package com.chiops.vehicle.services;

import com.chiops.vehicle.entities.VehicleAssignment;
import com.chiops.vehicle.libs.dtos.VehicleAssignmentDTO;
import com.chiops.vehicle.libs.dtos.VehicleDTO;
import com.chiops.vehicle.repositories.VehicleAssignmentRepository;
import com.chiops.vehicle.repositories.VehicleRepository;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public interface VehicleAssignmentService {

    List<VehicleAssignmentDTO> findByStatus(String status);

    List<VehicleAssignmentDTO> assignmentsHistory();

    VehicleAssignmentDTO findByVin(String vin);

    VehicleAssignmentDTO assignVehicleToDriver(VehicleAssignmentDTO vehicleAssignmentDto);

    VehicleAssignmentDTO releaseVehicleFromDriver(VehicleAssignmentDTO vehicleAssignmentDto);

    VehicleAssignmentDTO changeDriver(VehicleAssignmentDTO vehicleAssignmentDto);
}
