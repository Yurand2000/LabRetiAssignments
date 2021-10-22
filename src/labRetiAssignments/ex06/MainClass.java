package labRetiAssignments.ex06;

import java.io.IOException;

public class MainClass
{
	private static ServerHandler serverSocket;
	
	public static void main(String[] args)
	{
		try
		{
			serverSocket = new ServerHandler(args[0]);
			serverSocket.start();
			
			System.in.read();
			serverSocket.stop();
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
