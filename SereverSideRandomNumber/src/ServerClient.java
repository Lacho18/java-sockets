import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerClient extends Thread{
	private PrintWriter out;
	private BufferedReader in;
	private Socket socket;
	boolean newPlayer = true;
	int theRandomNumber;
	String name;
	
	 ServerClient(Socket socket, int numberToGuess) throws IOException{
		this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        theRandomNumber = numberToGuess;
	}
	
	public void run() {
		try {
			//Intro of the game
			if(newPlayer) {
				System.out.println("HERE");
				out.println("Welcome to a game of guess number. A random number has been generated "
						+ "and you have to guess it. It's value is between 0 to 100. "
						+ "Guess its value!");
				out.println("Plese enter your name!");
				name = in.readLine();
				newPlayer = false;
			}
			while(true) {
				String clientInput = guessingNumber();
				String afterGuess = "Player " + name + " has made its guess";
				broadCasting(afterGuess);
	            
	            if (clientInput == null || clientInput.equals("end")) {
	                // Client disconnected or sent an "end" signal
	                System.out.println("Client disconnected");
	                break;
	            }

	            int guess = Integer.parseInt(clientInput);
	            String hint = "Player " + name + " made a guess! ";
				
				if(guess > theRandomNumber) {
					hint += "The number "+guess+" is too big!";
				}
				else if(guess < theRandomNumber) {
					hint += "The number "+guess+" is too small";
				}
				else {
					hint = "Player " + name + " guessed it! Congratulations";
					out.println(hint);
					break;
				}
				out.println(hint);
			}
			System.out.println("Closing connecton");
		}
		catch(IOException e) {
			System.out.println("Communication error");
		}
		finally {
			try {
				socket.close();
			}
			catch(IOException e) {
				System.out.println("Can't close socket");
			}
		}
	}
	
	public synchronized String guessingNumber() {
		String clientInput = "";
		try {
			String suobshtenie = "Player " + name + " is guessing at the moment";
			broadCasting(suobshtenie);
			clientInput = in.readLine();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return clientInput;	
	}
	
	public void broadCasting(String message) {
		for(ServerClient client : Main.clients) {
			out.println(message);
		}
	}
}
