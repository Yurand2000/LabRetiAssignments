package labRetiAssignments.ex03;

import java.util.*;

public class MainClass {
	private final static int numero_studenti = 100;
	private final static int numero_tesisti = 20;
	private final static int numero_professori = 5;
	private final static int numero_computer = 20;
	private final static int numero_computer_tesisti = 7;

	public static void main(String[] args)
	{
		Laboratorio lab = new Laboratorio(numero_computer, numero_computer_tesisti);
		List<Thread> threads = new ArrayList<Thread>();
		
		for(int i = 0; i < numero_studenti; i++)
			threads.add(new Thread(Utente.creaStudente(lab)));
		
		for(int i = 0; i < numero_tesisti; i++)
			threads.add(new Thread(Utente.creaTesista(lab)));
		
		for(int i = 0; i < numero_professori; i++)
			threads.add(new Thread(Utente.creaProfessore(lab)));

		Collections.shuffle(threads);
		
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
