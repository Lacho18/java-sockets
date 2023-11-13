import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	final static int PORT = 10000;
	public static List<ServerClient> clients = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		Random random = new Random();
		int numberToGuess = random.nextInt(100);
		System.out.println("THIS IS THE NUMBER TO GUESS : "+ numberToGuess);
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Server has started");
		
		while(true) {
			Socket socket = s.accept();
			System.out.println("Incoming request");
			try {
				ServerClient client = new ServerClient(socket, numberToGuess);
				client.start();
				clients.add(client);
			}
			catch(IOException e) {
				if(socket != null) socket.close();
			}
		}
	}
	
	public static synchronized String guessingNumber(ServerClient client) {
		String clientInput = "";
		String name = client.getMyName();
		BufferedReader in = client.getBufferedReader();
		try {
			String suobshtenie = "Player " + name + " is guessing at the moment";
			broadCasting(client, suobshtenie);
			client.getPrintWriter().println("IT'S YOUR TURN!");
			clientInput = in.readLine();
			client.hints(clientInput);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return clientInput;	
	}
	
	public static void broadCasting(ServerClient currentClient, String message) {
		for(ServerClient client : clients) {
			if(currentClient != client) {
				PrintWriter clientOut = client.getPrintWriter();
				clientOut.println(message);
			}
		}
	}
}
