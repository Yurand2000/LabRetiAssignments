package labRetiAssignments.ex09;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.util.Iterator;

public class EchoServer implements Runnable
{
	private InetAddress address;
	private int port;
	
	private ServerSocketChannel listening_socket;
	private Selector selector;
	private Thread server_thread;

	public EchoServer(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
		
		this.listening_socket = null;
		this.selector = null;
		this.server_thread = null;
	}
	
	public void startServer()
	{
		server_thread = new Thread(this);
		server_thread.start();
		
		System.out.println("Server started...");
	}
	
	public void stopServer() throws InterruptedException, IOException
	{
		System.out.println("Server stopping...");

		server_thread.interrupt();
		selector.wakeup();
		
		if(listening_socket != null)
		{
			listening_socket.close();
			listening_socket = null;
		}
		
		if(selector != null)
		{
			selector.close();
			selector = null;
		}
		
		server_thread.join();
		server_thread = null;		
	}

	@Override
	public void run()
	{
		setupServer();
		selectLoop();
	}
	
	private void setupServer()
	{
		try
		{
			listening_socket = ServerSocketChannel.open();
			listening_socket.socket().bind(new InetSocketAddress(address, port));
			listening_socket.configureBlocking(false);
			
			selector = Selector.open();
			listening_socket.register(selector, SelectionKey.OP_ACCEPT);			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void selectLoop()
	{
		try
		{
			while(!Thread.currentThread().isInterrupted())
			{
				int ready_keys = selector.select();
				if(ready_keys != 0)
				{
					Iterator<SelectionKey> keys_iterator = selector.selectedKeys().iterator();
					while(keys_iterator.hasNext())
					{
						tryExecuteIterator(keys_iterator.next());
						keys_iterator.remove();
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void tryExecuteIterator(SelectionKey key)
	{
		try
		{
			executeIterator(key);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void executeIterator(SelectionKey key) throws IOException
	{
		if(key.isAcceptable())
		{
			executeAcceptableKey(key);
		}
		else
		{
			try
			{
				EchoServerHandler handler = (EchoServerHandler)key.attachment();
				if(key.isReadable())
				{
					handler.readOperation(key);
				}
				if(key.isWritable())
				{
					handler.writeOperation(key);
				}
			}
			catch (IOException e)
			{
				SocketChannel channel = (SocketChannel)key.channel();
				channel.close();
			}
		}
	}
	
	private void executeAcceptableKey(SelectionKey key) throws IOException
	{
		SocketChannel new_channel = ((ServerSocketChannel)key.channel()).accept();
		new_channel.configureBlocking(false);
		SelectionKey new_key = new_channel.register(selector, SelectionKey.OP_READ);
		new_key.attach(new EchoServerHandler(new_channel));
	}
}
