package labRetiAssignments.ex11;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

public class MySocketFactory extends RMISocketFactory
{
	@Override
	public Socket createSocket(String host, int port) throws IOException
	{
		System.out.println("New Socket: " + host + " " + port);
		return RMISocketFactory.getDefaultSocketFactory().createSocket(host, port);
	}

	@Override
	public ServerSocket createServerSocket(int port) throws IOException
	{
		System.out.println("New ServerSocket: " + port);
		return RMISocketFactory.getDefaultSocketFactory().createServerSocket(port);
	}

}
