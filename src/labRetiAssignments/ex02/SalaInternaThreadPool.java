package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class SalaInternaThreadPool extends ThreadPoolExecutor {
	BlockingDeque<Runnable> coda_esterna;
	Thread thread_riempi_fila;
	
	public SalaInternaThreadPool(int sportelli, int lunghezza_coda_interna, BlockingDeque<Runnable> coda_esterna)
	{		
		super(sportelli, sportelli, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(lunghezza_coda_interna));
		this.coda_esterna = coda_esterna;
		this.thread_riempi_fila = new Thread(new RiempiFilaInternaRunnable(coda_esterna, this));
		this.thread_riempi_fila.start();
	}
	
	@Override
	public void beforeExecute(Thread t, Runnable r)
	{
		Runnable riempi_fila_runnable = new RiempiFilaInternaRunnable(coda_esterna, this);
		if(coda_esterna.isEmpty())
		{
			thread_riempi_fila = new Thread(riempi_fila_runnable);
			thread_riempi_fila.start();
		}
		else
		{
			riempi_fila_runnable.run();
		}
		
		super.beforeExecute(t, r);
	}
}
