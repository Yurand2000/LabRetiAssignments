package labRetiAssignments.ex03;

public class Utente implements Runnable {
	
	private static int next_id = 0;
	private int id;
	private Laboratorio laboratorio;
	private UtentiLaboratorio tipo_utente;
	private int numeroAccessi;
	
	private Utente(Laboratorio lab, UtentiLaboratorio tipo)
	{
		this.laboratorio = lab;
		this.numeroAccessi = (int)(Math.random() * 20);
		this.tipo_utente = tipo;
		this.id = next_id;
		next_id++;
	}
	
	public static Utente creaProfessore(Laboratorio lab)
	{
		return new Utente(lab, UtentiLaboratorio.Professore);
	}
	
	public static Utente creaTesista(Laboratorio lab)
	{
		return new Utente(lab, UtentiLaboratorio.Tesista);
	}
	
	public static Utente creaStudente(Laboratorio lab)
	{
		return new Utente(lab, UtentiLaboratorio.Studente);
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
		Thread.sleep((long)(Math.random() * 1000));
	}
	
	private void attendiPerRientrare(int iterazione) throws InterruptedException
	{
		if(iterazione < numeroAccessi)
		{
			Thread.sleep((long)(Math.random() * 1000));
		}
	}
	
	public UtentiLaboratorio tipoUtente()
	{
		return tipo_utente;
	}
	
	public int idUtente()
	{
		return id;
	}
}
