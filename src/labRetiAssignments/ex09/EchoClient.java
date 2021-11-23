package labRetiAssignments.ex09;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class EchoClient
{
	private SocketChannel socket;
	private ByteBuffer buffer;
	
	private InetAddress address;
	private int port;

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
		sendMessage(new Message(data.getBytes(StandardCharsets.UTF_8)));
		System.out.println("RECEIVED: " + new String(readMessage().getData(), StandardCharsets.UTF_8));
	}
	
	public void close() throws IOException
	{
		socket.close();
		socket = null;
		buffer = null;
	}
	
	private void sendMessage(Message msg) throws IOException
	{
		buffer.putInt(msg.getSize());
		buffer.put(msg.getData());
		buffer.flip();
		socket.write(buffer);
		buffer.clear();
	}
	
	private Message readMessage() throws IOException
	{
		int size = readInt();
		return new Message(readBytes(size));
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
		buffer.clear();
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
