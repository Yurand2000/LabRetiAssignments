package labRetiAssignments.ex09;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;

public class MainClassClient
{
	private static String address_name = "192.168.1.144";
	private static String stop_string = "close";
	private static int port = 8080;
	private static BufferedReader console = null;
	private static EchoClient client = null;
	
	public static void main(String[] args) throws IOException
	{
		InetAddress address = InetAddress.getByName(address_name);		
		client = new EchoClient(address, port);
        console = new BufferedReader(new InputStreamReader(System.in));
		
		try
		{
			client.connect();
			System.out.println("Connected to server. Send '" + stop_string + "' to stop");

			readConsoleAndSendToServer();

			System.out.println("Closing connection.");
			client.close();
		}
		catch(ConnectException e)
		{
			System.out.println("Couldn't connect to server.");
		}
	}
	
	private static void readConsoleAndSendToServer() throws IOException
	{
		String line = console.readLine();
		while(line != null && !line.equals(stop_string))
		{
			client.sendAndReceive(line);
			line = console.readLine();
		}
	}
}
