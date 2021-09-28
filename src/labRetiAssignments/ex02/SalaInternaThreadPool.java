package labRetiAssignments.ex02;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class SalaInternaThreadPool extends ThreadPoolExecutor {
	int coda_interna_counter;
	int coda_interna_max;
	Lock counter_lock;
	Condition counter_lock_condition;
	
	public SalaInternaThreadPool(int sportelli, int lunghezza_coda_interna)
	{		
		super(sportelli, sportelli, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(lunghezza_coda_interna));
		coda_interna_counter = 0;
		coda_interna_max = lunghezza_coda_interna;
		counter_lock = new ReentrantLock();
		counter_lock_condition = counter_lock.newCondition();
	}
	
	@Override
	public void afterExecute(Runnable r, Throwable t)
	{
		counter_lock.lock();
		coda_interna_counter--;
		counter_lock_condition.signal();
		counter_lock.unlock();
		
		
		super.afterExecute(r, t);
	}
	
	public void executeBlocking(Runnable r)
	{
		counter_lock.lock();
		try
		{
			while(!(coda_interna_counter < coda_interna_max))
				counter_lock_condition.await();

			super.execute(r);
			coda_interna_counter++;
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		finally
		{
			counter_lock.unlock();
		}
	}
}
