package labRetiAssignments.ex09;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.util.Iterator;

public class EchoServer implements Runnable
{
	private final InetAddress address;
	private final int port;
	
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
	
	public void startServer() throws IOException
	{
		listening_socket = ServerSocketChannel.open();
		selector = Selector.open();
		server_thread = new Thread(this);
		server_thread.start();
		
		System.out.println("Server started. Press ENTER to stop.");
	}
	
	public void stopServer() throws InterruptedException, IOException
	{
		System.out.println("Server stopping.");

		server_thread.interrupt();
		selector.wakeup();

		listening_socket.close();
		selector.close();

		server_thread.join();
		listening_socket = null;
		server_thread = null;
		selector = null;
	}

	@Override
	public void run()
	{
		try
		{
			setupServer();
			selectLoop();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void setupServer() throws IOException
	{
		listening_socket.socket().bind(new InetSocketAddress(address, port));
		listening_socket.configureBlocking(false);		
		listening_socket.register(selector, SelectionKey.OP_ACCEPT);
	}
	
	private void selectLoop() throws IOException
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
	
	private void tryExecuteIterator(SelectionKey key)
	{
		try
		{
			if(key.isAcceptable())
			{
				executeAcceptableKey(key);
			}
			else
			{
				executeReadWriteKey(key);
			}
		}
		catch (IOException e)
		{
			System.err.println("Error on handling client request: " + e.getMessage());
		}
	}
	
	private void executeAcceptableKey(SelectionKey key) throws IOException
	{
		SocketChannel new_channel = listening_socket.accept();
		new_channel.configureBlocking(false);
		
		SelectionKey new_key = new_channel.register(selector, SelectionKey.OP_READ);
		new_key.attach(new EchoServerHandler(new_channel));
	}
	
	private void executeReadWriteKey(SelectionKey key) throws IOException
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
			key.channel().close();
		}
	}
}
