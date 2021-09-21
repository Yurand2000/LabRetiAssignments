package lesson2.exs;

public class Traveller implements Runnable {
	
	private int id;
	
	public Traveller(int id)
	{
		this.id = id;
	}
	
	public void run()
	{
		System.out.printf("Viaggiatore %d: sto acquistando un biglietto.\n", id);
		try {
			Thread.sleep((long)(Math.random() * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Viaggiator %d: ho acquistato il biglietto.\n", id);
	}
	
	public int getId()
	{
		return id;
	}
}
