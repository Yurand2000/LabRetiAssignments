package lesson3.exs;

import java.util.concurrent.locks.*;

public class ReadWriteLockCounter extends UnsafeCounter {
	private ReadWriteLock lock;
	
	public ReadWriteLockCounter()
	{
		lock = new ReentrantReadWriteLock();
	}
	
	public void increment()
	{
		Lock l = lock.writeLock();
		l.lock();
		super.increment();
		l.unlock();
	}
	
	public int get()
	{
		int temp = 0;

		Lock l = lock.readLock();
		l.lock();
		temp = super.get();
		l.unlock();
		
		return temp;
	}
}
