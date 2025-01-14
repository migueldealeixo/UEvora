package com.example.trabalho_t2.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.trabalho_t2.entitys.Metricas;

public interface MetricaRepository extends JpaRepository<Metricas, Integer> {
 
  @Query("SELECT m FROM Metricas m WHERE m.device.quarto = :quarto")
  List<Metricas> findMetricasByQuarto(@Param("quarto") String quarto);

   // Consultar métricas por piso
   @Query("SELECT m FROM Metricas m WHERE m.device.piso = :piso")
   List<Metricas> findByPiso(String piso);
   
   // Consultar métricas por serviço
   @Query("SELECT m FROM Metricas m WHERE m.device.servico = :servico")
   List<Metricas> findByServico(String servico);
   
   // Consultar métricas por edifício
   @Query("SELECT m FROM Metricas m WHERE m.device.edificio = :edificio")
   List<Metricas> findByEdificio(String edificio);









}



