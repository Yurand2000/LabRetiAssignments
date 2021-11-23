package labRetiAssignments.ex09;

public class Message
{
	private byte[] data;
	
	public Message(int length)
	{
		data = new byte[length];
	}
	
	public Message(byte[] data)
	{
		this.data = data.clone();
	}
	
	public int getSize()
	{
		return data.length;
	}
	
	public byte[] getData()
	{
		return data;
	}
}
