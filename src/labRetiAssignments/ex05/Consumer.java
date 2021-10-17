package labRetiAssignments.ex05;

import java.io.File;

public class Consumer implements Runnable
{
	private SynchronizedLinkedQueue<String> queue;

	public Consumer(SynchronizedLinkedQueue<String> queue)
	{
		this.queue = queue;
	}
	
	@Override
	public void run()
	{
		try
		{
			File curr_dir = null;
			while(!Thread.currentThread().isInterrupted())
			{
				curr_dir = new File(queue.pop());
				printFileTree(curr_dir);
			}
		}
		catch (InterruptedException e) { }	
	}
	
	private void printFileTree(File curr_dir)
	{
		StringBuffer file_tree = new StringBuffer();
		if(!curr_dir.exists() || !curr_dir.isDirectory())
		{
			throw new RuntimeException();
		}
		
		file_tree.append(curr_dir.getPath() + '\n');
		for(File sub_file : curr_dir.listFiles())
		{
			if(sub_file.isFile())
			{
				file_tree.append((sub_file.getName() + '\n').indent(2));
			}
		}
		
		synchronized(System.out.getClass())
		{
			System.out.print(file_tree.toString());
		}
	}
}
