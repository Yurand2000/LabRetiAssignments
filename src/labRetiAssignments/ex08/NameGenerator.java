package labRetiAssignments.ex08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

public class NameGenerator
{
	private List<String> names;
	
	public NameGenerator(String path) throws StreamReadException, DatabindException, IOException
	{
		this( Files.readAllBytes(Paths.get(path)) );
	}
	
	public NameGenerator(byte[] serialized_data) throws StreamReadException, DatabindException, IOException
	{
		names = SerializerWrapper.deserialize(serialized_data);
	}
	
	public void addName(String name)
	{
		names.add(name);
	}
	
	public List<String> getNames()
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
			throw new IllegalStateException("No names are saved into the generator");
		}
	}
}
