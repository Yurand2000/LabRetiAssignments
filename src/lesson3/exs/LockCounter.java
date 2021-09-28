package lesson3.exs;

import java.util.concurrent.locks.*;

public class LockCounter extends UnsafeCounter {
	private Lock lock;
	
	public LockCounter()
	{
		lock = new ReentrantLock();
	}
	
	public void increment()
	{
		lock.lock();
		super.increment();
		lock.unlock();
	}
	
	public int get()
	{
		int temp = 0;
		
		lock.lock();
		temp = super.get();
		lock.unlock();
		
		return temp;
	}
}
