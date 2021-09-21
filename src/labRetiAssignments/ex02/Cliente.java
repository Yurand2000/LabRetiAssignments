package labRetiAssignments.ex02;

public class Cliente implements Runnable {

	public Cliente()
	{
		
	}
	
	public void run()
	{
		try {
			Thread.sleep((long)(Math.random() * 1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
