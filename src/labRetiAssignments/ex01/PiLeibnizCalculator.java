package labRetiAssignments.ex01;

public class PiLeibnizCalculator
{
	private double accuracy;
	private long max_time;
	private Thread thread = null;
	
	public PiLeibnizCalculator(double accuracy, long max_time_millis)
	{
		this.accuracy = accuracy;
		this.max_time = max_time_millis;
		
		System.out.printf("Accuracy: %4.16f; Max Time: %d millis.\n", accuracy, max_time_millis);
	}
	
	public void calculatePi()
	{
		PiLeibnizRunnable runnable = new PiLeibnizRunnable(accuracy);
		
		createThreadThenStart(runnable);
		waitForThreadMaxTimeThenInterrupt();
	}
	
	private void createThreadThenStart(Runnable runnable)
	{
		thread = new Thread(runnable);
		thread.start();
	}
	
	private void waitForThreadMaxTimeThenInterrupt()
	{
		try
		{
			thread.join(max_time);
			if(thread.isAlive())
			{
				thread.interrupt();
				System.out.printf("Thread termination by interruption.\n");
				
				thread.join();
			}
			else
			{
				System.out.printf("Thread termination by end of task.\n");
			}
		}
		catch (InterruptedException e)
		{
			System.out.printf("Exceptional situation: No other thread should have called interrupt() on the given thread.\n");
		}
	}
}
