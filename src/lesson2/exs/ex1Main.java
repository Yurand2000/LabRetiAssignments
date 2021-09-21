package lesson2.exs;

import java.util.concurrent.*;

public class ex1Main {

	public static void main(String[] args) {
		ThreadPoolExecutor waitingRoom = new ThreadPoolExecutor(5, 5, 50, TimeUnit.MINUTES,
				new ArrayBlockingQueue<Runnable>(10), new RejectedTravellerExecutor());
		
		for(int i = 0; i < 50; i++)
		{
			try {
				waitingRoom.execute(new Traveller(i));
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		waitingRoom.shutdown();
	}

}
