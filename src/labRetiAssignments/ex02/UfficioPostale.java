package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class UfficioPostale {
	private LinkedBlockingDeque<Runnable> coda_esterna;
	private SalaInterna sala_interna;
	private int numero_sportelli = 1;
	private int lunghezza_coda_sala_interna = 10;
	
	public UfficioPostale()
	{
		coda_esterna = null;
		sala_interna = null;
	}
	
	public void entraUnNuovoCliente()
	{
		try {
			coda_esterna.putLast(new Cliente());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void apriUfficioPostale()
	{
		coda_esterna = new LinkedBlockingDeque<Runnable>();
		sala_interna = new SalaInterna(numero_sportelli, lunghezza_coda_sala_interna, coda_esterna);
	}
	
	public void chiudiUfficioPostale()
	{
		sala_interna.chiudiSalaInterna();
	}
}
