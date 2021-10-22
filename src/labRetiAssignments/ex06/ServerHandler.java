package labRetiAssignments.ex06;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class ServerHandler implements Runnable
{
	String cwd;
	ServerSocket listening_socket;
	Thread listening_thread;
	ThreadPoolExecutor thread_pool;
	
	public ServerHandler(String cwd) throws IOException
	{
		this.cwd = cwd;
		listening_thread = null;
		listening_socket = new ServerSocket();
		thread_pool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
	}
	
	public void start() throws IOException
	{
		System.out.println("Starting Server");
		listening_thread = new Thread(this);
		listening_thread.start();
	}
	
	public void stop() throws IOException, InterruptedException
	{
		System.out.println("Stopping Server");
		listening_socket.close();
		listening_thread.join();
	}

	@Override
	public void run()
	{
		try
		{
			listening_socket.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), 8081));		
			while(!listening_socket.isClosed())
			{
				Socket connection = listening_socket.accept();
				thread_pool.execute(new ConnectionHandler(cwd, connection));
			}
		}
		catch (SocketException e) { }
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
