package labRetiAssignments.ex06;

import java.util.*;

public class HTTPRequest
{
	private String request_line = null;
	private String accept_line = null;
	
	public HTTPRequest(List<String> lines)
	{		
		for(String line : lines)
		{
			if(HTTPParser.isRequestLine(line))
				request_line = line;
			if(HTTPParser.isAcceptLine(line))
				accept_line = line;
		}
	}
	
	public String getRequestLine()
	{
		return request_line;
	}
	
	public String getAcceptLine()
	{
		return accept_line;
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
