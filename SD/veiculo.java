

public class veiculo implements  java.io.Serializable{
    private String matricula;
    private String modelo;
    private String tipo;
    private String localizacao;
    private String estadoAluguer;
    private boolean estadoAdmin;
    private int veiculoID;

    
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getLocalizacao() {
        return localizacao;
    }
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    public String getEstadoAluguer() {
        return estadoAluguer;
    }
    public void setEstadoAluguer(String estadoAluguer) {
        this.estadoAluguer = estadoAluguer;
    }
    public boolean isEstadoAdmin() {
        return estadoAdmin;
    }
    public void setEstadoAdmin(boolean estadoAdmin) {
        this.estadoAdmin = estadoAdmin;
    }
    public int getVeiculoID() {
        return veiculoID;
    }
    public void setVeiculoID(int veiculoID) {
        this.veiculoID = veiculoID;
    }
    
}