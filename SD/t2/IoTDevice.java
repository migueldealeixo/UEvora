package com.example.trabalho_t2.entitys;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class IoTDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique=true)
    @NotNull(message="O device ID não pode ser nulo!")
    @Min(value = 1, message="O device ID deve ser maior que 0")
    private int deviceID;
    @NotBlank(message="O campo quarto é obrigatorio!")
    private String quarto;
    @NotBlank(message = "O campo serviço é obrigatorio!")
    private String servico;
    @NotBlank(message = "O campo piso é obrigatorio!")
    private String piso;
    @NotBlank(message="O campo edificio é obrigatorio!")
    private String edificio;
 
    @OneToMany(mappedBy="device", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Metricas> metricas;


  
    public int getId(){
        return this.id;
    }
    public int getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }
    public String getQuarto() {
        return quarto;
    }
    public void setQuarto(String quarto) {
        this.quarto = quarto;
    }
    public String getServico() {
        return servico;
    }
    public void setServiço(String servico) {
        this.servico = servico;
    }
    public String getPiso() {
        return piso;
    }
    public void setPiso(String piso) {
        this.piso = piso;
    }
    public String getEdificio() {
        return edificio;
    }
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }
    public void setID(int id){
        this.id = id;
    }

    @Override
    public String toString() {
    return "IoTDevice{" +
            "deviceId= '" + deviceID + '\'' +
            ", edificio= '" + edificio + '\'' +
            ", piso=" + piso +
            ", quarto= " + quarto +
            ", servico= '" + servico + '\'' +
            '}';
}

 
    }

