package lesson4.exs;

public class Producer implements Runnable
{
	private Dropbox dropbox;
	
	public Producer(Dropbox dropbox)
	{
		this.dropbox = dropbox;
	}

	@Override
	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			dropbox.put((int)(Math.random() * 100));
		}
	}

}
