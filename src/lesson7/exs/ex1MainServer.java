package lesson7.exs;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ex1MainServer
{
	private static String remoteAddrName = "localhost";
	private static int remotePort = 4040;

	public static void main(String[] args) throws SocketException, UnknownHostException, IOException
	{
		InetAddress remoteAddr = InetAddress.getByName(remoteAddrName);
		DatagramSocket socket = new DatagramSocket(remotePort, remoteAddr);
		
		byte[] ping_msg = new byte[256];
		DatagramPacket ping = new DatagramPacket(ping_msg, ping_msg.length);
		
		int count = 0;
		while(count < 10)
		{
			Arrays.fill(ping_msg, (byte)(0));
			while(!new String(ping.getData(), StandardCharsets.UTF_8).contains("Ping"))
			{
				socket.receive(ping);
				System.out.println(ping.getSocketAddress());
				System.out.println(new String(ping.getData(), StandardCharsets.UTF_8));
			}

			byte[] pong_msg = ("Pong").getBytes(StandardCharsets.UTF_8);
			DatagramPacket pong = new DatagramPacket(pong_msg, pong_msg.length, ping.getAddress(), ping.getPort());
			
			socket.send(pong);
			count++;
		}
		socket.close();
	}

}
