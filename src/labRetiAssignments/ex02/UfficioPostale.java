package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class UfficioPostale {
	private int numero_sportelli = 4;
	private int lunghezza_coda_sala_interna = 10;

	private boolean ufficio_aperto = false;
	private int numero_prossimo_cliente = 1;
	private BlockingQueue<Runnable> coda_esterna;
	private SalaInterna sala_interna;
	private Thread riempi_sala_interna;
	
	public UfficioPostale()
	{
		coda_esterna = null;
		sala_interna = null;
		riempi_sala_interna = null;
	}
	
	public void apriUfficioPostale()
	{
		ufficio_aperto = true;
		coda_esterna = new LinkedBlockingQueue<Runnable>();
		sala_interna = new SalaInterna(numero_sportelli, lunghezza_coda_sala_interna);
	}
	
	public void entraUnNuovoCliente()
	{
		if(!ufficio_aperto)
			throw new RuntimeException("Ufficio Postale Chiuso");
		
		try
		{
			accodaNuovoCliente();
			if(threadRiempiSalaInternaNonAttivo())
			{
				attivaThreadRiempiSalaInterna();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void chiudiUfficioPostale()
	{
		bloccaAccessoANuoviClienti();
		aspettaClientiDaAccodareInSalaInterna();
		sala_interna.processaUltimiClientiEChiudiSalaInterna();
	}
	
	private void accodaNuovoCliente() throws InterruptedException
	{
		coda_esterna.put(nuovoCliente());
	}
	
	private Cliente nuovoCliente()
	{
		Cliente c = new Cliente(numero_prossimo_cliente);
		numero_prossimo_cliente++;
		return c;
	}
	
	private boolean threadRiempiSalaInternaNonAttivo()
	{
		return riempi_sala_interna == null || !riempi_sala_interna.isAlive();
	}
	
	private void attivaThreadRiempiSalaInterna()
	{
		riempi_sala_interna = new Thread(
				new SpostaInSalaInternaRunnable(coda_esterna, sala_interna));
		riempi_sala_interna.start();
	}
	
	private void bloccaAccessoANuoviClienti()
	{
		ufficio_aperto = false;
	}
	
	private void aspettaClientiDaAccodareInSalaInterna()
	{
		while(!coda_esterna.isEmpty())
		{
			try {
				Thread.sleep(500); //sleep per non aspettare su una variabile di condizione
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
