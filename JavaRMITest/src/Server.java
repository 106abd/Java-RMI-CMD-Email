import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {

        try {
            RemoteInterface remoteObject = new RemoteFunctions();

            // Binding the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(5041);

            // Bind remoteObject to a String ID
            registry.rebind("RemoteEmailObject", remoteObject);

            System.out.println("Server is ready.");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}