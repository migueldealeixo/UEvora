package com.example.trabalho_t2.simulator;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.trabalho_t2.Repository.IotDeviceRepo;
import com.example.trabalho_t2.entitys.IoTDevice;

import jakarta.annotation.PostConstruct;

@Component
public class Sim {
    private static final String BROKER_URL = "tcp://localhost:1883"; 
    private static final String TOPIC = "hospital/iot";
    private static final String CLIENT_ID = "IoTDeviceSim";
    
    private static final int DEVICE_INTERVAL = 5000; 
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private IotDeviceRepo deviceRepo;



    @PostConstruct
    public void startSimulatingAllDevices() {
        List<IoTDevice> devices = deviceRepo.findAll();  
        for (IoTDevice device : devices) {
          executorService.submit(()->startSimulatingDeviceMetrics(device.getDeviceID()));
        }
    }
    
    public void startSimulatingDeviceMetrics(int deviceID) {

        new Thread(() -> {
            MqttClient client = null;
            try {
                client = new MqttClient(BROKER_URL, CLIENT_ID + deviceID);
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(false);
                client.connect(options);
    
                Random random = new Random();
                while (true) {
                 
                    if (!client.isConnected()) {
                        //System.out.println("O cliente MQTT não está mais conectado. Tentando reconectar...");
                        client.connect(options); 
                    }
    
                    String message = IoTDataSim(deviceID);
                    MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                    mqttMessage.setQos(2);
                    client.publish(TOPIC, mqttMessage); 
    
                    try {
                        Thread.sleep(DEVICE_INTERVAL);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Simulação interrompida.");
                        break;
                    }
                }
            } catch (MqttException e) {
               // e.printStackTrace();
            } finally {
                if (client != null && client.isConnected()) {
                    try {
                        client.disconnect();
                        //System.out.println("Cliente desconectado do broker.");
                    } catch (MqttException e) {
                        //e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private static String IoTDataSim(int deviceID) {
        Random random = new Random();
        double temp = 20 + (random.nextDouble() * 10);  // Temperatura entre 20°C e 30°C
        double hum = 40 + (random.nextDouble() * 60);   // Umidade entre 40% e 100%
        long timestamp = System.currentTimeMillis();  // Timestamp atual

        return String.format("{\"deviceId\": %d, \"temperature\": %.2f, \"humidity\": %.2f, \"timestamp\": %d}",
                deviceID, temp, hum, timestamp);
                
    }
}