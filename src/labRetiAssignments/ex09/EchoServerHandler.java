package labRetiAssignments.ex09;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class EchoServerHandler
{
	private static String echoed_by_server_string = "[ECHOED BY SERVER]::";
	
	private final SocketChannel channel;
	private final ByteBuffer in_buffer, out_buffer;
	private int dataSize, executedOperations;
	
	public EchoServerHandler(SocketChannel channel)
	{
		this.channel = channel;
		this.in_buffer = ByteBuffer.allocate(512);
		this.out_buffer = ByteBuffer.allocate(512);
		this.dataSize = -1;
		this.executedOperations = 0;
	}
	
	public void writeOperation(SelectionKey key) throws IOException
	{
		out_buffer.flip();
		if(out_buffer.hasRemaining())
		{
			channel.write(out_buffer);
			out_buffer.compact();
		}
		else
		{
			out_buffer.clear();
			key.interestOps(SelectionKey.OP_READ);
		}
	}
	
	public void readOperation(SelectionKey key) throws IOException
	{
		fillReadingBuffer();
		
		do
		{
			resetExecutedCounter();
			if(needsReadingMessageLength())
			{
				tryReadMessageLength();
			}
			else
			{
				tryReadMessage(key);
			}
		}
		while(hasBytesToRead() && hasAnyOperationBeenExecuted());
	}
	
	private void fillReadingBuffer() throws IOException
	{
		channel.read(in_buffer);
	}
	
	private boolean needsReadingMessageLength()
	{
		return dataSize < 0;
	}
	
	private void tryReadMessageLength() throws IOException
	{
		if(hasReadAtLeastBytes(Integer.BYTES))			
		{
			increaseExecutedCounter();
			in_buffer.flip();
			dataSize = in_buffer.getInt();
			in_buffer.compact();
		}
	}
	
	private void tryReadMessage(SelectionKey key) throws IOException
	{
		if(hasReadAtLeastBytes(dataSize))
		{
			increaseExecutedCounter();

			byte[] data = new byte[dataSize];
			in_buffer.flip();
			in_buffer.get(data);
			in_buffer.compact();
			
			byte[] echo_msg = echoed_by_server_string.getBytes(StandardCharsets.UTF_8);
			out_buffer.putInt(dataSize + echo_msg.length);
			out_buffer.put(echo_msg);
			out_buffer.put(data);
			key.interestOps(SelectionKey.OP_WRITE);
			dataSize = -1;
		}
	}
	
	private boolean hasReadAtLeastBytes(int bytes) throws IOException
	{
		return in_buffer.position() >= bytes;
	}
	
	private boolean hasBytesToRead() throws IOException
	{
		return in_buffer.position() > 0;
	}
	
	private boolean hasAnyOperationBeenExecuted()
	{
		return executedOperations > 0;
	}
	
	private void resetExecutedCounter()
	{
		executedOperations = 0;
	}
	
	private void increaseExecutedCounter()
	{
		executedOperations++;
	}
}
