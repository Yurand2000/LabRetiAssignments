package labRetiAssignments.ex07.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import labRetiAssignments.ex07.PingFactory;

public class PingServer implements Runnable
{
	private DatagramSocket socket;
	private InetAddress server_address;
	private int server_port;
	private int min_latency;
	private int diff_latency;
	private float loss_rate;
	private Thread listener_thread;
	
	public PingServer(InetAddress address, int port, int min_latency, int max_latency, float loss_rate)
	{
		this.socket = null;
		this.server_address = address;
		this.server_port = port;
		this.min_latency = min_latency;
		this.diff_latency = max_latency - min_latency;
		this.loss_rate = loss_rate;
		this.listener_thread = null;
	}

	public void startServer()
	{
		listener_thread = new Thread(this);
		listener_thread.start();
		System.out.println("Server Started");
	}
	
	public void stopServer() throws InterruptedException
	{
		if(socket != null)
		{
			socket.close();
		}

		listener_thread.interrupt();
		listener_thread.join();
		System.out.println("Server Stopped");
	}
	
	@Override
	public void run()
	{
		try
		{
			socket = new DatagramSocket(server_port, server_address);
			
			while(!Thread.currentThread().isInterrupted())
			{
				listenAndEcho();
			}
			
			socket.close();
		}
		catch (Exception e)
		{
			socket.close();
			e.printStackTrace();
		}
	}
	
	private void listenAndEcho() throws IOException
	{
		try
		{
			DatagramPacket ping = PingFactory.instance().makeNewResponseReceiver();
			socket.receive(ping);
			
			if(!doDiscard())
			{
				int delay = generateRandomDelay();
				Thread.sleep(delay);
				socket.send(ping);
				printEchoedMessage(ping, delay);
			}
			else
			{
				printNonEchoedMessage(ping);
			}
			
		}
		catch (InterruptedException e) { }
		catch (SocketException e) { }
	}
	
	private boolean doDiscard()
	{
		return Math.random() <= loss_rate;
	}
	
	private int generateRandomDelay()
	{
		return (int) ((Math.random() * diff_latency) + min_latency);
	}
	
	private void printEchoedMessage(DatagramPacket ping, int delay)
	{
		byte[] data = Arrays.copyOf(ping.getData(), ping.getLength());
		System.out.println("Received: [" + new String(data, StandardCharsets.UTF_8) + "] Action: delayed " + delay + " ms");
	}
	
	private void printNonEchoedMessage(DatagramPacket ping)
	{
		byte[] data = Arrays.copyOf(ping.getData(), ping.getLength());
		System.out.println("Received: [" + new String(data, StandardCharsets.UTF_8) + "] Action: not echoed");
	}
}
