package labRetiAssignments.ex06;

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
		return line.substring(4, line.indexOf(' ', 4));
	}
	
	public static boolean acceptsText(String line)
	{
		return line.contains("text/plain") || line.contains("text/html");
	}
	
	public static boolean acceptsImages(String line)
	{
		return line.contains("image/png") || line.contains("image/jpg") || line.contains("image/gif");
	}
}
