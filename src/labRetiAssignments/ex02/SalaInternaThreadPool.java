package labRetiAssignments.ex02;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class SalaInternaThreadPool extends ThreadPoolExecutor {
	private int contatore_coda_interna;
	private int lunghezza_coda_interna;
	private Lock mutex_contatore;
	private Condition condition_variable_contatore;
	
	public SalaInternaThreadPool(int sportelli, int lunghezza_coda_interna)
	{		
		super(sportelli, sportelli, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(lunghezza_coda_interna));
		
		this.contatore_coda_interna = 0;
		this.lunghezza_coda_interna = lunghezza_coda_interna;
		this.mutex_contatore = new ReentrantLock();
		this.condition_variable_contatore = mutex_contatore.newCondition();
	}
	
	@Override
	public void afterExecute(Runnable r, Throwable t)
	{
		doWhileLocked( () ->
		{
			decrementaContatoreESegnala();
		});
		
		super.afterExecute(r, t);
	}
	
	public void executeBlocking(Runnable r)
	{
		doWhileLocked( () ->
		{
			try
			{
				while(codaInternaPiena())
					aspettaUnSegnaleSulMutex();

				super.execute(r);
				incrementaContatore();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
	}
	
	private void doWhileLocked(Runnable f)
	{
		mutex_contatore.lock();
		f.run();
		mutex_contatore.unlock();
	}
	
	private void decrementaContatoreESegnala()
	{
		contatore_coda_interna--;
		condition_variable_contatore.signal();
	}
	
	private void incrementaContatore()
	{
		contatore_coda_interna++;
	}
	
	private boolean codaInternaPiena()
	{
		return !(contatore_coda_interna < lunghezza_coda_interna);
	}
	
	private void aspettaUnSegnaleSulMutex() throws InterruptedException
	{
		condition_variable_contatore.await();
	}
}
