import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;


public class Client {

    public static void main(String[] args) {
        try {
            // Referencing the remote object from the rmiregistry
            Registry registry = LocateRegistry.getRegistry("localhost", 5041); // Change if port is already taken

            // Get remote object from string ID
            RemoteInterface remoteObject = (RemoteInterface) registry.lookup("RemoteEmailObject");

            // Get connection message from the server's method
            String connectionMessage = remoteObject.sayHello();
            System.out.println(connectionMessage);

            // Register users
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scan.nextLine();

            remoteObject.registerClient(username); // Register client's name on object

            printMenu(); // Print user menu for user actions

            while(true) {

                System.out.println("\nSelect an action. Type '6' for help.");

                int choice = scan.nextInt(); // Get user's input
                scan.nextLine();

                if (choice == 1){ // View Inbox

                    List<Email> userInbox = remoteObject.getClientInbox(username); // Get client's inbox list

                    System.out.println("\nInbox for user " + username + ":");

                    for (int emailNumber = 0; emailNumber < userInbox.size(); emailNumber++) { // Loop through every email and print it out

                        Email email = userInbox.get(emailNumber); // Get current email

                        // Print out the general email information
                        System.out.println(email.getId() + "- " + "From: " + email.getSender() + ", Subject: " + email.getSubject());
                    }

                } else if (choice == 2) { // View Sent Emails

                    List<Email> sentEmails = remoteObject.getClientSentItems(username);

                    System.out.println("\nSent Items for user " + username + ":");

                    for (int emailNumber = 0; emailNumber < sentEmails.size(); emailNumber++) {

                        Email email = sentEmails.get(emailNumber);
                        System.out.println(email.getId() + "- " + "Subject: " + email.getSubject() + ", To: " + email.getRecipient());

                    }

                } else if (choice == 3) { // Open an Email in the client's inbox


                    System.out.print("\nEnter the email id to open: ");
                    int emailIdToOpen = scan.nextInt(); // Choose email to open via ID

                    Email openedEmail = remoteObject.openEmail(emailIdToOpen, username); // Call method to server

                    if (openedEmail != null) { // If email ID is present in of the client's inboxes

                        System.out.println("\nEmail details:");
                        System.out.println("Email ID: " + openedEmail.getId());
                        System.out.println("Subject: " + openedEmail.getSubject());
                        System.out.println("Sender: " + openedEmail.getSender());
                        System.out.println("Message: " + openedEmail.getMessage());

                    } else {
                        System.out.println("Email not found.");
                    }

                } else if (choice == 4) { // Send Email

                    // Username of the person to get the email
                    System.out.print("\nEnter recipient's username: ");
                    String recipientUsername = scan.nextLine();

                    // The Email's subject
                    System.out.print("Enter email subject: ");
                    String subject = scan.nextLine();

                    // The Email's message
                    System.out.print("Enter email message: ");
                    String message = scan.nextLine();

                    remoteObject.sendEmail(username, recipientUsername, subject, message); // Call the send email method from the server

                } else if (choice == 5) { // Deleting an Email

                    // Entering the email ID that the client wants to delete locally
                    System.out.print("\nEnter the email id to delete: ");
                    int emailIdToDelete = scan.nextInt();

                    remoteObject.deleteEmail(emailIdToDelete, username);

                } else if (choice == 6) {

                    printMenu(); // Call method to print out options

                } else if (choice == 7) {
                    // Exit
                    System.out.println("Leaving the server.");
                    return;
                    
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function that prints the user manual to help call operations
    public static void printMenu(){
        System.out.println("\nHere are your options. To pick one, type in the number corresponding to it:");
        System.out.println("1. View Inbox");
        System.out.println("2. View Sent Items");
        System.out.println("3. Open Email");
        System.out.println("4. Send Email");
        System.out.println("5. Delete Email");
        System.out.println("6. Help");
        System.out.println("7. Exit");
    }

}