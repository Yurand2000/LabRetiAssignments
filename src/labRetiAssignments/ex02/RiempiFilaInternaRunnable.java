package labRetiAssignments.ex02;

import java.util.concurrent.*;

public class RiempiFilaInternaRunnable implements Runnable {

	private BlockingDeque<Runnable> coda_esterna;
	private ThreadPoolExecutor sala_interna;
	
	public RiempiFilaInternaRunnable(BlockingDeque<Runnable> coda_esterna, ThreadPoolExecutor sala_interna)
	{
		this.coda_esterna = coda_esterna;
		this.sala_interna = sala_interna;
	}
	
	@Override
	public void run()
	{
		Runnable head = null;
		try
		{
			head = coda_esterna.takeFirst();
			sala_interna.execute(head);
			while(!coda_esterna.isEmpty())
			{
				head = coda_esterna.takeFirst();
				sala_interna.execute(head);
			}
		}
		catch(RejectedExecutionException e)
		{
			try
			{
				coda_esterna.putFirst(head);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
