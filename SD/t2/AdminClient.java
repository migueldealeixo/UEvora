package com.example.trabalho_t2.admin;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.trabalho_t2.entitys.IoTDevice;
import com.example.trabalho_t2.service.MetricaService;
import com.example.trabalho_t2.simulator.Sim;
@Component
public class AdminClient implements CommandLineRunner {

    @Autowired
    private MetricaService metricaServo;
    @Autowired 
    private Sim sim;

    private static final String BASE_URL = "http://localhost:8080/devices";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void run(String... args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Gestor de Dispositivos IoT\n");
            while (true) {
                System.out.print("Escolha uma das opções:\n");
                System.out.print("1 - Registrar dispositivos\n");
                System.out.print("2 - Listar dispositivos\n");
                System.out.print("3 - Atualizar dispositivo\n");
                System.out.print("4 - Remover dispositivo\n");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        registerDevice(scanner);
                        break;
                    case 2:
                        listDevices();
                        break;
                    case 3:
                        updateDevice(scanner);
                        break;
                    case 4:
                        deleteDevice(scanner);
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        }
    }

    private void registerDevice(Scanner scanner) {
    System.out.print("ID do dispositivo: ");
    int id = scanner.nextInt();
    scanner.nextLine();

    // Obter todos os dispositivos para verificar se o ID já existe
    IoTDevice[] devices = restTemplate.getForObject(BASE_URL, IoTDevice[].class);

    // Verificar se já existe um dispositivo com o ID fornecido
    for (IoTDevice device : devices) {
        if (device.getDeviceID() == id) {
            System.out.println("Dispositivo com ID " + id + " já existe!");
            return;
        }
    }

    // Solicitar as informações do novo dispositivo
    System.out.print("Indique o quarto onde o dispositivo se encontra: ");
    String room = scanner.nextLine().trim();

    if(room.isEmpty()){
        System.out.println("O quarto não pode estar vazio!");
    }
    for(IoTDevice device: devices){
        if(device.getQuarto().equals(room)){
            System.out.println("Quarto já ocupado!");
            return;
        }
    }
    System.out.print("Indique o serviço onde o dispositivo se encontra: ");
    String service = scanner.nextLine().trim();
    if(service.isEmpty()){
        System.out.println("O serviço não pode estar vazio!");
    }
    System.out.print("Indique o piso onde o dispositivo se encontra: ");
    String floor = scanner.nextLine().trim();
    if(floor.isEmpty()){
        System.out.println("O piso não pode estar vazio!");
    }
    System.out.print("Indique o edifício onde o dispositivo se encontra: ");
    String building = scanner.nextLine().trim();
    if(building.isEmpty()){
        System.out.println("O edifício não pode estar vazio!");
    }

    // Criar o novo dispositivo
    IoTDevice device = new IoTDevice();
    device.setDeviceID(id);
    device.setQuarto(room);
    device.setServiço(service);
    device.setPiso(floor);
    device.setEdificio(building);

    try {
        // Enviar o novo dispositivo para o servidor
        restTemplate.postForObject(BASE_URL, device, IoTDevice.class);
        System.out.println("Dispositivo registrado com sucesso!");

        // Iniciar a simulação de métricas
        sim.startSimulatingDeviceMetrics(id);
    } catch (Exception e) {
        //System.out.println("Erro ao registrar dispositivo: " + e.getMessage());
    }
}


    private void listDevices() {
        try {
            IoTDevice[] devices = restTemplate.getForObject(BASE_URL, IoTDevice[].class);
            if (devices != null) {
                for (IoTDevice device : devices) {
                    System.out.println("DeviceID: " + device.getDeviceID() +"\nQuarto: " + device.getQuarto());
                    System.out.println("XXXXXXXXXXX");
                }
            } else {
                System.out.println("Nenhum dispositivo encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar dispositivos: " + e.getMessage());
        }
    }
    private void updateDevice(Scanner scanner) {
        System.out.print("ID do dispositivo a ser atualizado: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        // Obter todos os dispositivos para verificar se o ID existe
        IoTDevice[] devices = restTemplate.getForObject(BASE_URL, IoTDevice[].class);
        
        // Verificar se existe um dispositivo com o ID fornecido
        IoTDevice existingDevice = null;
        for (IoTDevice device : devices) {
            if (device.getDeviceID() == id) {
                existingDevice = device;
                break;
            }
        }
        
        if (existingDevice == null) {
            System.out.println("Dispositivo com ID " + id + " não encontrado.");
            return;
        }
    
        // Validando e atualizando os campos
        System.out.print("Novo quarto do dispositivo: ");
        String newRoom = scanner.nextLine().trim();
        if (newRoom.isEmpty()) {
            System.out.println("O quarto não pode ser vazio.");
            return;
        }
    
        for (IoTDevice device : devices) {
            if (device.getQuarto().equals(newRoom)) {
                System.out.println("O quarto está ocupado.");
                return;
            }
        }
    
        System.out.print("Novo serviço do dispositivo: ");
        String newService = scanner.nextLine().trim();
        if (newService.isEmpty()) {
            System.out.println("O serviço não pode ser vazio.");
            return;
        }
    
        System.out.print("Novo piso do dispositivo: ");
        String newFloor = scanner.nextLine().trim();
        if (newFloor.isEmpty()) {
            System.out.println("O piso não pode ser vazio.");
            return;
        }
    
        System.out.print("Novo edifício do dispositivo: ");
        String newBuilding = scanner.nextLine().trim();
        if (newBuilding.isEmpty()) {
            System.out.println("O edifício não pode ser vazio.");
            return;
        }
    
        // Deletando o dispositivo existente antes de criar o novo
        try {
            restTemplate.delete(BASE_URL + "/" + id);
            System.out.println("Dispositivo eliminado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao eliminar dispositivo: " + e.getMessage());
            return;
        }
    
        // Criando um novo dispositivo com os dados atualizados
        IoTDevice updatedDevice = new IoTDevice();
        updatedDevice.setDeviceID(id);  // Mantendo o mesmo ID
        updatedDevice.setQuarto(newRoom);
        updatedDevice.setServiço(newService);
        updatedDevice.setPiso(newFloor);
        updatedDevice.setEdificio(newBuilding);
    
        // Adicionando o novo dispositivo
        try {
            restTemplate.postForObject(BASE_URL, updatedDevice, IoTDevice.class);
            System.out.println("Dispositivo atualizado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar dispositivo: " + e.getMessage());
        }
    }
    

private void deleteDevice(Scanner scanner) {
    System.out.print("ID do dispositivo a ser eliminado: ");
    int id = scanner.nextInt();

    // Verifica todos os dispositivos (GET)
    IoTDevice[] devices = restTemplate.getForObject(BASE_URL, IoTDevice[].class);
    if (devices == null || devices.length == 0) {
        System.out.println("Nenhum dispositivo encontrado.");
        return;
    }


    // Procura o dispositivo pelo ID
    IoTDevice deviceToDelete = null;
    for (IoTDevice device : devices) {
        if (device.getDeviceID() == id) {
            deviceToDelete = device;
            break;
        }
    }

    if (deviceToDelete == null) {
        System.out.println("Dispositivo com ID " + id + " não encontrado.");
        return;
    }

    try {
        // Exclui o dispositivo com o ID fornecido
        restTemplate.delete(BASE_URL + "/" + id);
        System.out.println("Dispositivo eliminado com sucesso");
    } catch (Exception e) {
        System.out.println("Erro ao eliminar dispositivo: " + e.getMessage());
        }
    }

  
}
