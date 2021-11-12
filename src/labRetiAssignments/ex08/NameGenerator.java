package labRetiAssignments.ex08;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

public class NameGenerator
{
	private final ArrayList<String> names = new ArrayList<String>();
	
	public NameGenerator() { }
	
	public NameGenerator(byte[] serialized_data) throws StreamReadException, DatabindException, IOException
	{
		String[] serialized_names = SerializerWrapper.deserialize(serialized_data, String[].class);
		for(String name : serialized_names)
		{
			names.add(name);
		}
	}
	
	public void addName(String name)
	{
		this.names.add(name);
	}
	
	public ArrayList<String> getNames()
	{
		return names;
	}
	
	public String getRandomName()
	{
		if(names.size() > 0)
		{
			return names.get((int)(Math.random() * names.size()));
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException();
		}
	}
}
