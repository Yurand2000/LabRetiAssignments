package lesson3.exs;

public class Reader implements Runnable {

	private Counter counter;
	
	public Reader(Counter c)
	{
		counter = c;
	}
	
	@Override
	public void run() {
		System.out.println(counter.get());
	}
}
