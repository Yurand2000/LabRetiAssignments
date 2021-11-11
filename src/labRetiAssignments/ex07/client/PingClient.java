package labRetiAssignments.ex07.client;

import java.io.IOException;
import java.net.*;

public class PingClient
{
	private static int timeout = 2000;
	private DatagramSocket socket;
	private InetAddress destination_address;
	private int destination_port;
	
	private int sent_pings, received_echos;
	private long min_time, max_time, total_time;
	
	public PingClient(InetAddress destination_address, int destination_port)
	{
		this.destination_address = destination_address;
		this.destination_port = destination_port;
		resetStatistics();
	}
	
	public void sendPings(int number_of_pings) throws IOException
	{
		try
		{
			socket = new DatagramSocket();
			socket.setSoTimeout(timeout);
			
			for(int i = 0; i < number_of_pings; i++)
			{
				PingDatagram ping = new PingDatagram(i);
				if(ping.trySendAndWaitForResponse(socket, destination_address, destination_port))
				{
					long rtt = ping.getRoundTripTimeMillis();
					updateRTTStatistics(rtt);
					received_echos++;
				}
				sent_pings++;
			}
			
			socket.close();
			
			printStatistics();
			resetStatistics();
		}
		catch(Exception e)
		{
			socket.close();
			throw e;
		}
	}
	
	private void updateRTTStatistics(long new_message_rtt)
	{

		if(new_message_rtt > max_time)
		{
			max_time = new_message_rtt;
		}
		
		if(new_message_rtt < min_time)
		{
			min_time = new_message_rtt;
		}
		
		total_time += new_message_rtt;
	}
	
	private void resetStatistics()
	{
		sent_pings = 0;
		received_echos = 0;
		min_time = Integer.MAX_VALUE;
		max_time = 0;
		total_time = 0;
	}
	
	private void printStatistics()
	{
		System.out.println("- - - - PING STATISTICS - - - -");
		System.out.println(
			String.format("%d packet(s) transmitted, %d packet(s) received, %.0f%% packet loss.",
				sent_pings, received_echos, (float)(sent_pings - received_echos)*(100.f)/(float)(sent_pings) ));
		
		if(received_echos > 0)
		{
			System.out.println(
				String.format("round-trip (ms) min/avg/max = %d/%.2f/%d",
					min_time, (float)(total_time)/(float)(received_echos), max_time ));
		}
		else
		{
			System.out.println("round-trip (ms) min/avg/max = */*/*");
		}
	}
}
