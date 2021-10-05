package labRetiAssignments.ex03;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

public class Laboratorio {
	final int numero_computer;
	final int indice_pc_tesisti;

	ReadWriteLock mutex_laboratorio_professori;
	
	Lock mutex_dati_laboratorio;
	Condition condition_pc_tesista_bloccato;
	Condition condition_pc_bloccati;
	boolean pc_tesista_bloccato;
	List<Utente> utenti_al_pc;
	
	AtomicInteger studenti_in_coda = new AtomicInteger(0);
	AtomicInteger tesisti_in_coda = new AtomicInteger(0);
	AtomicInteger professori_in_coda = new AtomicInteger(0);
	
	public Laboratorio(int numero_computer, int indice_pc_tesisti)
	{
		this.numero_computer = numero_computer;
		this.indice_pc_tesisti = indice_pc_tesisti;
		
		//mutex_laboratorio = new ReentrantReadWriteLock(true);
		mutex_laboratorio_professori = new WriterPriorityReadWriteLock();
		mutex_dati_laboratorio = new ReentrantLock(true);
		condition_pc_tesista_bloccato = mutex_dati_laboratorio.newCondition();
		condition_pc_bloccati = mutex_dati_laboratorio.newCondition();
		
		pc_tesista_bloccato = false;
		utenti_al_pc = new ArrayList<Utente>();
		for(int i = 0; i < numero_computer; i++)
			utenti_al_pc.add(null);
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
		professori_in_coda.getAndIncrement();
		OttieniMutexProfessore();
		professori_in_coda.getAndDecrement();
		synchronized(System.out.getClass()) {
			StampaUtentiInCoda();
			System.out.printf("Professore n. %02d entra.\n", u.matricolaUtente());
		}
	}
	
	private void EntraTesista(Utente u) throws InterruptedException
	{
		BloccaPCTesista(u);
		tesisti_in_coda.getAndIncrement();
		OttieniMutexStudente();
		tesisti_in_coda.getAndDecrement();
		synchronized(System.out.getClass()) {
			StampaUtentiInCoda();
			System.out.printf("Tesista n. %02d entra.\n", u.matricolaUtente());
		}
	}
	
	private void BloccaPCTesista(Utente u) throws InterruptedException
	{
		mutex_dati_laboratorio.lock();
		while(utenti_al_pc.get(indice_pc_tesisti) != null)
		{
			pc_tesista_bloccato = true;
			condition_pc_tesista_bloccato.await();
		}
		utenti_al_pc.set(indice_pc_tesisti, u);
		mutex_dati_laboratorio.unlock();
	}
	
	private void EntraStudente(Utente u) throws InterruptedException
	{
		BloccaPCUtente(u);
		studenti_in_coda.getAndIncrement();
		OttieniMutexStudente();
		studenti_in_coda.getAndDecrement();
		synchronized(System.out.getClass()) {
			StampaUtentiInCoda();
			System.out.printf("Studente n. %02d entra.\n", u.matricolaUtente());
		}
	}
	
	private void BloccaPCUtente(Utente u) throws InterruptedException
	{
		mutex_dati_laboratorio.lock();
		int pc_libero = OttieniIndicePcLibero();
		while(pc_libero == -1)
		{
			condition_pc_bloccati.await();
			pc_libero = OttieniIndicePcLibero();
		}
		utenti_al_pc.set(pc_libero, u);
		mutex_dati_laboratorio.unlock();
	}
	
	private int OttieniIndicePcLibero()
	{
		int pc_libero = -1;
		mutex_dati_laboratorio.lock();
		for(int i = 0; (i < numero_computer) && (pc_libero == -1); i++)
		{			
			if((!pc_tesista_bloccato || indice_pc_tesisti != i) && utenti_al_pc.get(i) == null)
			{
				pc_libero = i;
			}
		}
		mutex_dati_laboratorio.unlock();
		return pc_libero;
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
		synchronized(System.out.getClass()) {
			StampaUtentiInCoda();
			System.out.printf("Professore n. %02d esce.\n", u.matricolaUtente());
		}
		RilasciaMutexProfessore();
	}
	
	private void EsceTesista(Utente u)
	{
		synchronized(System.out.getClass()) {
			StampaUtentiInCoda();
			System.out.printf("Tesista n. %02d esce.\n", u.matricolaUtente());
		}
		SbloccaPCUtente(u);
		RilasciaMutexStudente();
	}
	
	private void EsceStudente(Utente u)
	{
		synchronized(System.out.getClass()) {
			StampaUtentiInCoda();
			System.out.printf("Studente n. %02d esce.\n", u.matricolaUtente());
		}
		SbloccaPCUtente(u);
		RilasciaMutexStudente();
	}
	
	private void SbloccaPCUtente(Utente u)
	{
		mutex_dati_laboratorio.lock();
		boolean found = false;
		for(int i = 0; i < numero_computer && !found; i++)
		{
			if(utenti_al_pc.get(i) == u)
			{
				utenti_al_pc.set(i, null);
				condition_pc_bloccati.signal();
				if(indice_pc_tesisti == i)
				{
					pc_tesista_bloccato = false;
					condition_pc_tesista_bloccato.signal();
				}
				found = true;
			}
		}
		mutex_dati_laboratorio.unlock();
	}
	
	private void OttieniMutexProfessore()
	{
		mutex_laboratorio_professori.writeLock().lock();
	}
	
	private void RilasciaMutexProfessore()
	{
		mutex_laboratorio_professori.writeLock().unlock();
	}
	
	private void OttieniMutexStudente()
	{
		mutex_laboratorio_professori.readLock().lock();
	}
	
	private void RilasciaMutexStudente()
	{
		mutex_laboratorio_professori.readLock().unlock();
	}
	
	private void StampaUtentiInCoda()
	{	
		System.out.printf("P:%3d T:%3d S:%3d - ", professori_in_coda.get(), tesisti_in_coda.get(), studenti_in_coda.get());
	}
}
