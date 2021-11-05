package labRetiAssignments.ex06;

import java.io.IOException;

public class MainClass
{
	private static ServerHandler serverSocket;
	
	//start with arguments: host address, host port, curraent working directory.
	public static void main(String[] args)
	{
		try
		{
			validateArguments(args);
			serverSocket = new ServerHandler(args[2], args[0], Integer.valueOf(args[1]));
			System.out.println("Starting server, press Enter to stop.");
			serverSocket.start();
			
			System.in.read();
			serverSocket.stop();
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private static void validateArguments(String[] args)
	{
		if(args.length != 3)
		{
			throw new IllegalArgumentException("Required arguments: host address, host port, current working directory");
		}
	}
}
