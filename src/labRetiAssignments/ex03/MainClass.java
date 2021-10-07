package labRetiAssignments.ex03;

import java.util.*;

public class MainClass {
	private final static int numero_studenti = 20;
	private final static int numero_tesisti = 10;
	private final static int numero_professori = 5;
	private final static int numero_computer = 20;

	public static void main(String[] args)
	{
		Laboratorio lab = new Laboratorio(numero_computer);
		List<Thread> threads = new ArrayList<Thread>();
		
		CreaUtentiLaboratorio(lab, threads);
		AttivaThreadUtenti(threads);
	}
	
	private static void CreaUtentiLaboratorio(Laboratorio lab, List<Thread> threads)
	{
		for(int i = 0; i < numero_professori; i++)
			threads.add(new Thread(  Utente.creaProfessore(lab)  ));
		
		for(int i = 0; i < numero_tesisti; i++)
			threads.add(new Thread(  Utente.creaTesista( lab, (int)(Math.random() * numero_computer) )  ));
		
		for(int i = 0; i < numero_studenti; i++)
			threads.add(new Thread(  Utente.creaStudente(lab)  ));
		
		Collections.shuffle(threads);
	}
	
	private static void AttivaThreadUtenti(List<Thread> threads)
	{
		try
		{
			for(Thread t : threads)
				t.start();
			
			for(Thread t : threads)
				t.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
