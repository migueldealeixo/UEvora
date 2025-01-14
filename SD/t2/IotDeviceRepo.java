package com.example.trabalho_t2.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trabalho_t2.entitys.IoTDevice;
public interface IotDeviceRepo extends JpaRepository<IoTDevice, Integer> {
    List<IoTDevice> findAll();
    //Encontra um dispositivo pelo seu ID
    Optional<IoTDevice> findByDeviceID(int deviceID);
    boolean existsByDeviceID(int deviceID);
    //Procurar dispositivo por edificio/serviço/quarto
    List<IoTDevice> findByEdificio(String edificio);
    List<IoTDevice> findByServico(String servico);
    List<IoTDevice> findByQuarto(String quarto);
    long countByEdificio(String edificio);
    long countByPiso(String piso);
}
