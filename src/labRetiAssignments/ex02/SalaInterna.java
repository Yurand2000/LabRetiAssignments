package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class SalaInterna {
	int numero_sportelli;
	ThreadPoolExecutor sportelli;
	BlockingQueue<Runnable> coda_interna;
	
	public SalaInterna(int sportelli, ArrayBlockingQueue<Runnable> coda_interna)
	{
		this.numero_sportelli = sportelli;
		this.coda_interna = coda_interna;
		this.sportelli = null;
	}
	
	public void apriSalaInterna()
	{
		this.sportelli = new ThreadPoolExecutor(numero_sportelli, numero_sportelli, 1, TimeUnit.HOURS, coda_interna);
	}
	
	public void accodaInSalaInterna(Runnable cliente) throws InterruptedException
	{
		coda_interna.put(cliente);
	}
	
	public void chiudiSalaInterna()
	{
		if(sportelli != null)
			sportelli.shutdown();
	}
}
