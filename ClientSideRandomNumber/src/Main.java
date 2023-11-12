import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    final static String IP = "127.0.0.1";
    final static int PORT = 10000;

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket(IP, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Connected to the server.");
        
        	String intro = in.readLine();
        	System.out.println(intro);
        	System.out.println("");
        	System.out.println("");
        	String forName = in.readLine();
        	System.out.println(forName);
        	String name = scanner.next();
        	out.println(name);
        	
        	Thread messageReaderThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("Server says: " + serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            messageReaderThread.start();

        try {
            while (true) {
                System.out.println("Enter your guess (or type 'end' to exit): ");
                String input = scanner.nextLine();

                if ("end".equalsIgnoreCase(input)) {
                    out.println("end");
                    break;
                }

                try {
                    int number = Integer.parseInt(input);
                    out.println(number);

                    String response = in.readLine();
                    //System.out.println(response);

                    if (response.equals("You guessed it! Congratulations")) {
                        break; // Exit the loop if the guess is correct
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        } finally {
            socket.close();
            scanner.close();
        }
    }
}
