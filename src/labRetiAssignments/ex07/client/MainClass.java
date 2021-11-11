package labRetiAssignments.ex07.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainClass
{	
	private static InetAddress server_address;
	private static int server_port;
	private static int number_of_pings = 10;
	
	public static void main(String[] args)
	{
		if(!areArgumentsValid(args))
		{
			return;
		}
		
		try
		{
			PingClient client = new PingClient(server_address, server_port);
			client.sendPings(number_of_pings);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static boolean areArgumentsValid(String[] args)
	{
		if(args.length != 2)
		{
			System.out.println("ERR -args");
			System.out.println("Arguments must be 2: server name, server port");
			return false;
		}
		
		try
		{
			server_address = InetAddress.getByName(args[0]);
			server_port = Integer.parseInt(args[1]);
		}
		catch(UnknownHostException e)
		{
			System.out.println("ERR -arg 1");
			System.out.println("Unknown Host");
			return false;
		}
		catch(NumberFormatException e)
		{
			System.out.println("ERR -arg 2");
			System.out.println("Port number invalid");
			return false;
		}	
		
		return true;
	}
}
