public class cliente implements java.io.Serializable{
    private String name;
    private int idNumber;
    private int numeroTele;
    private int clienteID;
   
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIdNumber() {
        return idNumber;
    }
    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }
    public int getNumeroTele() {
        return numeroTele;
    }
    public void setNumeroTele(int numeroTele) {
        this.numeroTele = numeroTele;
    }
    public int getClienteID(){
        return clienteID;
    }
    public void setClienteID(int clienteID){
        this.clienteID = clienteID;
    }
}