package com.chiops.vehicle.services.impl;

import com.chiops.vehicle.entities.Brand;
import com.chiops.vehicle.entities.Model;
import com.chiops.vehicle.entities.Vehicle;
import com.chiops.vehicle.entities.VehicleIdentification;
import com.chiops.vehicle.libs.exceptions.exception.*;
import com.chiops.vehicle.libs.dtos.VehicleDTO;
import com.chiops.vehicle.repositories.*;
import com.chiops.vehicle.services.VehicleService;
import io.micronaut.http.multipart.CompletedFileUpload;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ImageStoreServiceImpl imageStoreService;
    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository,
                            ImageStoreServiceImpl imageStoreService,
                            ModelRepository modelRepository,
                            BrandRepository brandRepository) {
        this.vehicleRepository = vehicleRepository;
        this.imageStoreService = imageStoreService;
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public VehicleDTO getVehicleByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findByVin(vin)
                .orElseThrow(() -> new NotFoundException("Vehicle with VIN " + vin + " not found"));
        return toDTO(vehicle);
    }

    @Override
    public List<VehicleDTO> getVehiclesByModelName(String model) {
        Model modelEntity = modelRepository.findByName(model);
        if (modelEntity == null) {
            throw new NotFoundException("Model " + model + " not found");
        }
        
        List<Vehicle> vehicles = vehicleRepository.findByModel(modelEntity);
        if (vehicles.isEmpty()) {
            throw new NotFoundException("No vehicles found for model " + model);
        }
        
        return vehicles.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            throw new NotFoundException("No vehicles found in the system");
        }
        return vehicles.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public VehicleDTO createVehicle(VehicleDTO vehicleDto, CompletedFileUpload imageFile) {
        
        if (vehicleRepository.findByVin(vehicleDto.getVin()).isPresent()) {
            throw new ConflictException("Vehicle with VIN " + vehicleDto.getVin() + " already exists");
        }

        Brand brand = brandRepository.findByName(vehicleDto.getBrand());
        if (brand == null) {
            brand = brandRepository.save(new Brand(vehicleDto.getBrand()));
        }

        Model model = modelRepository.findByName(vehicleDto.getModel());
        if (model == null) {
            model = modelRepository.save(new Model(vehicleDto.getModel(), brand));
        }
        
        Vehicle vehicle = toEntity(vehicleDto);
        vehicle.setVin(vehicleDto.getVin());
        vehicle.setModel(model);
        vehicle.getModel().setBrand(brand);
        vehicle.setRegistrationDate(vehicleDto.getRegistrationDate());

        String photoUrl = imageStoreService.upload(vehicleDto.getVin(), imageFile);
        if (photoUrl == null) {
            throw new BadRequestException("Failed to upload vehicle image");
        }

        VehicleIdentification vehicleIdentification = new VehicleIdentification(
                vehicle,
                vehicleDto.getPlate(),
                vehicleDto.getPurchaseDate(),
                photoUrl,
                vehicleDto.getCost()
        );

        vehicle.setIdentification(vehicleIdentification);
        vehicleRepository.save(vehicle);
        return toDTO(vehicle);
    }
    
    private VehicleDTO toDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVin(vehicle.getVin());
        dto.setModel(vehicle.getModel().getName());
        dto.setBrand(vehicle.getModel().getBrand().getName());
        dto.setPlate(vehicle.getIdentification().getPlate());
        dto.setPhotoUrl(vehicle.getIdentification().getPhotoUrl());
        dto.setPurchaseDate(vehicle.getIdentification().getPurchasedDate());
        dto.setCost(vehicle.getIdentification().getPrice());
        dto.setRegistrationDate(vehicle.getRegistrationDate());
        dto.setAssigmentStatus(vehicle.getVehicleAssignment() == null ?  "unassigned" : vehicle.getVehicleAssignment().getStatus());
        dto.setAssignmentId(vehicle.getVehicleAssignment() != null ? vehicle.getVehicleAssignment().getId() : null);
        return dto;
    }

    private Vehicle toEntity(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vehicleDTO.getVin());
        vehicle.setModel(new Model(vehicleDTO.getModel(), new Brand(vehicleDTO.getBrand())));
        VehicleIdentification identification = new VehicleIdentification(
                vehicle,
                vehicleDTO.getPlate(),
                vehicleDTO.getPurchaseDate(),
                vehicleDTO.getPhotoUrl(),
                vehicleDTO.getCost()
        );
        vehicle.setIdentification(identification);
        vehicle.setRegistrationDate(vehicleDTO.getRegistrationDate());
        return vehicle;
    }
}