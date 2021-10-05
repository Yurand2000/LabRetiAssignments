package labRetiAssignments.ex03;

import java.util.concurrent.locks.*;

public class ComputerLaboratorio {
	Lock mutex;
	Condition condition;
	Utente utente_corrente;
	
	public ComputerLaboratorio()
	{
		mutex = new ReentrantLock();
		condition = mutex.newCondition();
		utente_corrente = null;
	}
	
	public void UsaComputer(Utente u) throws InterruptedException
	{
		mutex.lock();
		while(utente_corrente != null)
			condition.await();
		utente_corrente = u;
		mutex.unlock();
	}
	
	public void RilasciaComputer(Utente u)
	{
		mutex.lock();
		utente_corrente = null;
		condition.signal();
		mutex.unlock();
	}
	
	public Utente OttieniUtenteCorrente()
	{
		Utente utente_corrente;
		mutex.lock();
		utente_corrente = this.utente_corrente;
		mutex.unlock();
		return utente_corrente;
	}
}
