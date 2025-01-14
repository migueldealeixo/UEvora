package com.example.trabalho_t2.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.trabalho_t2.Repository.IotDeviceRepo;
import com.example.trabalho_t2.Repository.MetricaRepository;
import com.example.trabalho_t2.entitys.IoTDevice;
import com.example.trabalho_t2.entitys.Metricas;

import jakarta.transaction.Transactional;

@Service
public class MetricaService {

    @Autowired
    private MetricaRepository metricaRepo;

    @Autowired
    private IotDeviceRepo deviceRepo;



    public boolean deviceExists(int deviceID){
        return deviceRepo.findById(deviceID).isPresent();
    }

    // Validação e armazenamento de métricas
    @Transactional
    public boolean processMetricas(Metricas metrica) {
        if(metrica.getDevice()== null){
            System.out.println("Dispositivo não encontrado");
            return false;
        }
        if (!isValidMetric(metrica)) {
            System.err.println("Métrica inválida descartada: " + metrica);
            return false;
        }

        if (deviceRepo.existsById(metrica.getDevice().getId())) {
            metricaRepo.save(metrica);
            return true;
        }
        System.out.println("Dispositivo não encontrado: ID " + metrica.getDevice().getId());
        return false;
    }

    private boolean isValidMetric(Metricas metrica) {
        return metrica.getTemperatura() >= -50 && metrica.getTemperatura() <= 100 &&
               metrica.getHumidade() >= 0 && metrica.getHumidade() <= 100;
    }

       public void saveDeviceMetrics(int deviceID, double temperatura, double humidade) {
        // Verificar se o dispositivo existe
        IoTDevice device = deviceRepo.findById(deviceID)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado"));

        // Criar uma nova instância de Metricas
        Metricas metric = new Metricas();
        metric.setDevice(device);  // Associar o dispositivo à métrica
        metric.setTemperatura(temperatura);
        metric.setHumidade(humidade);
        metric.setTimestamp(LocalDateTime.now());

        // Salvar a métrica no banco de dados
        metricaRepo.save(metric);
    }

    public List<Metricas> getMetricsByRoom(String quarto) {
    return metricaRepo.findMetricasByQuarto(quarto);
    }

     // Consultar métricas por piso
     public List<Metricas> getMetricsByFloor(String piso) {
        return metricaRepo.findByPiso(piso);
    }

    // Consultar métricas por serviço
    public List<Metricas> getMetricsByService(String servico) {
        return metricaRepo.findByServico(servico);
    }

    // Consultar métrricas por edifício
    public List<Metricas> getMetricsByBuilding(String edificio) {
        return metricaRepo.findByEdificio(edificio);
    }

    public double mediaPorQuarto(String quarto){
        List<Metricas> metricas = getMetricsByRoom(quarto);
        if(metricas == null || metricas.isEmpty()){
            return 0;
        }
        double soma = 0;
        for(Metricas metrica : metricas){
            soma += metrica.getTemperatura();
        }
        return soma/metricas.size();
    }

    public double calcularMediaHumidadePorQuarto(String quarto) {
        List<Metricas> metricas = getMetricsByRoom(quarto);
        if (metricas == null || metricas.isEmpty()) {
            return 0; // Retorna 0 se não houver métricas
        }
    
        double somaHumidade = 0;
        for (Metricas metrica : metricas) {
            somaHumidade += metrica.getHumidade();
        }
    
        return somaHumidade / metricas.size(); // Média
    }
    
    public double mediaPorServico(String servico){
        List<Metricas> metricas = getMetricsByService(servico);
        if(metricas == null || metricas.isEmpty()){
            return 0;
        }
        double soma = 0;
        for(Metricas metrica : metricas){
            soma += metrica.getTemperatura();
        }
        return soma/metricas.size();
    }

    public double calcularMediaHumidadePorServico(String servico){
        List<Metricas> metricas = getMetricsByService(servico);
        if (metricas == null || metricas.isEmpty()) {
            return 0;
        }
        double somaHumidade = 0;
        for (Metricas metrica : metricas) {
            somaHumidade += metrica.getHumidade();
        }
        return somaHumidade / metricas.size();
    }

    public double mediaPorPiso(String piso){
        List<Metricas> metricas = getMetricsByFloor(piso);
        if(metricas == null || metricas.isEmpty()){
        return 0;
        }
        double soma = 0;
        for(Metricas metrica : metricas){
            soma += metrica.getTemperatura();
        }
        return soma/metricas.size();
    }

    public double calcularMediaHumidadePorPiso(String piso){
        List<Metricas> metricas = getMetricsByFloor(piso);
        if (metricas == null || metricas.isEmpty()) {
            return 0;
        }   
        double somaHumidade = 0;
        for (Metricas metrica : metricas) {
            somaHumidade += metrica.getHumidade();
        }
        return somaHumidade / metricas.size();
    }
    
    public double mediaPorEdificio(String edificio) {
        List<Metricas> metricas = metricaRepo.findByEdificio(edificio);
        
        if (metricas.isEmpty()) {
            return 0; 
        }
        
        double somaTemperatura = 0;
        for (Metricas metrica : metricas) {
            somaTemperatura += metrica.getTemperatura();
        }
        
        return somaTemperatura / metricas.size();
    }

    public double mediaHumidadePorEdificio(String edificio) {
        List<Metricas> metricas = metricaRepo.findByEdificio(edificio);
        
        if (metricas.isEmpty()) {
            return 0; 
        }
        
        double somaHumidade = 0;
        for (Metricas metrica : metricas) {
            somaHumidade += metrica.getHumidade();
        }
        
        return somaHumidade / metricas.size();
    }


  
   
   









}
