import java.io.*;
import java.util.*;

public class clienteServer {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static remoteObject obj;


    private static void menu() throws IOException{

        System.out.println("1- Registrar um novo veiculo");
        System.out.println("2- Registro Cliente");
        System.out.println("3- Registrar novo aluguer");
        System.out.println("4- Listar veiculos disponiveis para aluguer");
        System.out.println("5- Listar localização de veiculos alugados");
        System.out.println("6- Histórico");
        System.out.println("0- Sair");

        int option = -1;

        try {
            option = Integer.parseInt(br.readLine());
            if(option < 0 || option > 6){
                System.out.println("Insira uma das opções apresentadas");
                menu();
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção Invalida");
            menu();
        }
        switch (option) {
            case 0:
                System.exit(1);
                break;
            case 1:
                registrarVeiculo();
                break;
            case 2:
                registrarCliente();
                break;
             case 3:
                registrarAluguer();
                break;
          case 4:
                listarVeiculos();
                break;
             case 5:
                localizarVeiculos();
              case 6:
                consultarHistorico();
                break;
            default:
                menu();
         }
         menu();
    }

    private static void registrarVeiculo() throws IOException{
        veiculo veiculo = new veiculo();

        String matricula ="";
        do {
            System.out.println("Insira a matricula do veiculo");
            matricula = br.readLine();
            matricula= matricula.trim();
        } while (matricula.length() == 0);
            veiculo.setMatricula(matricula);

        String modelo = "";
        do{
            System.out.println("Insira o modelo do veiculo");
            modelo = br.readLine();
            modelo = modelo.trim();
        }while(modelo.length() == 0);
            veiculo.setModelo(modelo);

        String tipo = "";
        do{
            System.out.println("Insira o tipo de veiculo");
            tipo = br.readLine();
            tipo = tipo.trim();
        }while(tipo.length() == 0);
            veiculo.setTipo(tipo);

        String localizacao = "";
        do{
            System.out.println("Insira a localizacao do veiculo");
            localizacao = br.readLine();
            localizacao = localizacao.trim();
        }while(localizacao.length() == 0);
            veiculo.setLocalizacao(localizacao);

        String estadoAluguer = "";
        do { 
            System.out.println("Qual o estado de aluguer do veiculo");
            estadoAluguer = br.readLine();
            estadoAluguer = estadoAluguer.trim();
        } while (estadoAluguer.length() == 0);
            veiculo.setEstadoAluguer(estadoAluguer);

        //estadoAdmin
        boolean estadoAdmin = false;
        veiculo.setEstadoAdmin(estadoAdmin);

        int id = obj.sendVeiculo(veiculo);
        veiculo.setVeiculoID(id);
        System.out.println("Veiculo " + id + " inserido");

    }

public static void registrarCliente() throws IOException{
    cliente cliente = new cliente();

    String name = "";
    do {
        System.out.println("Insira o nome do cliente: ");
        name = br.readLine();
        name = name.trim();
        } while (name.length() == 0);
        cliente.setName(name);
    int idNumber = 0;
    do {
        System.out.println("Insira o numero de ID do cliente: ");
        idNumber = Integer.parseInt(br.readLine());
    } while (idNumber == 0);
        cliente.setIdNumber(idNumber);
    int numeroTele = 0;
    do {
        System.out.println("Insira o numero de telemovel do cliente: ");
        numeroTele = Integer.parseInt(br.readLine());
    } while (numeroTele == 0);
        cliente.setNumeroTele(numeroTele);

    int id = obj.sendCliente(cliente);
    cliente.setClienteID(id);
    System.out.println("Cliente " + id + " inserido");

}

public static void registrarAluguer() throws IOException{
    aluguer aluguer = new aluguer();

    int veiculoID = 0;
    do{
        System.out.println("Insira o ID do veiculo: ");
        veiculoID = Integer.parseInt(br.readLine());
    }while(veiculoID == 0);
    aluguer.setVeiculoID(veiculoID);

    int clienteID = 0;
    do{
        System.out.println("Insira o ID do cliente: ");
        clienteID = Integer.parseInt(br.readLine());
    }while(clienteID == 0);
    aluguer.setClienteID(clienteID);

    float valor = 0;
    do {
        System.out.println("Insira o valor da viagem: ");
        valor = Float.parseFloat(br.readLine());
    } while (valor == 0);
    aluguer.setValor(valor);

    String dataInicio = "";
    do {
        System.out.println("Insira a data de inicio(DD/MM/YYYY): ");
        dataInicio = br.readLine();
        dataInicio = dataInicio.trim();
    } while (dataInicio.length() == 0);
        aluguer.setDataInicio(dataInicio);

    float duracaoPrev = 0;
    do { 
        System.out.println("Insira a duração prevista: ");
        duracaoPrev = Float.parseFloat(br.readLine());
    } while (duracaoPrev == 0);

    int id = obj.sendAluguer(aluguer);
    aluguer.setAluguerID(id);
    System.out.println("Aluguer " + id + " inserido");


}
private static boolean userChoice(String line) {
    do {
        if (line.equalsIgnoreCase("sim")) {
            return true;
        } else if (line.equalsIgnoreCase("não")) {
            return false;
        }
        System.out.println("input Invalido. Escrever 'sim' ou 'não': ");

    } while (!line.equalsIgnoreCase("sim") || !line.equalsIgnoreCase("não"));
    return false;
}



public static void listarVeiculos() throws java.rmi.RemoteException, IOException{
    System.out.println("Filtrar por localização? ");
    String where = "where ";
    boolean first = userChoice(br.readLine());
    if(first){
        System.out.println("Inserir cidade: ");
        where = where + "localizacao='" + br.readLine().trim()+ "'";
    }
    System.out.println("Filtrar por tipo de veiculo? ");
    boolean second = userChoice(br.readLine());
    if(first == true && second == true){
        System.out.println("Inserir tipo de veiculo: ");
        where = where + "and tipo='" + br.readLine().trim()+ "'";
    }else if (first == false && second == true) {
        System.out.println("Inserir tipo de veiculo: ");
        where = where + "and tipo='" + br.readLine().trim()+ "'";
        
    }
    System.out.println("where");
    List<veiculo> results = obj.consultarVeiculos(where);

    if(results.size()> 0){
        for(veiculo veiculo : results){
            System.out.println("id: "+ veiculo.getVeiculoID() + "- " + veiculo.getMatricula());
        }

    }
}



public static void localizarVeiculos() throws java.rmi.RemoteException, IOException{
    String where = " where estadoaluguer ='Ocupado' ";
    List<veiculo> results = obj.consultarVeiculos(where);
    List<String> locals = new ArrayList<>();

    if(results.size()> 0){
        for(veiculo veiculo : results){
            if(!locals.contains(veiculo.getLocalizacao())){
                locals.add(veiculo.getLocalizacao());
            }
            }
            for(String local :locals){
                System.out.println(local);
            }
          }else{
            System.out.println("Não existem veículos ocupados");
          }

}
public static void consultarHistorico() throws java.rmi.RemoteException,IOException{
    System.out.println("Qual o ID do veiculo? ");
    int veiculoID = Integer.parseInt(br.readLine());
    String where = "where veiculoID=" + veiculoID;
    List<aluguer> results = obj.consultarAluguer(where);
    List<veiculo> results2 = obj.consultarVeiculos(where);
    List<String> historico = new ArrayList<>();

    if(results.isEmpty()){
        System.out.println("Nenhuma informação acerca desse veiculo...");
        return;
    }

    for(veiculo v : results2){
        historico.add("Veiculo ID:" + v.getVeiculoID() + "\nLocalizacao do Aluguer: " + v.getLocalizacao() );
    }
    for(aluguer a : results){
    String infoAluguer = "Data de Aluguer: " + a.getDataInicio() + "\nValor da Viagem: " + a.getValor();
    historico.add(infoAluguer);
    }
    for(String history :historico){
        System.out.println(history);
    }

}
    public static void main(String[] args) {
        String regHost = "localhost";
        String regPort = "9000";

        if (args.length != 2) {
            System.out.println("Falt argumentos!");
            System.exit(1);
        }

        regHost = args[0];
        regPort = args[1];
        try {
            obj = (remoteObject) java.rmi.Naming.lookup("rmi://" + regHost + ":" + regPort + "/remoteObject");

            System.out.println("Bem vindo ao servidor \n");
            System.out.println("");

            menu();

        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}