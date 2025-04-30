package com.chiops.vehicle.services;

import com.chiops.vehicle.libs.dtos.VehicleWithImageDTO;
import com.chiops.vehicle.libs.dtos.VehicleDTO;

public interface VehicleImageDecodingService {

    VehicleDTO createVehicleFromEncodedImage(VehicleWithImageDTO vehicleWithImageDTO);
}