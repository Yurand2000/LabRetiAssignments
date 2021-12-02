package labRetiAssignments.ex10;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TimeServerMain
{
	private static int sleep_time = 2000;
	private static String multicast_address = "239.255.1.3";
	private static int multicast_port = 8082;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		MulticastSocket s = new MulticastSocket();
		s.setTimeToLive(1);
		
		while(!Thread.currentThread().isInterrupted())
		{
			byte[] data = LocalDateTime.now().toString().getBytes(StandardCharsets.UTF_8);
			DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(multicast_address), multicast_port);
			s.send(dp);
			Thread.sleep(sleep_time);
		}
		
		s.close();
	}

}
