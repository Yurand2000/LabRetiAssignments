package lesson3.exs;

public class Writer implements Runnable {

	private Counter counter;
	
	public Writer(Counter c)
	{
		counter = c;
	}
	
	@Override
	public void run() {
		counter.increment();
	}
}
