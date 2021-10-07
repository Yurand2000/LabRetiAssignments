package lesson4.exs;

public class DropboxExtended extends Dropbox {

	private Object consumerLock = new Object();
	private Object producerLock = new Object();
	
	@Override
	public int take(boolean e)
	{
		int val;
		synchronized(consumerLock)
		{
			try
			{
				while(isEmptyAndEvenOdd(e))
					consumerLock.wait();
			}
			catch (InterruptedException e1) { e1.printStackTrace(); }

			synchronized(producerLock) { producerLock.notify(); }
			synchronized(this) { val = super.take(e); }
		}
		return val;
	}
	
	@Override
	public void put(int val)
	{
		synchronized(producerLock)
		{
			try
			{
				while(isFull())
					producerLock.wait();
			}
			catch (InterruptedException e1) { e1.printStackTrace(); }

			synchronized(consumerLock) { consumerLock.notifyAll(); }
			synchronized(this) { super.put(val); }
		}
	}
	
	private synchronized boolean isFull()
	{
		return full;
	}
	
	private synchronized boolean isEmptyAndEvenOdd(boolean even)
	{
		return !full || even != (num % 2 == 0);
	}
}
