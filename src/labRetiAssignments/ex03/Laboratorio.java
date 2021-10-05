package labRetiAssignments.ex03;

import java.util.concurrent.locks.*;

public class Laboratorio {
	//variabili read-only
	final int indice_pc_tesisti;

	//variabili da gestire in mutua esclusione	
	final ReadWriteLock mutexAccessoLaboratorio;
	
	final Lock mutexDatiLaboratorio;
	final Condition conditionPcTesistiOccupato;
	final Condition conditionPcTuttiOccupati;
	int tesistiInAttesa;
	final ComputerLaboratorio computers;
	
	public Laboratorio(int numero_computer, int indice_pc_tesisti)
	{
		this.indice_pc_tesisti = indice_pc_tesisti;
		
		mutexAccessoLaboratorio = new WriterPriorityReadWriteLock();
		
		mutexDatiLaboratorio = new ReentrantLock(true);
		conditionPcTesistiOccupato = mutexDatiLaboratorio.newCondition();
		conditionPcTuttiOccupati = mutexDatiLaboratorio.newCondition();
		
		tesistiInAttesa = 0;
		computers = new ComputerLaboratorio(numero_computer);
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
		System.out.printf("Professore n. %02d entra.\n", u.matricolaUtente());
	}
	
	private void EntraTesista(Utente u) throws InterruptedException
	{
		BloccaPCTesista(u);
		OttieniMutexStudente();
		System.out.printf("Tesista n. %02d entra.\n", u.matricolaUtente());
	}
	
	private void BloccaPCTesista(Utente u) throws InterruptedException
	{
		mutexDatiLaboratorio.lock();
		
		tesistiInAttesa++;
		while(!computers.computerLibero(indice_pc_tesisti))
			AspettaPcTesisti();
		tesistiInAttesa--;
		computers.occupa(indice_pc_tesisti, u);
		
		mutexDatiLaboratorio.unlock();
	}
	
	private void AspettaPcTesisti() throws InterruptedException
	{
		conditionPcTesistiOccupato.await();
	}
	
	private void EntraStudente(Utente u) throws InterruptedException
	{
		BloccaPCStudente(u);
		OttieniMutexStudente();
		System.out.printf("Studente n. %02d entra.\n", u.matricolaUtente());
	}
	
	private void BloccaPCStudente(Utente u) throws InterruptedException
	{
		mutexDatiLaboratorio.lock();
		int pc_libero = OttieniIndicePcLibero();
		while(pc_libero == -1)
		{
			AspettaPcLibero();
			pc_libero = OttieniIndicePcLibero();
		}
		computers.occupa(pc_libero, u);
		mutexDatiLaboratorio.unlock();
	}
	
	private void AspettaPcLibero() throws InterruptedException
	{
		conditionPcTuttiOccupati.await();
	}
	
	private int OttieniIndicePcLibero()
	{
		int pc_libero = -1;
		for(int i = 0; (i < computers.totaleComputer()) && (pc_libero == -1); i++)
		{			
			if(computers.computerLibero(i) && !EscludiPcTesisti(i))
				pc_libero = i;
		}
		return pc_libero;
	}
	
	private boolean EscludiPcTesisti(int indice_pc_corrente)
	{
		return indice_pc_tesisti == indice_pc_corrente && tesistiInAttesa > 0;
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
		System.out.printf("Professore n. %02d esce.\n", u.matricolaUtente());
		RilasciaMutexProfessore();
	}
	
	private void EsceTesista(Utente u)
	{
		System.out.printf("Tesista n. %02d esce.\n", u.matricolaUtente());
		RilasciaMutexStudente();
		SbloccaPCUtente(u);
	}
	
	private void EsceStudente(Utente u)
	{
		System.out.printf("Studente n. %02d esce.\n", u.matricolaUtente());
		RilasciaMutexStudente();
		SbloccaPCUtente(u);
	}
	
	private void SbloccaPCUtente(Utente u)
	{
		mutexDatiLaboratorio.lock();
		boolean found = false;
		for(int i = 0; i < computers.totaleComputer() && !found; i++)
		{
			if(computers.utenteAlComputer(i, u))
			{
				computers.libera(i);
				SegnalaPCLibero();
				if(indice_pc_tesisti == i)
					SegnalaPCTesistiLibero();
				found = true;
			}
		}
		mutexDatiLaboratorio.unlock();
	}
	
	private void SegnalaPCLibero()
	{
		conditionPcTuttiOccupati.signal();
	}
	
	private void SegnalaPCTesistiLibero()
	{
		conditionPcTesistiOccupato.signal();
	}
	
	private void OttieniMutexProfessore()
	{
		mutexAccessoLaboratorio.writeLock().lock();
	}
	
	private void RilasciaMutexProfessore()
	{
		mutexAccessoLaboratorio.writeLock().unlock();
	}
	
	private void OttieniMutexStudente()
	{
		mutexAccessoLaboratorio.readLock().lock();
	}
	
	private void RilasciaMutexStudente()
	{
		mutexAccessoLaboratorio.readLock().unlock();
	}
}
