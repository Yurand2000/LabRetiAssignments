package labRetiAssignments.ex01;

public class MainClass
{
	private static double accuracy = 0.0;
	private static long max_time = 0L;
	
	public static void main(String[] args)
	{
		try
		{
			parseArguments(args);
		}
		catch(Exception e)
		{
			System.out.printf("Error in parsing the arguments: %s\n", e.getMessage());
			return;
		}
		
		PiLeibnizCalculator piCalculator =
			new PiLeibnizCalculator(accuracy, max_time);
		piCalculator.calculatePi();
	}
	
	private static void parseArguments(String args[])
	{		
		if(args.length == 2)
		{
			accuracy = Double.parseDouble(args[0]);
			max_time = Long.parseLong(args[1]) * 1000L;
			
			if(accuracy < 0)
			{
				throw new RuntimeException("Accuracy can't be negative.");
			}
			
			if(max_time < 0)
			{
				throw new RuntimeException("Max waiting time can't be negative.");
			}
		}
		else
		{
			throw new RuntimeException("Given number of arguments is incorrect.");
		}
	}
}
