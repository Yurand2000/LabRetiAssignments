package labRetiAssignments.ex07.server;

import java.io.IOException;
import java.net.InetAddress;

public class MainClass
{
	private static int max_latency = 500;
	private static int min_latency = 50;
	private static float loss_rate = 0.25f;
	private static int server_port;
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		if(areArgumentsValid(args))
		{
			PingServer server = new PingServer(
				InetAddress.getLoopbackAddress(), server_port, min_latency, max_latency, loss_rate);
			
			server.startServer();
			
			System.in.read();
			server.stopServer();
		}		
	}

	private static boolean areArgumentsValid(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("ERR -args");
			System.out.println("Arguments must be 1: server port");
			return false;
		}
		
		try
		{
			server_port = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException e)
		{
			System.out.println("ERR -arg 1");
			System.out.println("Port number invalid");
			return false;
		}	
		
		return true;
	}
}
