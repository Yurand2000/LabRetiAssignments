package labRetiAssignments.ex05;

import java.io.*;

public class Producer implements Runnable
{
	private SynchronizedLinkedQueue<String> queue;
	private File directory;
	
	public Producer(File directory, SynchronizedLinkedQueue<String> queue)
	{
		this.directory = directory;
		this.queue = queue;
	}
	
	@Override
	public void run()
	{
		recursivelyVisitAllSubdirectories(directory);
	}
	
	private void recursivelyVisitAllSubdirectories(File curr_dir)
	{
		if(curr_dir.isDirectory())
		{ 
			queue.push(curr_dir.getPath());
			for(File sub_dir : curr_dir.listFiles())
				recursivelyVisitAllSubdirectories(sub_dir);
		}
	}
}
