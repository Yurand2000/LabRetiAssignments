package labRetiAssignments.ex10;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

public class MulticastClient
{
	private static final int read_packets_total = 10;
	private static final int datagram_max_size = 512;

	private MulticastSocket socket;
	private InetAddress address;
	private int port;	
	
	public MulticastClient(InetAddress multicast_address, int multicast_port)
	{
		address = multicast_address;
		port = multicast_port;
		socket = null;
	}
	
	public void connect() throws IOException
	{
		join_group();
		read_packets();
		leave_group();
	}
	
	private void join_group() throws IOException
	{
		socket = new MulticastSocket(port);
		socket.joinGroup(address);
	}
	
	private void read_packets() throws IOException
	{
		byte[] container = new byte[datagram_max_size];
		DatagramPacket datagram = new DatagramPacket(container, container.length);
		
		for(int i = 0; i < read_packets_total; i++)
		{
			socket.receive(datagram);
			System.out.println(new String(datagram.getData(), StandardCharsets.UTF_8));
		}
	}
	
	private void leave_group() throws IOException
	{
		socket.leaveGroup(address);
		socket.close();
		socket = null;
	}
}
