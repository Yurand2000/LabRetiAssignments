package labRetiAssignments.ex05;

import java.io.File;

public class Consumer implements Runnable
{
	private SynchronizedLinkedQueue<String> queue;

	public Consumer(SynchronizedLinkedQueue<String> queue)
	{
		if(queue == null)
			throw new NullPointerException("Given queue is null.");
		
		this.queue = queue;
	}
	
	@Override
	public void run()
	{
		try
		{
			while(!Thread.currentThread().isInterrupted())
			{
				printFileTree(popFileFromQueue());
			}
		}
		catch (InterruptedException e) {
			
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	private File popFileFromQueue() throws InterruptedException
	{
		return new File(queue.pop());
	}
	
	private void printFileTree(File curr_dir)
	{
		StringBuffer file_tree = new StringBuffer();
		if(!isValidDirectory(curr_dir))
		{
			throw new RuntimeException("Unexpected exception, directory " +
				curr_dir.getPath() + " is no longer valid!");
		}
		
		appendDirNameToOutputString(file_tree, curr_dir);
		for(File sub_file : curr_dir.listFiles())
		{
			appendFileNameToOutputString(file_tree, sub_file);
		}
		
		printFileTreeToConsole(file_tree);
	}
	
	private boolean isValidDirectory(File dir)
	{
		return dir.exists() && dir.isDirectory();
	}
	
	private void appendDirNameToOutputString(StringBuffer buf, File dir)
	{
		buf.append(dir.getPath() + '\n');
	}
	
	private void appendFileNameToOutputString(StringBuffer buf, File file)
	{
		if(file.isFile())
		{
			buf.append("  " + file.getName() + '\n');
		}
	}
	
	private void printFileTreeToConsole(StringBuffer s)
	{
		synchronized(System.out.getClass())
		{
			System.out.print(s.toString());
		}
	}
}
