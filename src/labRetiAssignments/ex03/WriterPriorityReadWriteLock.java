package labRetiAssignments.ex03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class WriterPriorityReadWriteLock implements ReadWriteLock {
	final ReadLock readLock;
	final WriteLock writeLock;

	final Lock lock;
	final Condition readingCondition;
	final Condition writingCondition;
	
	int readersCount;
	int waitingWritersCount;
	boolean isWriterActive;
	
	public WriterPriorityReadWriteLock()
	{
		readLock = new ReadLock();
		writeLock = new WriteLock();
		
		lock = new ReentrantLock(true);
		readingCondition = lock.newCondition();
		writingCondition = lock.newCondition();
		
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
			lock.lock();
			while(isWriterActive || waitingWritersCount > 0)
				readingCondition.await();
			readersCount++;
			lock.unlock();
		}
		
		@Override
		public void unlock()
		{
			lock.lock();
			readersCount--;
			if(readersCount == 0)
				writingCondition.signal();
			lock.unlock();
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
			lock.lock();
			waitingWritersCount++;
			if(readersCount > 0 || isWriterActive)
				writingCondition.await();
			waitingWritersCount--;
			isWriterActive = true;
			lock.unlock();
		}

		@Override
		public void unlock()
		{
			lock.lock();
			isWriterActive = false;
			if(waitingWritersCount > 0)
				writingCondition.signal();
			else
				readingCondition.signalAll();
			lock.unlock();
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
