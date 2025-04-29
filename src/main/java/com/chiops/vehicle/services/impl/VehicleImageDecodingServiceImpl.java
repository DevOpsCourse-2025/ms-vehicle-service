package com.chiops.vehicle.services.impl;

import com.chiops.vehicle.libs.dtos.VehicleWithImageDTO;
import com.chiops.vehicle.libs.dtos.VehicleDTO;
import com.chiops.vehicle.libs.exceptions.exception.InternalServerException;
import com.chiops.vehicle.services.VehicleImageDecodingService;
import com.chiops.vehicle.services.VehicleService;
import jakarta.inject.Singleton;

import java.util.Base64;

@Singleton
public class VehicleImageDecodingServiceImpl implements VehicleImageDecodingService {

    private final VehicleService vehicleService;

    public VehicleImageDecodingServiceImpl(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Override
    public VehicleDTO createVehicleFromEncodedImage(VehicleWithImageDTO dto) {
        try {
            VehicleDTO vehicleDto = dto.getVehicle();
            byte[] imageBytes = Base64.getDecoder().decode(dto.getImageBase64());
            return vehicleService.createVehicle(vehicleDto, imageBytes);
        } catch (Exception e) {
            throw new InternalServerException("Error al decodificar la imagen: " + e.getMessage());
        }
    }
}
