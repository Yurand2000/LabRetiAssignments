package lesson7.exs;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ex1MainClient {

	private static int wait_timer = 10;
	private static String remoteAddrName = "localhost";
	private static int remotePort = 4040;
	
	public static void main(String[] args) throws SocketException, UnknownHostException, IOException
	{
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(wait_timer * 1000);
		
		InetAddress remoteAddr = InetAddress.getByName(remoteAddrName);
		byte[] ping_msg = ("Ping").getBytes(StandardCharsets.UTF_8);
		DatagramPacket ping = new DatagramPacket(ping_msg, ping_msg.length, remoteAddr, remotePort);
		byte[] pong_msg = new byte[256];
		DatagramPacket pong = new DatagramPacket(pong_msg, pong_msg.length);
		
		try
		{
			socket.send(ping);	
			socket.receive(pong);
			System.out.println("Pong received!");
		}
		catch(SocketTimeoutException e)
		{
			System.out.println("Pong not received!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		socket.close();
	}

}
