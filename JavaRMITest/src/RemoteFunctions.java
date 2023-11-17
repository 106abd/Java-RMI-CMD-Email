import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class RemoteFunctions extends UnicastRemoteObject implements RemoteInterface {

    private Map<String, List<Email>> userInboxes;
    private Map<String, List<Email>> userSentItems;

    protected RemoteFunctions() throws RemoteException {
        super();
        this.userInboxes = new HashMap<>();
        this.userSentItems = new HashMap<>();
    }

    // Function that prints a successful connection message. This message can only appear to clients through calling it, confirming a true connection to the server
    @Override
    public String sayHello() throws RemoteException {
        return "Connected to Server!";
    }

    // Function that allows a client to send an email to another client
    @Override
    public void sendEmail(String sender, String recipient, String subject, String message) throws RemoteException {

        // Ensuring that both the sender and recipient have their inboxes and sent items created
        userInboxes.putIfAbsent(sender, new ArrayList<>());
        userSentItems.putIfAbsent(sender, new ArrayList<>());
        userInboxes.putIfAbsent(recipient, new ArrayList<>());


        // Creating an Email

        Email email = new Email(); // Create an Email object
        email.setId(userInboxes.get(recipient).size() + 1); // Set the email's ID
        email.setSender(sender); // Attach the sender's username
        email.setRecipient(recipient); // Attach the recipient's username
        email.setSubject(subject); // Attach the email's subject text
        email.setMessage(message); // Attach the email's message text

        //  Append the email to the recipient's inbox
        userInboxes.get(recipient).add(email);

        // Add a copy to the sender's sent items inbox
        userSentItems.get(sender).add(email);

    }

    // Function that deletes an email locally for a client
    @Override
    public void deleteEmail(int emailId, String username) throws RemoteException {

        // Remove the email from the sender's sent items inbox
        userSentItems.getOrDefault(username, new ArrayList<>())
                .removeIf(email -> email.getId() == emailId);

        // Remove the email from the recipient's inbox
        userInboxes.getOrDefault(username, new ArrayList<>())
                .removeIf(email -> email.getId() == emailId);

    }

    // Function that gives the entire client's inbox
    @Override
    public List<Email> getClientInbox(String username) throws RemoteException {

        // Return a copy of the client's inbox
        List<Email> inboxCopy = new ArrayList<>(userInboxes.getOrDefault(username, new ArrayList<>()));

        // Exclude sent items from the inbox
        inboxCopy.removeAll(userSentItems.getOrDefault(username, new ArrayList<>()));

        return inboxCopy; // Return the filtered inbox
    }


    // Function that gives the client's sent emails
    @Override
    public List<Email> getClientSentItems(String username) throws RemoteException {

        // Return a copy of the client's sent items
        return new ArrayList<>(userSentItems.getOrDefault(username, new ArrayList<>()));
    }


    // Function that register's the client to the server, and thus other clients
    @Override
    public void registerClient(String username) throws RemoteException {

        // Register the user with an empty inbox
        userInboxes.put(username, new ArrayList<>());

        // Register the user with an empty sent emails inbox
        userSentItems.put(username, new ArrayList<>());

    }

    // Function that opens email's in any of the local client's inboxes
    @Override
    public Email openEmail(int emailId, String username) throws RemoteException {

        // Search in the client's inbox to find the email with the given id
        for (Email email : userInboxes.getOrDefault(username, new ArrayList<>())) {
            if (email.getId() == emailId) {
                return email;
            }
        }

        // Search in the client's sent inbox to find the email with the given id
        for (Email email : userSentItems.getOrDefault(username, new ArrayList<>())) {
            if (email.getId() == emailId) {
                return email;
            }
        }
        return null; // If the Email is not found
    }
}