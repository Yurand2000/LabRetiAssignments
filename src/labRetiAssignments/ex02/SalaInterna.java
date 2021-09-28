package labRetiAssignments.ex02;


public class SalaInterna {
	SalaInternaThreadPool sportelli;
	
	public SalaInterna(int sportelli, int lunghezza_coda_interna)
	{
		this.sportelli = new SalaInternaThreadPool(sportelli, lunghezza_coda_interna);
	}
	
	public void aggiungiInSalaInternaBloccante(Runnable r)
	{
		sportelli.executeBlocking(r);
	}
	
	public void chiudiSalaInterna()
	{
		sportelli.shutdown();
	}
}
