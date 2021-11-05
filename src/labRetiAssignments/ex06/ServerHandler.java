package labRetiAssignments.ex06;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

public class ServerHandler implements Runnable
{
	String current_working_dir;
	InetSocketAddress listening_address;
	ServerSocket listening_socket;
	Thread listening_thread;
	ThreadPoolExecutor thread_pool;
	
	public ServerHandler(String cwd, String address, int port) throws IOException
	{
		current_working_dir = cwd;
		listening_address = new InetSocketAddress(address, port);
		listening_thread = null;
		listening_socket = new ServerSocket();
		thread_pool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
	}
	
	public void start() throws IOException
	{
		listening_thread = new Thread(this);
		listening_thread.start();
		System.out.println("** Server started. **");
	}
	
	public void stop() throws IOException, InterruptedException
	{
		listening_socket.close();
		listening_thread.join();
		thread_pool.shutdown();
		System.out.println("** Server stopped. **");
	}

	@Override
	public void run()
	{
		try
		{
			listening_socket.bind(listening_address);		
			while(!listening_socket.isClosed())
			{
				Socket connection = listening_socket.accept();
				thread_pool.execute(new ConnectionHandler(current_working_dir, connection));
			}
		}
		catch (SocketException e) { }
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
