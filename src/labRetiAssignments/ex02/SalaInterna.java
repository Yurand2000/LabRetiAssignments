package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class SalaInterna {
	SalaInternaThreadPool sportelli;
	
	public SalaInterna(int sportelli, int lunghezza_coda_interna, BlockingDeque<Runnable> coda_esterna)
	{
		this.sportelli = new SalaInternaThreadPool(sportelli, lunghezza_coda_interna, coda_esterna);
	}
	
	public void chiudiSalaInterna()
	{
		sportelli.shutdown();
	}
}
