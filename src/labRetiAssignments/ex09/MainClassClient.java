package labRetiAssignments.ex09;

import java.io.*;
import java.net.InetAddress;

public class MainClassClient
{
	private static String address_name = "192.168.1.144";
	private static int port = 8080;
	
	public static void main(String[] args) throws IOException
	{
		InetAddress address = InetAddress.getByName(address_name);
		
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));		
		EchoClient client = new EchoClient(address, port);
		client.connect();

		System.out.println("connected...");
		String line = reader.readLine();
		while(line != null && !line.equals("close"))
		{
			client.sendAndReceive(line);
			line = reader.readLine();
		}

		System.out.println("closing...");
		client.close();
	}
}
