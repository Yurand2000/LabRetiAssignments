package labRetiAssignments.ex06;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConnectionHandler implements Runnable
{
	private Socket socket;
	private String cwd;
	private BufferedReader reader;
	private OutputStream writer;
	
	public ConnectionHandler(String current_working_directory, Socket socket) throws IOException
	{
		this.cwd = current_working_directory;
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader( socket.getInputStream() ));	
		this.writer = new DataOutputStream(new BufferedOutputStream( socket.getOutputStream() ));
	}
	
	@Override
	public void run()
	{
		try
		{
			notifyNewConnection();
			handleRequest(getRequest());
			notifyCloseConnection();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private HTTPRequest getRequest() throws IOException
	{
		List<String> requestLines = new LinkedList<String>();
		String line = reader.readLine();
		while(line != null && !line.isEmpty())
		{
			requestLines.add(line);
			line = reader.readLine();
		}
		
		return new HTTPRequest(requestLines);
	}
	
	private void notifyNewConnection()
	{
		synchronized(System.out.getClass())
		{
			System.out.println("** \'" + Thread.currentThread().getName() + "\' Connection from: " + socket.getInetAddress().getHostAddress() + " **");
		}
	}
	
	private void notifyCloseConnection()
	{
		synchronized(System.out.getClass())
		{
			System.out.println("** \'" + Thread.currentThread().getName() + "\' Connection closed with: " + socket.getInetAddress().getHostAddress() + " **");
		}
	}
	
	private void handleRequest(HTTPRequest request) throws IOException
	{
		if(request.isValidRequest())
		{
			if(request.isGetRequest())
			{
				handleGetRequest(request);
			}
			else
			{
				sendNotImplemented();
			}
		}
		else
		{
			sendBadRequest();
		}
		socket.close();
	}
	
	private void handleGetRequest(HTTPRequest request) throws IOException
	{
		try
		{
			File file = getRequestedFile(request);
			validateFileAccepted(request, file);
			byte[] file_data = readFile(file);
			String message = buildMessage(200, "OK", file_data.length, getContentType(file));
			
			sendData(message.getBytes(StandardCharsets.UTF_8));
			sendData(file_data);
		}
		catch(FileNotFoundException e)
		{
			sendFileNotFound();
		}
		catch(IOException e)
		{
			sendFileNotFound();
		}
		catch(Exception e)
		{
			synchronized(System.err.getClass())
			{
				System.err.println("** \'" + Thread.currentThread().getName() + "\' Error in handling get request: " + e.getMessage());
				e.printStackTrace();
			}
			sendGenericError();
		}
	}
	
	private void validateFileAccepted(HTTPRequest request, File file) throws FileNotFoundException
	{
		String type = getContentType(file);
		List<String> requested_types = request.getRequestedTypes();
		if(!requested_types.isEmpty() && (type == null || !requested_types.contains(type)))
		{
			throw new FileNotFoundException("Given file type is not accepted by the client.");
		}
	}
	
	private File getRequestedFile(HTTPRequest request) throws FileNotFoundException
	{
		File file = getFileFromFilename(request.getRequestedFile());
		validateFileExistance(file);
		return file;
	}
	
	private File getFileFromFilename(String filename)
	{
		File file = new File(cwd + filename);		
		if(file.isDirectory())
		{
			file = new File(cwd + filename + "/index.html");
		}

		return file;
	}
	
	private void validateFileExistance(File file) throws FileNotFoundException
	{
		if(!file.exists())
		{
			throw new FileNotFoundException();
		}
	}
	
	private byte[] readFile(File file) throws IOException
	{
		if(file.length() <= Integer.MAX_VALUE)
		{
			BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
			byte[] data = new byte[(int)(file.length())];
			reader.read(data);
			reader.close();
			return data;
		}
		else
		{
			throw new IOException("File size is too big");
		}
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
		if(file.getName().endsWith(".jpeg"))
			return "image/jpg";
		if(file.getName().endsWith(".gif"))
			return "image/gif";
		else
			return null;
	}
	
	private void sendNotImplemented() throws IOException
	{
		String error_message = buildMessage(501, "Not Implemented");
		sendData(error_message.getBytes(StandardCharsets.UTF_8));
	}
	
	private void sendFileNotFound() throws IOException
	{
		byte[] error_txt_message = "SORRY, FILE NOT FOUND!".getBytes(StandardCharsets.UTF_8);
		String error_message = buildMessage(404, "Requested file not found", error_txt_message.length, "text/plain");		
		sendData(error_message.getBytes(StandardCharsets.UTF_8));
		sendData(error_txt_message);
	}
	
	private void sendGenericError() throws IOException
	{
		String error_message = buildMessage(500, "Internal Server Error");
		sendData(error_message.getBytes(StandardCharsets.UTF_8));
	}
	
	private void sendBadRequest() throws IOException
	{
		String error_message = buildMessage(400, "Bad Request");
		sendData(error_message.getBytes(StandardCharsets.UTF_8));
	}
	
	private void sendData(byte[] data) throws IOException
	{
		writer.write(data, 0, data.length);
		writer.flush();
	}
	
	private String buildMessage(int response_code, String response_message)
	{
		return buildMessage(response_code, response_message, -1, null);
	}
	
	private String buildMessage(int response_code, String response_message, int response_data_len, String response_data_type)
	{
		StringBuffer message = new StringBuffer("HTTP/1.1 " + response_code + " " + response_message + "\r\n");
		
		if(response_data_len > 0)
		{
			message.append(
				"Accept-Ranges: none\r\n" +
				"Content-Length: " + response_data_len + "\r\n"
			);
			
			if(response_data_type != null)
			{
				message.append("Content-Type: " + response_data_type + "\r\n");
			}
		}
		
		message.append(
			"Connection: close\r\n"
			+ "\r\n"
		);
		
		return message.toString();
	}
}
