package com.example.trabalho_t2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trabalho_t2.entitys.IoTDevice;
import com.example.trabalho_t2.service.IoTDeviceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/devices")
public class IoTDeviceController {

    @Autowired
    private IoTDeviceService deviceService;

    
    @PostMapping
    public ResponseEntity<?> registerDevice(@Valid @RequestBody IoTDevice device) {
        try {
            IoTDevice savedDevice = deviceService.registerDevice(device);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDevice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<IoTDevice>> getAllDevices() {
        List<IoTDevice> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<IoTDevice> updateDevice(@Valid @PathVariable int id, @Valid @RequestBody IoTDevice device) {
    device.setID(id);
    IoTDevice updatedDevice = deviceService.updateDevice(device);
    if (updatedDevice != null) {
        return ResponseEntity.ok(updatedDevice);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable int id) {
        boolean isDeleted = deviceService.deleteDevice(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

