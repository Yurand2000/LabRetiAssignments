package labRetiAssignments.ex04;

import java.util.*;

public class ComputerLaboratorio {
	List<Utente> utentiAlPc;
	
	public ComputerLaboratorio(int numero_computer)
	{
		utentiAlPc = new ArrayList<Utente>(numero_computer);
		for(int i = 0; i < numero_computer; i++)
			utentiAlPc.add(null);
	}
	
	public int totaleComputer()
	{
		return utentiAlPc.size();
	}
	
	public void occupa(int indice_computer, Utente utente)
	{
		utentiAlPc.set(indice_computer, utente);
	}
	
	public void libera(int indice_computer)
	{
		utentiAlPc.set(indice_computer, null);
	}
	
	public boolean utenteAlComputer(int indice_computer, Utente utente)
	{
		return utentiAlPc.get(indice_computer) == utente;
	}
	
	public boolean computerLibero(int indice_computer)
	{
		return utentiAlPc.get(indice_computer) == null;
	}
}
