package labRetiAssignments.ex03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class WriterPriorityReadWriteLock implements ReadWriteLock {
	ReadLock readLock;
	WriteLock writeLock;
	
	Lock lock;
	Condition readingCondition;
	Condition writingCondition;
	int readingThreadsNumber = 0;
	int writerWaiting = 0;
	boolean writingThreadActive = false;
	
	public WriterPriorityReadWriteLock()
	{
		readLock = new ReadLock(this);
		writeLock = new WriteLock(this);
		lock = new ReentrantLock(true);
		readingCondition = lock.newCondition();
		writingCondition = lock.newCondition();
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
		WriterPriorityReadWriteLock main_lock;
		
		public ReadLock(WriterPriorityReadWriteLock main_lock)
		{
			this.main_lock = main_lock;
		}
		
		@Override
		public void lock()
		{
			try { lockInterruptibly(); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}

		@Override
		public void lockInterruptibly() throws InterruptedException
		{
			main_lock.lock.lock();
			while(writingThreadActive || writerWaiting > 0)
				readingCondition.await();
			readingThreadsNumber++;
			main_lock.lock.unlock();
		}

		@Override
		public void unlock()
		{
			main_lock.lock.lock();
			readingThreadsNumber--;
			if(readingThreadsNumber == 0)
				writingCondition.signal();
			main_lock.lock.unlock();
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
		WriterPriorityReadWriteLock main_lock;
		
		public WriteLock(WriterPriorityReadWriteLock main_lock)
		{
			this.main_lock = main_lock;
		}
		@Override
		public void lock() {
			try { lockInterruptibly(); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			main_lock.lock.lock();
			writerWaiting++;
			if(readingThreadsNumber > 0 || writingThreadActive)
				writingCondition.await();
			writerWaiting--;
			writingThreadActive = true;
			main_lock.lock.unlock();			
		}

		@Override
		public void unlock() {
			main_lock.lock.lock();
			writingThreadActive = false;
			if(writerWaiting > 0)
				writingCondition.signal();
			else
				readingCondition.signalAll();
			main_lock.lock.unlock();
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
