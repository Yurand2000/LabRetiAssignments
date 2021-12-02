package labRetiAssignments.ex11;

import java.util.List;
import java.util.LinkedList;

public class CongressTableServer extends CongressTable
{
	private int max_speakers;
	
	public CongressTableServer(int sessions, int max_speakers)
	{
		super();
		
		this.max_speakers = max_speakers;
		for(int i = 0; i < sessions; i++)
		{
			table.add(new LinkedList<String>());
		}
	}
	
	public void addSpeaker(int session, String speaker)
	{
		List<String> line = getLine(session);
		if(line.size() < max_speakers)
		{
			line.add(speaker);
		}
		else
		{
			throw new RuntimeException("Can't have more speakers added to the requested session.");
		}
	}
}
