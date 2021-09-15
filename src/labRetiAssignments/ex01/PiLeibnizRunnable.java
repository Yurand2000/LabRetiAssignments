package labRetiAssignments.ex01;

public class PiLeibnizRunnable implements Runnable
{
	private double accuracy;
	private double pi;
	private long step;
	
	public PiLeibnizRunnable(double accuracy)
	{
		this.accuracy = accuracy;
		this.pi = 0;
		this.step = 0;
	}
	
	@Override
	public void run()
	{
		while(isThreadNotInterruptedAndAccuracyNotReached())
		{
			calcNextStep();
		}

		System.out.printf("Leibniz Pi: %1.16f; Leibniz Depth: %d; Java Math.PI: %1.16f.\n", pi, step, Math.PI);
	}
	
	private boolean isThreadNotInterruptedAndAccuracyNotReached()
	{
		return !Thread.interrupted() && Math.abs(pi - Math.PI) >= accuracy;
	}
	
	private void calcNextStep()
	{
		//Leibniz formula: SUM(0, +inf, (-1^n)*4/(2n+1))
		//Each step is: (-1^n)*4/(2n+1)
		
		if(isEvenStep())
			pi += getNTerm();
		else
			pi -= getNTerm();
		
		step++;
	}
	
	private boolean isEvenStep()
	{
		return step % 2 == 0;
	}
	
	private double getNTerm()
	{
		return 4.0/(step * 2.0 + 1.0);
	}
}
