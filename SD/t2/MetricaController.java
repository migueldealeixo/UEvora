package com.example.trabalho_t2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.trabalho_t2.entitys.Metricas;
import com.example.trabalho_t2.service.MetricaService;


@RestController
@RequestMapping("/metricas")
public class MetricaController {
    
    @Autowired
    private MetricaService metricaService;
    
    @PostMapping
    public ResponseEntity<String> addMetricas(@RequestBody Metricas metricas){
        boolean sucess = metricaService.processMetricas(metricas);
        if(sucess){
            return ResponseEntity.ok("Metricas adicionadas com sucesso!");
        }else{
            return ResponseEntity.badRequest().body("Erro ao adicionar metricas!");
        }
    }

    @GetMapping("quarto/{quarto}")
    public List<Metricas> getMetricasByRoom(@PathVariable("quarto") String quarto){
        return metricaService.getMetricsByRoom(quarto);
    }
    @GetMapping("/media/temperatura/{quarto}")
    public double getMediaTemperaturaPorQuarto(@PathVariable String quarto) {
        return metricaService.mediaPorQuarto(quarto);
    }
    @GetMapping("/media/humidade/{quarto}")
    public double getMediaHumidadePorQuarto(@PathVariable String quarto) {
        return metricaService.calcularMediaHumidadePorQuarto(quarto);
    }



    // Endpoint para consultar métricas por piso
    @GetMapping("/piso/{piso}")
    public List<Metricas> getMetricsByFloor(@PathVariable String piso) {
        return metricaService.getMetricsByFloor(piso);
    }
    @GetMapping("/piso/{quarto}/media/temperatura/{piso}")
    public double getMediaTemperaturaPorPiso(@PathVariable String piso) {
        return metricaService.mediaPorPiso(piso);
    }
    @GetMapping("/piso/media/humidade/{piso}")
    public double getMediaHumidadePorPiso(@PathVariable String piso) {
        return metricaService.calcularMediaHumidadePorPiso(piso);
    }



    // Endpoint para consultar métricas por serviço
    @GetMapping("/servico/{servico}")
    public List<Metricas> getMetricsByService(@PathVariable String servico) {
        return metricaService.getMetricsByService(servico);
    }
    @GetMapping("/servico/media/temperatura/{servico}")
    public double getMediaTemperaturaPorServico(@PathVariable String servico) {
        return metricaService.mediaPorServico(servico);
    }
    @GetMapping("/servico/media/humidade/{servico}")
    public double getMediaHumidadePorServico(@PathVariable String servico) {
        return metricaService.calcularMediaHumidadePorServico(servico);
    }

    // Endpoint para consultar métricas por edifício
    @GetMapping("/edificio/{edificio}")
    public List<Metricas> getMetricsByBuilding(@PathVariable String edificio) {
        return metricaService.getMetricsByBuilding(edificio);
    }
    @GetMapping("/edificio/media/temperatura/{edificio}")
    public double mediaTemperaturaEdificio(@RequestParam String edificio) {
        return metricaService.mediaPorEdificio(edificio);
    }
    @GetMapping("/edificio/media/humidade/{edificio}")
    public double mediaHumidadeEdificio(@RequestParam String edificio) {
        return metricaService.mediaHumidadePorEdificio(edificio);
    }

}
