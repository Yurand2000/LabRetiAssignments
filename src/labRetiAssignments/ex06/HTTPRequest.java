package labRetiAssignments.ex06;

import java.util.*;

public class HTTPRequest
{
	private String request_line = null;
	private String accept_line = null;
	
	public HTTPRequest(List<String> lines)
	{
		if(HTTPParser.isRequestLine(lines.get(0)))
		{
			request_line = lines.get(0);
		}
		
		for(String line : lines)
		{
			if(HTTPParser.isAcceptLine(line))
			{
				accept_line = line;
			}
		}
	}
	
	public String getRequestLine()
	{
		return request_line;
	}
	
	public List<String> getRequestedTypes()
	{
		if(accept_line != null)
			return HTTPParser.getAcceptedTypes(accept_line);
		else
			return new LinkedList<String>();
	}
	
	public boolean isValidRequest()
	{
		return request_line != null;
	}
	
	public boolean isGetRequest()
	{
		return HTTPParser.isGetRequest(request_line);
	}
	
	public String getRequestedFile()
	{
		return HTTPParser.getFileRequest(request_line);
	}
}
