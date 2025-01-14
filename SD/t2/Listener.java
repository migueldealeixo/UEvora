package com.example.trabalho_t2.simulator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.trabalho_t2.Repository.IotDeviceRepo;
import com.example.trabalho_t2.entitys.IoTDevice;
import com.example.trabalho_t2.entitys.Metricas;
import com.example.trabalho_t2.service.MetricaService;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Component
public class Listener {
    
    private static final String BROKER_URL= "tcp://localhost:1883";
    private static final String TOPIC = "hospital/iot";
    private static final String CLIENT_ID = "IoTListener";

    @Autowired
    private MetricaService metricaservo;

    @Autowired
    private IotDeviceRepo deviceRepo;

    private MqttClient client;

    @PostConstruct
    public void start(){
        try{
            client = new MqttClient(BROKER_URL, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setAutomaticReconnect(true);
            client.connect(options);

            if(client.isConnected()){
                client.subscribe(TOPIC,(topic,message)->{
                    processMessage(new String(message.getPayload()));
                });
                //System.out.print("Listener MQTT conectado e ouvindo no topico: "+ TOPIC);
            }else{
                //System.out.print("Falha ao conectar ao broker MQTT");
            }
            
        }catch(MqttException e){
            e.printStackTrace();
        }
    }

    @Transactional
   private void processMessage(String payload) {
    try {
        // Parse do JSON
        int deviceId = Integer.parseInt(payload.split("\"deviceId\":")[1].split(",")[0].trim());
        double temperature = Double.parseDouble(payload.split("\"temperature\":")[1].split(",")[0].trim());
        double humidity = Double.parseDouble(payload.split("\"humidity\":")[1].split(",")[0].trim());
        long timestamp = Long.parseLong(payload.split("\"timestamp\":")[1].split("}")[0].trim());

        // Verifica se o dispositivo existe no banco de dados
        IoTDevice device = deviceRepo.findByDeviceID(deviceId).orElse(null); // Busca o dispositivo pelo ID
        if (device == null) {
      
            //System.out.println("Dispositivo não registrado: " + deviceId);
            return; 
        }

        Metricas metrica = new Metricas();
        metrica.setHumidade(humidity);
        metrica.setTemperatura(temperature);
        metrica.setTimestamp(LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC));
        metrica.setDevice(device);  

        if (metricaservo.processMetricas(metrica)) {
           // System.out.println("Métrica processada com sucesso: " + metrica);
        } else {
            System.out.println("Erro ao processar a métrica!");
        }
    } catch (Exception e) {
        System.err.println("Erro ao processar a mensagem: " + payload);
        e.printStackTrace();
    }
}


    public void stop(){
        try{
            if(client != null && client.isConnected()){
                client.disconnect();
                System.out.println("Listener MQTT desconectado");
            }else{
                System.out.println("Listener MQTT não conectado");
            }
        }catch(MqttException e){
            e.printStackTrace();
        }
    }
}