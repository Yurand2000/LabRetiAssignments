package lesson3.exs;

import java.util.*;
import java.util.concurrent.*;

public class ex1Main {
	public static void main(String[] args)
	{
		Counter c = new ReadWriteLockCounter();
		List<Writer> writers = new ArrayList<Writer>();
		List<Reader> readers = new ArrayList<Reader>();
		
		for(int i = 0; i < 20; i++)
		{
			writers.add(new Writer(c));
			readers.add(new Reader(c));
		}
		
		ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newCachedThreadPool();

		for(int i = 0; i < 20; i++)
			pool.execute(writers.get(i));
		
		for(int i = 0; i < 20; i++)
			pool.execute(readers.get(i));
	}
}
