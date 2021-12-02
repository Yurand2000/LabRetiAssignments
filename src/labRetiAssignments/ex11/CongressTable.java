package labRetiAssignments.ex11;

import java.io.Serializable;
import java.util.*;

public class CongressTable implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected final Vector<List<String>> table;
	
	public CongressTable()
	{
		table = new Vector<List<String>>();
	}
	
	public List<String> getSpeakers(int session)
	{
		return Collections.unmodifiableList(getLine(session));
	}
	
	protected List<String> getLine(int session)
	{
		if(session >= 0 && session < table.size())
		{
			return table.get(session);
		}
		else
		{
			throw new RuntimeException("Given session number is invalid.");
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder();
		for(int i = 0; i < table.size(); i++)
		{
			string.append("* Session ");
			string.append(i);
			string.append(": ");
			List<String> line = table.get(i);
			for(int j = 0; j < line.size(); j++)
			{
				string.append(line.get(j));
				string.append("; ");
			}
			string.append('\n');
		}
		return string.toString();
	}
}
