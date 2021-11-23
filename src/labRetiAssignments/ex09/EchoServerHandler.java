package labRetiAssignments.ex09;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class EchoServerHandler
{
	private SocketChannel channel;
	private ByteBuffer in_buffer, out_buffer;
	private int dataSize;
	
	public EchoServerHandler(SocketChannel channel)
	{
		this.channel = channel;
		this.in_buffer = ByteBuffer.allocate(512);
		this.out_buffer = ByteBuffer.allocate(512);
		this.dataSize = -1;
	}
	
	public void writeOperation(SelectionKey key) throws IOException
	{
		if(out_buffer.hasRemaining())
		{
			channel.write(out_buffer);
		}
		else
		{
			out_buffer.clear();
			key.interestOps(SelectionKey.OP_READ);
		}
	}
	
	public void readOperation(SelectionKey key) throws IOException
	{
		channel.read(in_buffer);
		if(dataSize < 0 && readAtLeastBytes(Integer.BYTES))			
		{
			in_buffer.flip();
			dataSize = in_buffer.getInt();
			in_buffer.compact();
		}		
		
		if(dataSize >= 0 && readAtLeastBytes(dataSize))
		{
			in_buffer.flip();
			byte[] echo_msg = new String("[ECHOED BY SERVER]").getBytes(StandardCharsets.UTF_8);
			out_buffer.putInt(dataSize + echo_msg.length);
			out_buffer.put(in_buffer);
			out_buffer.put(echo_msg);
			out_buffer.flip();
			in_buffer.clear();
			key.interestOps(SelectionKey.OP_WRITE);
			dataSize = -1;
		}
	}
	
	public boolean readAtLeastBytes(int bytes) throws IOException
	{
		return in_buffer.position() >= bytes;
	}
}
