package lesson6.exs;

import java.io.*;
import java.net.*;

public class ex1Main {

	public static void main(String[] args)
	{
		
		try
		{			
			File read_file = new File("./src/lesson6/exs/ex1Main.java");
			BufferedReader reader = new BufferedReader( new FileReader(read_file) );
			StringBuffer file_data = new StringBuffer();
			int character = reader.read();
			while(character != -1)
			{
				file_data.append((char)(character));
				character = reader.read();
			}
			reader.close();
			
			ServerSocket listening_socket = new ServerSocket();
			listening_socket.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), 8080));
			
			while(!Thread.currentThread().isInterrupted())
			{
				Socket sock = listening_socket.accept();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter( sock.getOutputStream() ));
				writer.write(file_data.toString());
				writer.flush();				
				sock.close();
			}
			
			listening_socket.close();
		}	
		catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
