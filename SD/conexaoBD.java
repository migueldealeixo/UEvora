import java.sql.*;
import java.util.*;

public class conexaoBD implements java.io.Serializable{

    private Connection con = null;
    private Statement stmt = null;

    private final String PG_HOST;
    private final String PG_DB;
    private final String USER;
    private final String PWD;

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public conexaoBD() {
        PG_HOST = "localhost";
        PG_DB = "trabalho1";
        USER = "user1";
        PWD = "umaPas";
      
    }

    public conexaoBD(String host, String bd, String user, String pass) {
        PG_HOST = host;
        PG_DB = bd;
        USER = user;
        PWD = pass;
        
    }
    public void connect() {

        try {

            Class.forName("org.postgresql.Driver");

            // url = "jdbc:postgresql://host:port/database", psql trabalho1 -U user1 -h localhost
            con = DriverManager.getConnection("jdbc:postgresql://" + PG_HOST + ":5432/" + PG_DB, USER, PWD);
            stmt = con.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems setting the connection");

        }
    }

    public int inserirCliente(cliente cliente){
        int clienteID = -1;
        String insertQuery = "insert into cliente (name,idNumber, numerotele) values(?,?,?) returning clienteID;";


        try(PreparedStatement pstmt = con.prepareStatement(insertQuery)){
            pstmt.setString(1, cliente.getName());
            pstmt.setInt(2,cliente.getIdNumber());
            pstmt.setInt(3, cliente.getNumeroTele());

            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                clienteID = resultSet.getInt("clienteID");
                cliente.setClienteID(clienteID);
            }
            System.out.println("Inserido cliente com id: " + clienteID);
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Problema a inserir cliente");
        }
        return clienteID;
    }

public int inserirVeiculo(veiculo veiculo){
    int veiculoID = -1;
    String insertQuery = "insert into veiculo(matricula,modelo,tipo,localizacao,estadoAluguer,estadoAdmin) values (?,?,?,?,?,?)returning veiculoID; ";
    try (PreparedStatement pstmt = con.prepareStatement(insertQuery)){
        pstmt.setString(1, veiculo.getMatricula());
        pstmt.setString(2, veiculo.getModelo());
        pstmt.setString(3, veiculo.getTipo());
        pstmt.setString(4, veiculo.getLocalizacao());
        pstmt.setString(5, veiculo.getEstadoAluguer());
        pstmt.setBoolean(6, veiculo.isEstadoAdmin());

        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            veiculoID = resultSet.getInt("veiculoID");
            veiculo.setVeiculoID(veiculoID);
        }
        System.out.println("Inserido veiculo com id: " + veiculoID);
    }catch(Exception e){
       e.printStackTrace();
        System.err.println("Problema a inserir veiculo");
    }
    return veiculoID;
}

public int inserirAluguer(aluguer aluguer){
    int aluguerID = -1;
    String insertQuery = "insert into aluguer(veiculoID, clienteID,valor,datainicio,duracaoPrev) values (?,?,?,?,?)returning aluguerID;";
    try(PreparedStatement pstmt = con.prepareStatement(insertQuery)){
        pstmt.setInt(1, aluguer.getVeiculoID());
        pstmt.setInt(2, aluguer.getClienteID());
        pstmt.setFloat(3, aluguer.getValor());
        pstmt.setString(4, aluguer.getDataInicio());
        pstmt.setFloat(5, aluguer.getDuracaoPrev());
       

        ResultSet resultSet = pstmt.executeQuery();

        if(resultSet.next()){
            aluguerID = resultSet.getInt("aluguerID");
            aluguer.setAluguerID(aluguerID);
        }
        System.out.println("Inserido aluguer com id: " + aluguerID);
    }catch(Exception e){
        e.printStackTrace();
        System.err.println("Problema a inserir aluguer");
    }
    return aluguerID;

}

public List<veiculo> checkVeiculo(String fields){
    List<veiculo> results = new ArrayList<veiculo>();
    veiculo veiculo = null;

    try {
        String insertQuery = "select * from veiculo " + fields + ";";
        ResultSet rs = stmt.executeQuery(insertQuery);
        int count = 0;
        while(rs.next()){
            String matricula = rs.getString("matricula");
            String modelo = rs.getString("modelo");
            String tipo = rs.getString("tipo");
            String localizacao = rs.getString("localizacao");
            String estadoaluguer = rs.getString("estadoaluguer");
            boolean estadoAdmin = rs.getBoolean("estadoadmin");
            int id = rs.getInt("veiculoID");

            veiculo = new veiculo();
            veiculo.setMatricula(matricula);
            veiculo.setModelo(modelo);
            veiculo.setTipo(tipo);
            veiculo.setLocalizacao(localizacao);
            veiculo.setEstadoAluguer(estadoaluguer);
            veiculo.setEstadoAdmin(estadoAdmin);
            veiculo.setVeiculoID(id);

            results.add(veiculo);
            count++;
        }
        if (count == 0){
            System.out.println("Nenhum veiculo encontrado");
            rs.close();
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Problema a obter dados...");
    }
    return results;

}

public List<aluguer> checkAluguer(String fields){
    List<aluguer> results = new ArrayList<>();
    aluguer aluguer = null;
    try {
        String insertQuery = "select * from aluguer " + fields + " ;";
        ResultSet rs = stmt.executeQuery(insertQuery);
        int count = 0;
        while(rs.next()){
            int veiculoID = rs.getInt("veiculoid");
            int clienteID = rs.getInt("clienteid");
            float valor = rs.getFloat("valor");
            String dataInicio = rs.getString("datainicio");
            float duracaoPrev = rs.getFloat("duracaoprev");
            int id = rs.getInt("aluguerid");

            aluguer = new aluguer();
            aluguer.setVeiculoID(veiculoID);
            aluguer.setClienteID(clienteID);
            aluguer.setValor(valor);
            aluguer.setDataInicio(dataInicio);
            aluguer.setDuracaoPrev(duracaoPrev);
            aluguer.setAluguerID(id);

            results.add(aluguer);
            count++;

        }
        if(count == 0){
            System.out.println("Nenhum aluguer encontrado");
            rs.close();
        }
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Problemas a obter dados...");
    }
    return results;
}

public void atualizarEstado(boolean estado, int veiculoID) {
    try {
        String updateQuery = "UPDATE veiculo SET estadoAdmin = ? WHERE veiculoID = ?;";
        try (PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.setBoolean(1, estado);

            pstmt.setInt(2, veiculoID);

            pstmt.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Problema ao atualizar a tabela...");
    }

}



    public void disconnect() { // importante: fechar a ligacao 'a BD
    try {
        stmt.close();
        con.close();
    } catch (Exception e) {

    }
 }
}
