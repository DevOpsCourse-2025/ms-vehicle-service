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
import io.micronaut.validation.Validated;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import java.util.Optional;
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
        if (model== null || model.isBlank()) {
            throw new BadRequestException("Model as parameter is obligatory");
        }
        Model modelEntity = modelRepository.findByName(model)
            .orElseThrow(() ->
                new NotFoundException("Model with name " + model + " not found")
            );
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
        return vehicles.stream()
                .map(this::toDTO)
                .toList();
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDto, byte[] imageBytes) {
        if (vehicleRepository.findByVin(vehicleDto.getVin()).isPresent()) {
            throw new ConflictException("Vehicle with VIN " + vehicleDto.getVin() + " already exists");
        }
    
        Brand brand = brandRepository
        .findByName(vehicleDto.getBrand())
        .orElseGet(() ->
            brandRepository.save(new Brand(vehicleDto.getBrand()))
        );
    
        Model model = modelRepository
            .findByName(vehicleDto.getModel())
            .orElseGet(() -> {
                Model m = new Model(vehicleDto.getModel(), brand);
                return modelRepository.save(m);
        });
        Vehicle vehicle = toEntity(vehicleDto);
        vehicle.setVin(vehicleDto.getVin());
        vehicle.setModel(model);
        vehicle.getModel().setBrand(brand);
        vehicle.setRegistrationDate(vehicleDto.getRegistrationDate());
        String photoUrl = imageStoreService.upload(
            vehicleDto.getVin(),
            imageBytes,
            vehicleDto.getVin() + ".jpg"
        );
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

    @Override
    public VehicleDTO updateVehicle(VehicleDTO vehicleDto) {
        Vehicle vehicle = vehicleRepository.findByVin(vehicleDto.getVin())
                .orElseThrow(() -> new BadRequestException("Vin cannot be changed, be sure to write de Vin correctly\""));
        
        if (!vehicle.getVin().equals(vehicleDto.getVin())) {
            throw new BadRequestException("Vin cannot be changed, be sure to write the Vin correctly of the vehicle you want to update");
        }

        Brand brand = brandRepository.findByName(vehicleDto.getBrand())
            .orElseGet(() -> brandRepository.save(new Brand(vehicleDto.getBrand())));

        Model model = modelRepository.findByName(vehicleDto.getModel())
            .orElseGet(() -> {
                Model m = new Model(vehicleDto.getModel(), brand);
                return modelRepository.save(m);
            });

        vehicle.setModel(model);

        vehicle.setRegistrationDate(vehicleDto.getRegistrationDate());
        vehicle.getIdentification().setPlate(vehicleDto.getPlate());
        vehicle.getIdentification().setPurchasedDate(vehicleDto.getPurchaseDate());
        vehicle.getIdentification().setPrice(vehicleDto.getCost());

        return toDTO(vehicleRepository.update(vehicle));
    }

    @Override
    public VehicleDTO deleteVehicle(String vin) {
        Optional <Vehicle> vehicleOpt = vehicleRepository.findByVin(vin);
        if (vehicleOpt.isEmpty()) {
            throw new BadRequestException("The VIN is incorrect.");
        }
        Vehicle vehicle = vehicleOpt.get();
        if (vehicle.getVehicleAssignment() != null && "assigned".equals(vehicle.getVehicleAssignment().getStatus())) {
            throw new ConflictException("Cannot delete vehicle with VIN " + vin + " because it is currently assigned");
        }
        vehicleRepository.delete(vehicle);
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