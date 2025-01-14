package com.example.trabalho_t2.entitys;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Metricas {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="device_id",nullable = false)
    private IoTDevice device;

    private double temperatura;
    private double humidade;
    private LocalDateTime timestamp;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public IoTDevice getDevice() {
        return device;
    }
    public void setDevice(IoTDevice device) {
        this.device = device;
    }
    public double getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }
    public double getHumidade() {
        return humidade;
    }
    public void setHumidade(double humidade) {
        this.humidade = humidade;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
 
    


}
