package labRetiAssignments.ex07.client;

import labRetiAssignments.ex07.PingFactory;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class PingDatagram
{
	int sequence_number;
	DatagramPacket ping_datagram;
	DatagramPacket echo_datagram;
	Instant ping_message_time;
	Instant echo_message_time;
	
	public PingDatagram(int sequence_number)
	{
		this.sequence_number = sequence_number;
	}
	
	public boolean trySendAndWaitForResponse(DatagramSocket socket, InetAddress address, int port) throws IOException
	{
		try
		{
			sendAndWaitForResponse(socket, address, port);
			printSuccessfulPing();
			return true;
		}
		catch(SocketTimeoutException e)
		{
			printFailedPing();
			return false;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public long getRoundTripTimeMillis()
	{
		return echo_message_time.toEpochMilli() - ping_message_time.toEpochMilli();
	}
	
	private void sendAndWaitForResponse(DatagramSocket socket, InetAddress address, int port)
			throws IOException, SocketTimeoutException
	{
		send(socket, address, port);
		receive(socket);
		while(!isEchoEqualToPing())
		{
			receive(socket);
		}
	}
	
	private void send(DatagramSocket socket, InetAddress address, int port) throws IOException
	{
		ping_message_time = Instant.now(); 
		ping_datagram = PingFactory.instance().makeNewPing(sequence_number, ping_message_time);
		ping_datagram.setAddress(address);
		ping_datagram.setPort(port);
		socket.send(ping_datagram);
	}
	
	private void receive(DatagramSocket socket) throws IOException, SocketTimeoutException
	{
		echo_datagram = PingFactory.instance().makeNewResponseReceiver();
		socket.receive(echo_datagram);
		echo_message_time = Instant.now();
	}
	
	private boolean isEchoEqualToPing()
	{
		if(echo_datagram.getLength() != ping_datagram.getLength())
		{
			return false;
		}

		for(int i = 0; i < ping_datagram.getLength(); i++)
		{
			if(ping_datagram.getData()[i] != echo_datagram.getData()[i])
			{
				return false;
			}
		}
		return true;
	}
	
	private void printSuccessfulPing()
	{
		System.out.println("Ping #" + sequence_number + ", Data: ["
			+ new String(ping_datagram.getData(), StandardCharsets.UTF_8)
			+ "], RTT: " + getRoundTripTimeMillis());
	}
	
	private void printFailedPing()
	{
		System.out.println("Ping #" + sequence_number + ", Data: ["
			+ new String(ping_datagram.getData(), StandardCharsets.UTF_8) + "], RTT: *");
	}
}
