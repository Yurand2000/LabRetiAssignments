package labRetiAssignments.ex11;

import java.io.Serializable;
import java.util.*;

public class CongressTable implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected final Vector<Vector<List<String>>> table;
	
	public CongressTable()
	{
		table = new Vector<Vector<List<String>>>();
	}
	
	public List<String> getSpeakers(int day, int session)
	{
		return Collections.unmodifiableList(getSession(day, session));
	}
	
	protected List<String> getSession(int day, int session)
	{
		checkDay(day);
		checkSessionForDay(day, session);
		return table.get(day).get(session);
	}
	
	private void checkDay(int day)
	{
		if( !(day >= 0 && day < table.size()) )
		{
			throw new InvalidDayException();
		}
	}
	
	private void checkSessionForDay(int day, int session)
	{
		if( !(session >= 0 && session < table.get(day).size()) )
		{
			throw new InvalidSessionException();
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder();
		for(int i = 0; i < table.size(); i++)
		{
			string.append("Day ");
			string.append(i+1);
			string.append(" sessions:\n");
			string.append(indentString(dayTableToString(table.get(i))));
			string.append('\n');
		}
		return string.toString();
	}
	
	private String dayTableToString(Vector<List<String>> table)
	{
		StringBuilder string = new StringBuilder();
		for(int i = 0; i < table.size(); i++)
		{
			List<String> session = table.get(i);
			
			string.append("S ");
			string.append(i+1);
			string.append("; Speakers: ");
			string.append(session.size());
			string.append(" -> ");
			string.append(sessionLineToString(session));
			string.append('\n');
		}
		return string.toString();
	}
	
	private String sessionLineToString(List<String> table)
	{
		StringBuilder string = new StringBuilder();
		for(int i = 0; i < table.size(); i++)
		{
			string.append(table.get(i));
			string.append("; ");
		}
		return string.toString();
	}
	
	private String indentString(String in)
	{
		String[] lines = in.split("\n");
		StringBuilder string = new StringBuilder();
		for(int i = 0; i < lines.length; i++)
		{
			string.append("  ");
			string.append(lines[i]);
			string.append("\n");
		}
		return string.toString();
	}
}
