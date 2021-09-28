package lesson3.exs;

public class UnsafeCounter implements Counter {
	private int counter;
	
	public UnsafeCounter()
	{
		counter = 0;
	}
	
	public void increment()
	{
		counter++;
	}
	
	public int get()
	{		
		return counter;
	}
}
