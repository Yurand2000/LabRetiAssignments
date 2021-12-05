package labRetiAssignments.ex10;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MulticastServer implements Runnable
{
	private static final int sleep_time = 2000;
	
	private Thread thread;
	private MulticastSocket socket;
	private InetAddress address;
	private int port;	
	
	public MulticastServer(InetAddress multicast_address, int multicast_port)
	{
		address = multicast_address;
		port = multicast_port;
		socket = null;
	}

	public void startServer()
	{
		thread = new Thread(this);
		thread.start();
		System.out.println("Server started...");
	}
	
	public void stopServer() throws InterruptedException
	{
		System.out.println("Server stopping...");
		thread.interrupt();
		thread.join();
	}
	
	@Override
	public void run()
	{
		try
		{
			openSocket();
			sendPackets();
			closeSocket();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e) { }
	}
	
	private void openSocket() throws IOException
	{
		socket = new MulticastSocket();
		socket.setTimeToLive(1);
	}
	
	private void sendPackets() throws IOException, InterruptedException
	{
		ZoneId timezone = ZoneId.of("GMT");
		
		while(!Thread.currentThread().isInterrupted())
		{
			byte[] data = LocalDateTime.now(timezone).toString().getBytes(StandardCharsets.UTF_8);
			DatagramPacket dp = new DatagramPacket(data, data.length, address, port);
			
			socket.send(dp);
			Thread.sleep(sleep_time);
		}
	}
	
	private void closeSocket()
	{
		socket.close();
		socket = null;
	}
}
