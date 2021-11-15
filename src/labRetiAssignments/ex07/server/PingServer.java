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
	private Thread listener_thread;
	
	private int server_port, min_latency, diff_latency;
	private float loss_rate;
	
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
		closeSocket();		
		listener_thread.interrupt();
		listener_thread.join();
		System.out.println("Server Stopped");
	}
	
	@Override
	public void run()
	{
		try
		{
			openSocket();
			while(!Thread.currentThread().isInterrupted())
			{
				listenOneMessageAndEcho();
			}
			closeSocket();
		}
		catch (Exception e)
		{
			closeSocket();
			e.printStackTrace();
		}
	}
	
	private void openSocket() throws SocketException
	{
		socket = new DatagramSocket(server_port, server_address);
	}
	
	private void closeSocket()
	{
		if(socket != null)
		{
			socket.close();
			socket = null;
		}
	}
	
	private void listenOneMessageAndEcho() throws IOException
	{
		try
		{
			DatagramPacket ping = PingFactory.makeNewResponsePacket();
			socket.receive(ping);
			
			if(doDiscard())
			{
				printNonEchoedMessage(ping);
			}
			else
			{
				int delay = generateRandomDelay();
				Thread.sleep(delay);
				socket.send(ping);
				printEchoedMessage(ping, delay);
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
		System.out.println(generatePartialPrintString(ping) + "delayed " + delay + " ms.");
	}
	
	private void printNonEchoedMessage(DatagramPacket ping)
	{
		System.out.println(generatePartialPrintString(ping) + "not echoed.");
	}
	
	private String generatePartialPrintString(DatagramPacket ping)
	{
		return
			"From: " + ping.getAddress().getHostAddress() + ":" + ping.getPort() + "; Received: [" +
			new String(ping.getData(), 0, ping.getLength(), StandardCharsets.UTF_8)
			+ "]; Action: ";
	}
}
