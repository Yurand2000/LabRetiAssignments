package labRetiAssignments.ex09;

import java.io.IOException;
import java.net.InetAddress;

public class MainClassServer
{
	private static String address_name = "192.168.1.144";
	private static int port = 8080;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		InetAddress address = InetAddress.getByName(address_name);

		EchoServer server = new EchoServer(address, port);
		server.startServer();
		
		System.in.read();
		server.stopServer();
	}

}
