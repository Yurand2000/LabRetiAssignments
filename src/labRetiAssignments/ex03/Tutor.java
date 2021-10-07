package labRetiAssignments.ex03;

import java.util.*;
import java.util.concurrent.locks.*;

public class Tutor {
	final Lock mutex;
	final Condition conditionPcTuttiOccupati;
	final List<Condition> listaConditionPcOccupato;
	final List<Integer> listaNumeroTesistiInAttesa;
	final ComputerLaboratorio computers;
	
	public Tutor(int numero_computer)
	{
		mutex = new ReentrantLock(true);
		conditionPcTuttiOccupati = mutex.newCondition();

		computers = new ComputerLaboratorio(numero_computer);
		listaNumeroTesistiInAttesa = new ArrayList<Integer>();
		listaConditionPcOccupato = new ArrayList<Condition>();
		for(int i = 0; i < numero_computer; i++)
		{
			listaConditionPcOccupato.add(mutex.newCondition());
			listaNumeroTesistiInAttesa.add(0);
		}
	}

	
	public void BloccaPCTesista(Utente u) throws InterruptedException
	{
		mutex.lock();
		
		int indice_pc = u.indiceComputerSpecifico();
		IncrementaTesistiInAttesa(indice_pc);
		while(!computers.computerLibero(indice_pc))
			AspettaPcTesisti(indice_pc);
		DecrementaTesistiInAttesa(indice_pc);
		computers.occupa(indice_pc, u);
		
		mutex.unlock();
	}
	
	private void IncrementaTesistiInAttesa(int indice_pc)
	{
		listaNumeroTesistiInAttesa.set(indice_pc, listaNumeroTesistiInAttesa.get(indice_pc) + 1);
	}
	
	private void DecrementaTesistiInAttesa(int indice_pc)
	{
		listaNumeroTesistiInAttesa.set(indice_pc, listaNumeroTesistiInAttesa.get(indice_pc) - 1);
	}
	
	private void AspettaPcTesisti(int indice_pc) throws InterruptedException
	{
		listaConditionPcOccupato.get(indice_pc).await();
	}
	
	public void BloccaPCStudente(Utente u) throws InterruptedException
	{
		mutex.lock();
		
		int pc_libero = OttieniIndicePcLibero();
		while(pc_libero == -1)
		{
			AspettaPcLibero();
			pc_libero = OttieniIndicePcLibero();
		}
		computers.occupa(pc_libero, u);
		
		mutex.unlock();
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
			if(computers.computerLibero(i) && !TesistiInAttesa(i))
				pc_libero = i;
		}
		return pc_libero;
	}
	
	private boolean TesistiInAttesa(int indice_pc_corrente)
	{
		return listaNumeroTesistiInAttesa.get(indice_pc_corrente) > 0;
	}
	
	public void SbloccaPCUtente(Utente u)
	{
		mutex.lock();
		boolean found = false;
		for(int i = 0; i < computers.totaleComputer() && !found; i++)
		{
			if(computers.utenteAlComputer(i, u))
			{
				computers.libera(i);
				SegnalaPCLibero();
				SegnalaPCTesistiLibero(i);
				found = true;
			}
		}
		mutex.unlock();
	}
	
	private void SegnalaPCLibero()
	{
		conditionPcTuttiOccupati.signal();
	}
	
	private void SegnalaPCTesistiLibero(int indice_pc)
	{
		listaConditionPcOccupato.get(indice_pc).signal();
	}
}
