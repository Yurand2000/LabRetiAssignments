package lesson4.exs;

public class ex1Main {

	public static void main(String[] args) {
		//Dropbox dropbox = new Dropbox();
		Dropbox dropbox = new DropboxExtended();
		
		Thread even_consumer = new Thread( new Consumer(dropbox, true) );
		Thread odd_consumer = new Thread( new Consumer(dropbox, false) );
		Thread producer = new Thread( new Producer(dropbox) );
		
		even_consumer.start();
		odd_consumer.start();
		producer.start();
		
		
		try
		{
			Thread.sleep(5000);
			producer.interrupt();
			even_consumer.interrupt();
			odd_consumer.interrupt();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
