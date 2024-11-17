import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class remoteObjectImpl extends  UnicastRemoteObject implements remoteObject{
    private final conexaoBD cbd;
    public remoteObjectImpl(conexaoBD cbd) throws java.rmi.RemoteException{
        this.cbd = cbd;
    }
    public int sendCliente(cliente cliente) throws java.rmi.RemoteException{
        return cbd.inserirCliente(cliente);
    }

    public int sendAluguer(aluguer aluguer) throws java.rmi.RemoteException{
        return cbd.inserirAluguer(aluguer);
    }

    public int sendVeiculo(veiculo veiculo) throws java.rmi.RemoteException{
        return cbd.inserirVeiculo(veiculo);
    }
    
    public List<veiculo> consultarVeiculos(String fields){
        return cbd.checkVeiculo(fields);
    }

    public void updateState(Boolean estado, int veiculoID)throws java.rmi.RemoteException{
        cbd.atualizarEstado(estado, veiculoID);
    }

    public List<aluguer> consultarAluguer(String fields){
        return cbd.checkAluguer(fields);
    }

}
