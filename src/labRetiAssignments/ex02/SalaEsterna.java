package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class SalaEsterna {
	BlockingQueue<Runnable> fila_sala_esterna;
	
	public SalaEsterna()
	{
		fila_sala_esterna = new LinkedBlockingQueue<Runnable>();
	}
	
	public void entraNuovoCliente() throws InterruptedException
	{
		fila_sala_esterna.put(new Cliente());
	}
	
	public Runnable clientePassaAllaSalaInterna() throws InterruptedException
	{
		return fila_sala_esterna.take();
	}
}
