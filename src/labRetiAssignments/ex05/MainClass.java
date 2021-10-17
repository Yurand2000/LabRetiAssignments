package labRetiAssignments.ex05;

import java.io.File;
import java.util.*;

public class MainClass {
	private static int consumer_number = 10;
	
	private static File main_dir;
	private static SynchronizedLinkedQueue<String> queue;
	private static Thread producer_thread;
	private static List<Thread> consumer_threads;
	
	public static void main(String[] args) {
		
		//main_dir = new File(args[0]);
		main_dir = new File(".");
		if(!checkDirectoryValid(main_dir))
		{
			return;
		}
		
		queue = new SynchronizedLinkedQueue<String>();
		consumer_threads = new LinkedList<Thread>();
		
		try
		{
			spawnAndStartThreads();
			waitForProducerTermination();
			waitForEmptyConsumerQueue();
			interruptAndCloseConsumerThreads();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private static boolean checkDirectoryValid(File main_dir)
	{
		return main_dir.exists() && main_dir.isDirectory();
	}
	
	private static void spawnAndStartThreads()
	{
		producer_thread = new Thread(new Producer(main_dir, queue));
		for(int i = 0; i < consumer_number; i++)
		{
			consumer_threads.add(new Thread(new Consumer(queue)));
		}
		
		producer_thread.start();
		for(int i = 0; i < consumer_threads.size(); i++)
		{
			consumer_threads.get(i).start();
		}
	}
	
	private static void waitForProducerTermination() throws InterruptedException
	{
		producer_thread.join();
	}
	
	private static void waitForEmptyConsumerQueue() throws InterruptedException
	{
		queue.waitEmpty();
	}
	
	private static void interruptAndCloseConsumerThreads() throws InterruptedException
	{
		for(int i = 0; i < consumer_threads.size(); i++)
		{
			consumer_threads.get(i).interrupt();
		}
		
		for(int i = 0; i < consumer_threads.size(); i++)
		{
			consumer_threads.get(i).join();
		}
	}
}
