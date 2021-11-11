package labRetiAssignments.ex07;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.*;

public class PingFactory
{
	private static PingFactory instance = new PingFactory();
	
	private PingFactory() { }
	
	public static PingFactory instance()
	{
		return instance;
	}
	
	public DatagramPacket makeNewPing(int sequence_number, Instant timestamp)
	{
		StringBuilder ping_msg = new StringBuilder("PING ");
		ping_msg.append(sequence_number);
		ping_msg.append(' ');
		ping_msg.append(timestamp);
		byte[] data = ping_msg.toString().getBytes(StandardCharsets.UTF_8);
		
		DatagramPacket ping = new DatagramPacket(data, data.length);
		return ping;
	}
	
	public DatagramPacket makeNewResponseReceiver()
	{
		byte[] data = new byte[256];
		DatagramPacket response = new DatagramPacket(data, data.length);
		return response;
	}
}
