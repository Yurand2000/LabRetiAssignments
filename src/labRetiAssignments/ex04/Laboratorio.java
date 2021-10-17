package labRetiAssignments.ex04;

import java.util.concurrent.locks.*;

public class Laboratorio {
	final ReadWriteLock mutexAccessoLaboratorio;
	final Tutor tutor;
	
	public Laboratorio(int numero_computer)
	{		
		mutexAccessoLaboratorio = new WriterPriorityReadWriteLock_MonitorImplementation(); //implementata utilizzando solo construtti monitor
		tutor = new Tutor(numero_computer);
	}
	
	public void Entra(Utente u) throws InterruptedException
	{
		if(u.tipoUtente() == UtentiLaboratorio.Professore)
			EntraProfessore(u);
		else if(u.tipoUtente() == UtentiLaboratorio.Tesista)
			EntraTesista(u);
		else if(u.tipoUtente() == UtentiLaboratorio.Studente)
			EntraStudente(u);
		else
			throw new RuntimeException();
	}
	
	private void EntraProfessore(Utente u)
	{
		OttieniMutexProfessore();
		StampaUtenteEntra(u);
	}
	
	private void EntraTesista(Utente u) throws InterruptedException
	{
		tutor.BloccaPCTesista(u);
		OttieniMutexStudente();
		StampaUtenteEntra(u);
	}
	
	private void EntraStudente(Utente u) throws InterruptedException
	{
		tutor.BloccaPCStudente(u);
		OttieniMutexStudente();
		StampaUtenteEntra(u);
	}
	
	private synchronized void StampaUtenteEntra(Utente u)
	{
		if(u.tipoUtente() == UtentiLaboratorio.Professore)
			System.out.printf("Professore n. %02d entra.\n", u.matricolaUtente());
		else if(u.tipoUtente() == UtentiLaboratorio.Tesista)
			System.out.printf("Tesista n. %02d entra.\n", u.matricolaUtente());
		else if(u.tipoUtente() == UtentiLaboratorio.Studente)
			System.out.printf("Studente n. %02d entra.\n", u.matricolaUtente());
		else
			throw new RuntimeException();
	}
	
	private void OttieniMutexProfessore()
	{
		mutexAccessoLaboratorio.writeLock().lock();
	}
	
	private void OttieniMutexStudente()
	{
		mutexAccessoLaboratorio.readLock().lock();
	}
	
	public void Esci(Utente u) throws InterruptedException
	{
		if(u.tipoUtente() == UtentiLaboratorio.Professore)
			EsceProfessore(u);
		else if(u.tipoUtente() == UtentiLaboratorio.Tesista)
			EsceTesista(u);
		else if(u.tipoUtente() == UtentiLaboratorio.Studente)
			EsceStudente(u);
		else
			throw new RuntimeException();
	}
	
	private void EsceProfessore(Utente u)
	{
		StampaUtenteEsce(u);
		RilasciaMutexProfessore();
	}
	
	private void EsceTesista(Utente u)
	{
		StampaUtenteEsce(u);
		RilasciaMutexStudente();
		tutor.SbloccaPCUtente(u);
	}
	
	private void EsceStudente(Utente u)
	{
		StampaUtenteEsce(u);
		RilasciaMutexStudente();
		tutor.SbloccaPCUtente(u);
	}
	
	private synchronized void StampaUtenteEsce(Utente u)
	{
		if(u.tipoUtente() == UtentiLaboratorio.Professore)
			System.out.printf("Professore n. %02d esce.\n", u.matricolaUtente());
		else if(u.tipoUtente() == UtentiLaboratorio.Tesista)
			System.out.printf("Tesista n. %02d esce.\n", u.matricolaUtente());
		else if(u.tipoUtente() == UtentiLaboratorio.Studente)
			System.out.printf("Studente n. %02d esce.\n", u.matricolaUtente());
		else
			throw new RuntimeException();
	}
	
	private void RilasciaMutexProfessore()
	{
		mutexAccessoLaboratorio.writeLock().unlock();
	}
	
	private void RilasciaMutexStudente()
	{
		mutexAccessoLaboratorio.readLock().unlock();
	}
}
