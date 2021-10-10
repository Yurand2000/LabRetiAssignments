package labRetiAssignments.ex04;

public class Utente implements Runnable {
	
	private static int prossima_matricola = 0;
	private final int matricola;
	private final int numeroAccessi;
	private final int computer_specifico;
	private final Laboratorio laboratorio;
	private final UtentiLaboratorio tipo_utente;
	
	private Utente(Laboratorio lab, UtentiLaboratorio tipo, int min_accessi, int max_accessi, int computer_specifico)
	{
		this.laboratorio = lab;
		this.numeroAccessi = min_accessi + (int)(Math.random() * max_accessi);
		this.tipo_utente = tipo;
		this.matricola = prossima_matricola;
		this.computer_specifico = computer_specifico;
		prossima_matricola++;
	}
	
	public static Utente creaProfessore(Laboratorio lab)
	{
		return new Utente(lab, UtentiLaboratorio.Professore, 2, 3, -1);
	}
	
	public static Utente creaTesista(Laboratorio lab, int computer_specifico)
	{
		return new Utente(lab, UtentiLaboratorio.Tesista, 1, 5, computer_specifico);
	}
	
	public static Utente creaStudente(Laboratorio lab)
	{
		return new Utente(lab, UtentiLaboratorio.Studente, 1, 8, -1);
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
		Thread.sleep((long)(Math.random() * 1000) + 100);
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
	
	public int indiceComputerSpecifico()
	{
		return computer_specifico;
	}
}
