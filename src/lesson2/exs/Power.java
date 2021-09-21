package lesson2.exs;

import java.util.concurrent.*;

public class Power implements Callable<Double> {
	private double number;
	private int exponent;
	
	public Power(double number, int exponent)
	{
		this.number = number;
		this.exponent = exponent;
	}
	
	public Double call()
	{
		return Math.pow(number, exponent);
	}
}
