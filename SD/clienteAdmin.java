import java.io.*;
import java.util.*;

public class clienteAdmin {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static remoteObject obj;

        private static void menu() throws IOException{

            System.out.println("1. Listar veiculos por estado");
            System.out.println("2. Aprovar veiculos");
            System.out.println("0. Sair");

            int option = -1;

            try {
                option = Integer.parseInt(br.readLine());
                if (option < 0 || option > 2) {
                    System.out.println("Insira uma opção apresentada");
                    menu();
                }
            } catch (NumberFormatException e) {
                System.err.println("Opção inválida!");
                menu();
            }

            switch(option){
                case 0:
                    System.exit(1);
                    break;
                case 1:
                    consultarEstadoAdmin();
                    break;
                case 2:
                    updateState();
                    break;
                default:
                    menu();
            }
            menu();

        
    }
    private static void consultarEstadoAdmin() throws java.rmi.RemoteException, IOException{
        String where = " where estadoadmin= ";

        System.out.println("EstadoAdmin a procurar: 0: Não aprovado 1:Aprovado");
        int estado = Integer.parseInt(br.readLine());

        if(estado == 0){
            where = where + false;
        }else if(estado == 1){
            where = where + true;
        }
        System.out.println(where);
        List<veiculo> results = obj.consultarVeiculos(where);

        if(results.size() > 0){
            for(veiculo veiculo : results){
                System.out.println("id: " + veiculo.getVeiculoID());
            }
            }else{
                System.out.println("Nenhum veiculo");
        }
    }

    private static void updateState() throws java.rmi.RemoteException, IOException{

        List<veiculo> naoAprovado = obj.consultarVeiculos("where estadoadmin=" + false);

        if(naoAprovado.size()>0){
            for(veiculo veiculo : naoAprovado){
                System.out.println("id: " + veiculo.getVeiculoID());
            }
        }else{
                System.out.println("Nenhum veiculo! ");
                return;
        }

        System.out.println("ID do veiculo a aprovar: ");
        int where = Integer.parseInt(br.readLine());
        System.out.println(where);

        obj.updateState(true,where);

    }


public static void main(String[] args){
    String reghost="localhost";
    String regPort = "9000";

    if(args.length != 2){
        System.out.println("Faltam Argumentos! ");
        System.exit(1);
    }
    reghost = args[0];
    regPort = args[1];

    try {
        obj = (remoteObject) java.rmi.Naming.lookup("rmi://" + reghost + ":" + regPort + "/remoteObject");
        System.out.println("Bem vindo ao servidor! ");
        System.out.println("");

        menu();
    } catch (Exception e) {
        e.printStackTrace();
    }

}


}
