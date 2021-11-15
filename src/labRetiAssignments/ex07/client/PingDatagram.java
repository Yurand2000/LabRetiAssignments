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
	boolean received_response;
	DatagramPacket ping_datagram;
	DatagramPacket echo_datagram;
	Instant ping_message_time;
	Instant echo_message_time;
	
	public PingDatagram(int sequence_number)
	{
		this.sequence_number = sequence_number;
		this.received_response = false;
	}
	
	public void trySendAndWaitForResponse(DatagramSocket socket, InetAddress address, int port) throws IOException
	{
		received_response = false;
		try
		{
			sendAndWaitForResponse(socket, address, port);
			received_response = true;
			
			printSuccessfulPing();
		}
		catch(SocketTimeoutException e)
		{
			printFailedPing();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public boolean receivedResponse()
	{
		return received_response;
	}
	
	public long getRoundTripTimeMillis()
	{
		if(received_response)
		{
			return echo_message_time.toEpochMilli() - ping_message_time.toEpochMilli();
		}
		else
		{
			throw new IllegalStateException();
		}
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
		ping_datagram = PingFactory.makeNewPingPacket(sequence_number, ping_message_time);
		ping_datagram.setAddress(address);
		ping_datagram.setPort(port);
		socket.send(ping_datagram);
	}
	
	private void receive(DatagramSocket socket) throws IOException, SocketTimeoutException
	{
		echo_datagram = PingFactory.makeNewResponsePacket();
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
		System.out.println(generatePartialPrintString() + getRoundTripTimeMillis());
	}
	
	private void printFailedPing()
	{
		System.out.println(generatePartialPrintString() + "*");
	}
	
	private String generatePartialPrintString()
	{
		return "Ping #" + sequence_number +
			"; Data: [" + new String(ping_datagram.getData(), 0, ping_datagram.getLength(), StandardCharsets.UTF_8) + "]; RTT: ";
	}
}
