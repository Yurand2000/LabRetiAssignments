package labRetiAssignments.ex10;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

public class TimeClientMain
{
	private static String multicast_address = "239.255.1.3";
	private static int multicast_port = 8082;

	public static void main(String[] args) throws IOException
	{
		MulticastSocket ms = new MulticastSocket(multicast_port);
		InetAddress group = InetAddress.getByName(multicast_address);
		ms.joinGroup(group);
		
		byte[] data = new byte[512];
		DatagramPacket p = new DatagramPacket(data, data.length);
		
		for(int i = 0; i < 10; i++)
		{
			ms.receive(p);
			System.out.println(new String(p.getData(), StandardCharsets.UTF_8));
		}
		
		ms.leaveGroup(group);
		ms.close();
	}
}
