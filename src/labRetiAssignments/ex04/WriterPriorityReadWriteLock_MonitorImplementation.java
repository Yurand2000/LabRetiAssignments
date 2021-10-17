package labRetiAssignments.ex04;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class WriterPriorityReadWriteLock_MonitorImplementation implements ReadWriteLock {
	final ReadLock readLock;
	final WriteLock writeLock;

	final Object lock;
	final Object readingCondition;
	final Object writingCondition;
	
	int readersCount;
	int waitingWritersCount;
	boolean isWriterActive;
	
	public WriterPriorityReadWriteLock_MonitorImplementation()
	{
		readLock = new ReadLock();
		writeLock = new WriteLock();
		
		lock = new Object();
		readingCondition = new Object();
		writingCondition = new Object();
		
		readersCount = 0;
		waitingWritersCount = 0;
		isWriterActive = false;
	}
	
	@Override
	public Lock readLock()
	{
		return readLock;
	}

	@Override
	public Lock writeLock() {
		return writeLock;
	}
	
	private class ReadLock implements Lock
	{
		@Override
		public void lock()
		{
			try { lockInterruptibly(); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}

		@Override
		public void lockInterruptibly() throws InterruptedException
		{
			while(consumerWait())
			{
				synchronized(readingCondition) { readingCondition.wait(); }
			}
			increaseReadersCount();
		}
		
		private boolean consumerWait()
		{
			boolean do_wait = false;
			synchronized(lock)
			{
				do_wait = isWriterActive || waitingWritersCount > 0;
			}
			return do_wait;
		}
		
		private void increaseReadersCount()
		{
			synchronized(lock)
			{
				readersCount++;
			}
		}
		
		@Override
		public void unlock()
		{
			synchronized(lock)
			{
				readersCount--;
				if(readersCount == 0)
				{
					signalNoMoreReaders();
				}
			}
		}

		private void signalNoMoreReaders()
		{
			synchronized(writingCondition)
			{
				writingCondition.notify();
			}
		}

		//NOT IMPLEMENTED FUNCTIONS OF LOCK INTERFACE
		@Override
		public boolean tryLock() {
			return false;
		}
		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			return false;
		}
		@Override
		public Condition newCondition() {
			throw new RuntimeException("Not Implemented");
		}
	}
	
	private class WriteLock implements Lock
	{
		@Override
		public void lock()
		{
			try { lockInterruptibly(); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}

		@Override
		public void lockInterruptibly() throws InterruptedException
		{
			increaseWritersInQueueCount();
			while(producerWait())
			{
				synchronized(writingCondition) { writingCondition.wait(); }
			}
			decreaseWritersInQueueCount();
		}
		
		private void increaseWritersInQueueCount()
		{
			synchronized(lock)
			{
				waitingWritersCount++;
			}
		}
		
		private boolean producerWait()
		{
			boolean do_wait = false;
			synchronized(lock)
			{
				do_wait = readersCount > 0 || isWriterActive;
			}
			return do_wait;
		}
		
		private void decreaseWritersInQueueCount()
		{
			synchronized(lock)
			{
				waitingWritersCount--;
				isWriterActive = true;
			}
		}

		@Override
		public void unlock()
		{
			synchronized(lock)
			{
				isWriterActive = false;
				if(waitingWritersCount > 0)
					signalNextWriter();
				else
					signalAllReaders();
			}
		}
		
		private void signalNextWriter()
		{
			synchronized(writingCondition)
			{
				writingCondition.notify();
			}
		}
		
		private void signalAllReaders()
		{
			synchronized(readingCondition)
			{
				readingCondition.notifyAll();
			}
		}

		//NOT IMPLEMENTED FUNCTIONS OF LOCK INTERFACE
		@Override
		public boolean tryLock() {
			return false;
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			return false;
		}
		
		@Override
		public Condition newCondition() {
			throw new RuntimeException("Not Implemented");
		}
	}
}
