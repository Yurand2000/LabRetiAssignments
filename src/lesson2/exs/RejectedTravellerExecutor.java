package lesson2.exs;

import java.util.concurrent.*;

public class RejectedTravellerExecutor implements RejectedExecutionHandler {
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
	{
		try
		{
			Traveller tr = (Traveller)(r);
			System.out.printf("Traveller no. %d: sala esaurita.\n", tr.getId());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
