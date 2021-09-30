package labRetiAssignments.ex02;

public class Cliente implements Runnable {
	
	private int id;
	
	public Cliente(int id)
	{
		this.id = id;
	}
	
	public void run()
	{
		try
		{
			Thread.sleep((long)(Math.random() * 100));
			System.out.printf("Sportello, Cliente: %s, %d: Operazione Completata.\n",
					Thread.currentThread().getName().charAt(14), id);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
