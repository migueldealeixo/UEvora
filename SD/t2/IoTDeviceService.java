package com.example.trabalho_t2.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.trabalho_t2.Repository.IotDeviceRepo;
import com.example.trabalho_t2.entitys.IoTDevice;
import com.example.trabalho_t2.simulator.Sim;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class IoTDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(IoTDeviceService.class);

    @Autowired
    private IotDeviceRepo deviceRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Sim sim;


    public boolean isDevice(int deviceID){
        return deviceRepo.existsById(deviceID);
    }

    @Transactional
    public IoTDevice registerDevice(IoTDevice device) {
        logger.info("Attempting to register device with ID: {}", device.getDeviceID());
        if (deviceRepo.findByDeviceID(device.getDeviceID()).isPresent()) {
            logger.warn("Device with ID {} already exists", device.getDeviceID());
            throw new IllegalArgumentException("Dispositivo com o ID já registrado.");
        }
        IoTDevice savedDevice = deviceRepo.save(device);
        deviceRepo.flush();
        entityManager.flush();
        logger.info("Device registered successfully: {}", savedDevice);
        sim.startSimulatingDeviceMetrics(device.getDeviceID());
        return savedDevice;
    }

    public List<IoTDevice> getAllDevices() {
        logger.info("Fetching all devices");
        return deviceRepo.findAll();
    }

    public IoTDevice updateDevice(IoTDevice device) {
        logger.info("Attempting to update device with ID: {}", device.getDeviceID());
        Optional<IoTDevice> existingDevice = deviceRepo.findByDeviceID(device.getDeviceID());
        if (existingDevice.isPresent()) {
            IoTDevice updatedDevice = deviceRepo.save(device);
            logger.info("Device updated successfully: {}", updatedDevice);
            return updatedDevice;
        }
        logger.warn("Device with ID {} not found for update", device.getDeviceID());
        return null;
    }

    public boolean deleteDevice(int deviceID) {
        logger.info("Attempting to delete device with ID: {}", deviceID);
        Optional<IoTDevice> device = deviceRepo.findByDeviceID(deviceID);
        if (device.isPresent()) {
            deviceRepo.delete(device.get());
            logger.info("Device with ID {} deleted successfully", deviceID);
            return true;
        }
        logger.warn("Device with ID {} not found for deletion", deviceID);
        return false;
    }
}
