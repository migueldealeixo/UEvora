public class aluguer implements java.io.Serializable {
    private int veiculoID;
    private int clienteID;
    private float valor;
    private String dataInicio;
    private float duracaoPrev;
    private int aluguerID;
    
    public int getVeiculoID() {
        return veiculoID;
    }
    public void setVeiculoID(int veiculoID) {
        this.veiculoID = veiculoID;
    }
    public int getClienteID() {
        return clienteID;
    }
    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public String getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }
    public float getDuracaoPrev() {
        return duracaoPrev;
    }
    public void setDuracaoPrev (float duracaoPrev) {
        this.duracaoPrev = duracaoPrev;
    }
    public int getAluguerID() {
        return aluguerID;
    }
    public void setAluguerID(int aluguerID) {
        this.aluguerID = aluguerID;
    }

}
