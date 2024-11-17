

public class servidor{
    static conexaoBD cbd;
    public static void main(String[] args) {
        int regPort = 1099;

        if(args.length <1 ){
            System.out.println();
            System.exit(1);
        }
        try {
            regPort = Integer.parseInt(args[0]);
            cbd = new conexaoBD();
            cbd = new conexaoBD(args[1], args[2], args[3], args[4]);
            cbd.connect();

            remoteObject obj = new remoteObjectImpl(cbd);
            java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry(regPort);
            registry.rebind("remoteObject", obj);

          
             /*
             * //bd = new BDserver(  "localhost", "trabalho1","user1","umaPas");
             *
             * localhost trabalho1 user1 umaPas
             * bd = new BDserver( args[1],args[2],args[3],args[4]);
            */

            System.out.println("Server Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
 
