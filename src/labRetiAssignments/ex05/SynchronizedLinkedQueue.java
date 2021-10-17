package labRetiAssignments.ex05;

import java.util.*;
import java.util.concurrent.locks.*;

public class SynchronizedLinkedQueue<T>
{
	private LinkedList<T> list;
	private Lock mutex;
	private Condition not_empty_condition;
	private Condition empty_condition;
	
	public SynchronizedLinkedQueue()
	{
		list = new LinkedList<T>();
		mutex = new ReentrantLock(true);
		not_empty_condition = mutex.newCondition();
		empty_condition = mutex.newCondition();
	}
	
	public void push(T elem)
	{
		mutex.lock();
		
		list.addLast(elem);
		not_empty_condition.signal();
		
		mutex.unlock();
	}
	
	public T pop() throws InterruptedException
	{
		T elem = null;	
		mutex.lock();
		
		try
		{
			while(list.isEmpty())
			{
				not_empty_condition.await();
			}
			elem = list.pollFirst();		
			
			if(list.isEmpty())
			{
				empty_condition.signal();
			}
		}
		catch(InterruptedException e) {
			mutex.unlock();
			throw e;
		}
		
		mutex.unlock();
		return elem;
	}
	
	public boolean isEmpty()
	{
		boolean cond = false;
		mutex.lock();
		
		cond = list.isEmpty();
		
		mutex.unlock();		
		return cond;
	}
	
	public void waitEmpty() throws InterruptedException
	{
		mutex.lock();
		
		try
		{
			while(!isEmpty())
			{
				empty_condition.await();
			}
		}
		catch(InterruptedException e) {
			mutex.unlock();
			throw e;
		}
		
		mutex.unlock();
	}
}
