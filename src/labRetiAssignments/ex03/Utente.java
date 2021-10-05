package labRetiAssignments.ex03;

public class Utente implements Runnable {
	
	private static int prossima_matricola = 0;
	private final int matricola;
	private final Laboratorio laboratorio;
	private final UtentiLaboratorio tipo_utente;
	private final int numeroAccessi;
	
	private Utente(Laboratorio lab, UtentiLaboratorio tipo, int max_accessi)
	{
		this.laboratorio = lab;
		this.numeroAccessi = (int)(Math.random() * max_accessi);
		this.tipo_utente = tipo;
		this.matricola = prossima_matricola;
		prossima_matricola++;
	}
	
	public static Utente creaProfessore(Laboratorio lab)
	{
		return new Utente(lab, UtentiLaboratorio.Professore, 3);
	}
	
	public static Utente creaTesista(Laboratorio lab)
	{
		return new Utente(lab, UtentiLaboratorio.Tesista, 5);
	}
	
	public static Utente creaStudente(Laboratorio lab)
	{
		return new Utente(lab, UtentiLaboratorio.Studente, 15);
	}
	
	@Override
	public void run()
	{
		try
		{
			for(int i = 0; i < numeroAccessi; i++)
			{
				laboratorio.Entra(this);
				usaComputer();
				laboratorio.Esci(this);
				attendiPerRientrare(i);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void usaComputer() throws InterruptedException
	{
		Thread.sleep((long)(Math.random() * 1000) + 500);
	}
	
	private void attendiPerRientrare(int iterazione) throws InterruptedException
	{
		if(iterazione < numeroAccessi)
		{
			Thread.sleep((long)(Math.random() * 4000) + 1000);
		}
	}
	
	public UtentiLaboratorio tipoUtente()
	{
		return tipo_utente;
	}
	
	public int matricolaUtente()
	{
		return matricola;
	}
}
