package labRetiAssignments.ex04;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class LockUsingMonitor implements Lock
{
	private boolean locked = false;
	@Override
	public void lock()
	{
		try
		{
			lockInterruptibly();
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}	
	}

	@Override
	public synchronized void lockInterruptibly() throws InterruptedException
	{
		while(locked == true)
			this.wait();
		locked = true;
	}

	@Override
	public synchronized void unlock() {
		locked = false;
		this.notify();
	}

	@Override
	public Condition newCondition()
	{
		return new ConditionUsingMonitor();
	}
	
	private class ConditionUsingMonitor implements Condition
	{
		@Override
		public void awaitUninterruptibly()
		{
			try
			{
				await();
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void await() throws InterruptedException
		{
			unlock();
			synchronized(this) { this.wait(); }
			lock();
		}

		@Override
		public synchronized void signal() {
			this.notify();
		}

		@Override
		public synchronized void signalAll() {
			this.notifyAll();
		}
		
		//NOT IMPLEMENTED FUNCTIONS OF CONDITION INTERFACE -------------------------
		@Override
		public long awaitNanos(long nanosTimeout) throws InterruptedException {
			return 0;
		}

		@Override
		public boolean await(long time, TimeUnit unit) throws InterruptedException {
			return false;
		}

		@Override
		public boolean awaitUntil(Date deadline) throws InterruptedException {
			return false;
		}		
	}
	
	//NOT IMPLEMENTED FUNCTIONS OF LOCK INTERFACE ----------------------------------
	@Override
	public boolean tryLock()
	{
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException
	{
		return false;
	}

}
