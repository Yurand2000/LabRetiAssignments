package labRetiAssignments.ex09;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class EchoClient
{
	private final InetAddress address;
	private final int port;
	
	private SocketChannel socket;
	private ByteBuffer buffer;

	public EchoClient(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
		
		this.socket = null;
		this.buffer = null;
	}
	
	public void connect() throws IOException
	{
		socket = SocketChannel.open();
		buffer = ByteBuffer.allocate(1024);
		socket.connect(new InetSocketAddress(address, port));
	}
	
	public void sendAndReceive(String data) throws IOException
	{
		sendMessage(data.getBytes(StandardCharsets.UTF_8));
		displayMessage(readMessage());
	}
	
	public void close() throws IOException
	{
		socket.close();
		socket = null;
		buffer = null;
	}
	
	private void sendMessage(byte[] msg) throws IOException
	{
		buffer.putInt(msg.length);
		buffer.put(msg);
		buffer.flip();
		socket.write(buffer);
		buffer.clear();
	}
	
	private byte[] readMessage() throws IOException
	{
		int size = readInt();
		return readBytes(size);
	}
	
	private void displayMessage(byte[] data)
	{
		System.out.println("RECEIVED: " + new String(data, StandardCharsets.UTF_8));
	}
	
	private int readInt() throws IOException
	{
		readUntilBytes(Integer.BYTES);
		
		buffer.flip();
		int num = buffer.getInt();
		buffer.compact();
		return num;
	}
	
	private byte[] readBytes(int bytes) throws IOException
	{
		byte[] data = new byte[bytes];
		readUntilBytes(bytes);
		
		buffer.flip();
		buffer.get(data);
		buffer.compact();
		return data;
	}
	
	private void readUntilBytes(int bytes) throws IOException
	{
		while(buffer.position() < bytes)
		{
			socket.read(buffer);
		}
	}
}
