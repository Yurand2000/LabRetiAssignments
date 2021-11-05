package labRetiAssignments.ex06;

import java.util.*;

public class HTTPParser
{
	public static boolean isRequestLine(String line)
	{
		return line.matches("^(OPTIONS|GET|HEAD|POST|PUT|TRACE|DELETE|extension-method) [^ ]+ [^ ]+$");
	}
	
	public static boolean isAcceptLine(String line)
	{
		final String accept_type = "[^ ]+(; [^ ])*"; 
		final String accept_line_regex = "Accept: (" + accept_type + ",)*" + accept_type;
		return line.matches(accept_line_regex);
	}
	
	public static boolean isGetRequest(String line)
	{
		return line.regionMatches(0, "GET ", 0, 4);
	}
	
	public static String getFileRequest(String line)
	{
		line = line.substring(4, line.indexOf(' ', 4));
		int scheme_end = line.indexOf("://", 0);
		if(scheme_end != -1)
		{
			int host_end = line.indexOf("/", scheme_end + 3);
			return line.substring(host_end);
		}
		else
		{
			return line;
		}
	}
	
	public static List<String> getAcceptedTypes(String line)
	{
		List<String> list = new LinkedList<String>();
		line = line.substring(8);
		String[] accepted_types = line.split(", ");
		String type = "";
		for(String type_and_preference : accepted_types)
		{
			int end = type_and_preference.indexOf(';');
			if(end > 0)
			{
				type = type_and_preference.substring(0, end - 1);
			}
			else
			{
				type = type_and_preference;
			}
			
			if(type == "*/*")
			{
				return new LinkedList<String>();
			}
		}
		return list;
	}
}
