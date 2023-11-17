// Define the remote interface
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteInterface extends Remote{

    String sayHello() throws RemoteException;

    // Method to send an email
    void sendEmail(String sender, String recipient, String subject, String message) throws RemoteException;

    // Method to delete an email
    void deleteEmail(int emailId, String username) throws RemoteException;

    // Method to retrieve all emails
    List<Email> getClientInbox(String username) throws RemoteException;

    // Method to retrieve all sent emails for a specific user
    List<Email> getClientSentItems(String username) throws RemoteException;

    // Method to register a client
    void registerClient(String username) throws RemoteException;

    // Method to open an email and retrieve its details
    Email openEmail(int emailId, String username) throws RemoteException;



}
