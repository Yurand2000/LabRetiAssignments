package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class SpostaInSalaInternaRunnable implements Runnable {

	private BlockingQueue<Runnable> coda_esterna;
	private SalaInterna sala_interna;
	
	public SpostaInSalaInternaRunnable(BlockingQueue<Runnable> coda_esterna, SalaInterna sala_interna)
	{
		this.coda_esterna = coda_esterna;
		this.sala_interna = sala_interna;
	}

	@Override
	public void run() {
		try {
			while(!coda_esterna.isEmpty())
			{
				Runnable r = coda_esterna.take();
				sala_interna.aggiungiInSalaInternaBloccante(r);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
