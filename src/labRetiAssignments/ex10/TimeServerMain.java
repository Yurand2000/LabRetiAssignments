package labRetiAssignments.ex10;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TimeServerMain
{
	private static InetAddress multicast_address;
	private static int multicast_port;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		try
		{
			parseArgs(args);
			
			MulticastServer server = new MulticastServer(multicast_address, multicast_port);
			server.startServer();
			
			System.in.read();
			server.stopServer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void parseArgs(String[] args) throws UnknownHostException
	{
		if(args.length == 2)
		{
			multicast_address = InetAddress.getByName(args[0]);
			multicast_port = Integer.parseInt(args[1]);
		}
		else
		{
			throw new IllegalArgumentException("Arguments are not two: you should enter IpAddress and Port of your multicast group.");
		}
	}
}
