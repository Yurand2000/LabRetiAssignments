package lesson4.exs;

public class Consumer implements Runnable
{
	private Dropbox dropbox;
	private boolean even;
	
	public Consumer(Dropbox dropbox, boolean take_even)
	{
		this.dropbox = dropbox;
		this.even = take_even;
	}

	@Override
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			dropbox.take(even);
		}
	}
}
