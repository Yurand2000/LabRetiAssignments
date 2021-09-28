package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class UfficioPostale {
	private BlockingQueue<Runnable> coda_esterna;
	private SalaInterna sala_interna;
	private int numero_sportelli = 4;
	private int lunghezza_coda_sala_interna = 10;
	private Thread riempi_sala_interna;
	private boolean ufficio_aperto = false;
	
	public UfficioPostale()
	{
		coda_esterna = null;
		sala_interna = null;
		riempi_sala_interna = null;
	}
	
	public void entraUnNuovoCliente()
	{
		if(!ufficio_aperto)
			return;
		
		try {
			coda_esterna.put(new Cliente());
			if(riempi_sala_interna == null || !riempi_sala_interna.isAlive())
			{
				riempi_sala_interna = new Thread(new SpostaInSalaInternaRunnable(coda_esterna, sala_interna));
				riempi_sala_interna.start();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void apriUfficioPostale()
	{
		ufficio_aperto = true;
		coda_esterna = new LinkedBlockingDeque<Runnable>();
		sala_interna = new SalaInterna(numero_sportelli, lunghezza_coda_sala_interna);
	}
	
	public void chiudiUfficioPostale()
	{
		bloccaAccessoANuoviClienti();
		aspettaCheSianoUscitiIClienti();
		sala_interna.chiudiSalaInterna();
	}
	
	private void bloccaAccessoANuoviClienti()
	{
		ufficio_aperto = false;
	}
	
	private void aspettaCheSianoUscitiIClienti()
	{
		while(!coda_esterna.isEmpty())
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
