package labRetiAssignments.ex05;

import java.io.File;

public class Producer implements Runnable
{
	private SynchronizedLinkedQueue<String> queue;
	private File directory;
	
	public Producer(File directory, SynchronizedLinkedQueue<String> queue)
	{
		if(queue == null)
			throw new NullPointerException("Given queue is null.");
		if(directory == null)
			throw new NullPointerException("Given directory is null.");
		
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
			pushDirectoryIntoQueue(curr_dir);
			for(File sub_dir : curr_dir.listFiles())
				recursivelyVisitAllSubdirectories(sub_dir);
		}
	}
	
	private void pushDirectoryIntoQueue(File dir)
	{
		queue.push(dir.getPath());
	}
}
