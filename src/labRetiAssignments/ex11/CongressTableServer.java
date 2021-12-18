package labRetiAssignments.ex11;

import java.util.List;
import java.util.Vector;
import java.util.LinkedList;

public class CongressTableServer extends CongressTable
{
	private static final long serialVersionUID = 1L;
	private int max_speakers;
	private int max_sessions;
	private int max_days;
	
	public CongressTableServer(int days, int sessions, int max_speakers)
	{
		super();
		
		this.max_days = days;
		this.max_sessions = sessions;
		this.max_speakers = max_speakers;
		generateTable();
	}
	
	private void generateTable()
	{
		for(int i = 0; i < max_days; i++)
		{
			Vector<List<String>> day = new Vector<List<String>>();
			
			for(int j = 0; j < max_sessions; j++)
			{
				day.add(new LinkedList<String>());
			}
			
			table.add(day);
		}
	}
	
	public void addSpeaker(int day, int session_number, String speaker)
	{
		List<String> session = getSession(day, session_number);
		checkSpeakerCanBeAdded(session);
		addSpeakerToSession(session, speaker);
	}
	
	private void checkSpeakerCanBeAdded(List<String> session)
	{
		if(session.size() >= max_speakers)
		{
			throw new MaxSpeakersException();
		}
	}
	
	private void addSpeakerToSession(List<String> session, String speaker)
	{
		session.add(speaker);
	}
}
