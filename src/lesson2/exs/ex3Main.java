package lesson2.exs;

import java.util.ArrayList;
import java.util.concurrent.*;

public class ex3Main {

	private static double n = 1;
	private static int max_pow = 50;
	private static double result = 0;
	
	public static void main(String[] args) {
		ThreadPoolExecutor pow_calculator = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		ArrayList<Future<Double>> future_array = new ArrayList<Future<Double>>();
		
		for(int i = 2; i <= max_pow; i++)
		{
			future_array.add(pow_calculator.submit(new Power(n, i)));
		}
		
		for(int i = 0; i < future_array.size(); i++)
		{
			try {
				result += future_array.get(i).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		System.out.printf("Sum of pows from 2 to %d of %f: %f\n", max_pow, n, result);
		pow_calculator.shutdown();
	}

}
