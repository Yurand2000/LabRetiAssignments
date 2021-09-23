package labRetiAssignments.ex02;

import java.util.concurrent.atomic.*;

public class Cliente implements Runnable {

	private static int count = 0;
	private int id;
	private static AtomicInteger exec_count = new AtomicInteger(0);
	
	public Cliente()
	{
		count++;
		id = count;
	}
	
	public void run()
	{
		try {
			int exec = exec_count.addAndGet(1);
			System.out.printf("Sportello, Cliente: %s, %02d, %d: Operazione Completata.\n", Thread.currentThread().getName().charAt(14), exec, id);
			Thread.sleep((long)(Math.random() * 1000));
			
			//System.out.printf("Sportello, Cliente: %s, %02d, %d: Operazione Completata.\n", Thread.currentThread().getName().charAt(14), exec, id);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
