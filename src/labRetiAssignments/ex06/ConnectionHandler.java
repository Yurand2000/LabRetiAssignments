package labRetiAssignments.ex06;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionHandler implements Runnable
{
	private Socket socket;
	private String cwd;
	private BufferedReader character_reader;
	private InputStream data_reader;
	private Writer character_writer;
	private OutputStream data_writer;
	
	public ConnectionHandler(String current_working_directory, Socket socket) throws IOException
	{
		this.cwd = current_working_directory;
		this.socket = socket;
		this.data_reader = new BufferedInputStream(new DataInputStream( socket.getInputStream() ));
		this.character_reader = new BufferedReader(new InputStreamReader( this.data_reader ));		
		this.data_writer = new BufferedOutputStream (new DataOutputStream ( socket.getOutputStream() ));		
		this.character_writer = new OutputStreamWriter( this.data_writer );
	}
	
	@Override
	public void run()
	{
		try
		{
			System.out.println("Connection from: " + socket.getInetAddress().getHostAddress());
			List<String> data = getRequest();
			System.out.println(data);
			HTTPRequest request = new HTTPRequest(data);
			
			if(request.isGetRequest())
			{
				handleGetRequest(request);
			}
			else
			{
				sendNotImplemented();
			}

			System.out.println("Connection closed with: " + socket.getInetAddress().getHostAddress());
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> getRequest() throws IOException
	{
		List<String> requestLines = new LinkedList<String>();
		String line = character_reader.readLine();
		while(line != null && !line.isEmpty())
		{
			requestLines.add(line);
			line = character_reader.readLine();
		}
		
		return requestLines;
	}
	
	private void handleGetRequest(HTTPRequest request) throws IOException
	{
		File file = getRequestedFile(request);
		if(!file.exists())
			sendFileNotFound();
		
		byte[] file_data = readAllFile(file);
		String data_message =
				"HTTP/1.1 200 OK\r\n"
				+ "Accept-Ranges: bytes\r\n"
				+ "Content-Length: " + file_data.length + "\r\n"
				+ "Connection: close\r\n"
				+ "Content-Type: " + getContentType(file) + "\r\n"
				+ "\r\n";
		
		character_writer.write(data_message);
		character_writer.flush();
		data_writer.write(file_data);
		data_writer.flush();
	}
	
	private File getRequestedFile(HTTPRequest request)
	{
		File file = new File(cwd + request.getRequestedFile());		
		if(file.isDirectory())
		{
			return new File(cwd + request.getRequestedFile() + "/index.html");
		}
		else
		{

			return file;
		}
	}
	
	private byte[] readAllFile(File file) throws IOException
	{
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		byte[] data = reader.readAllBytes();
		reader.close();
		return data;
	}
	
	private String getContentType(File file)
	{
		if(file.getName().endsWith(".txt"))
			return "text/plain";
		if(file.getName().endsWith(".css"))
			return "text/css";
		if(file.getName().endsWith(".html"))
			return "text/html";
		if(file.getName().endsWith(".png"))
			return "image/png";
		if(file.getName().endsWith(".jpg"))
			return "image/jpg";
		if(file.getName().endsWith(".gif"))
			return "image/gif";
		else
			return "text/plain";
	}
	
	private void sendNotImplemented() throws IOException
	{
		final String error_message =
			"HTTP/1.1 501 Not Implemented\r\n"
			+ "Connection: close\r\n"
			+ "\r\n";
		
		character_writer.write(error_message);
		character_writer.flush();
	}
	
	private void sendFileNotFound() throws IOException
	{
		final String error_message =
			"HTTP/1.1 404 Requested file not found\r\n"
			+ "Connection: close\r\n"
			+ "\r\n";
		
		character_writer.write(error_message);
		character_writer.flush();
	}
}
