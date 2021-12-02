package labRetiAssignments.ex11;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain
{
	private static int sessions = 12;
	private static int max_speakers = 5;	
	
	public static void main(String[] args) throws AlreadyBoundException, IOException, NotBoundException
	{
		RMISocketFactory.setSocketFactory(new MySocketFactory());
		
		CongressHandlerImpl handler = new CongressHandlerImpl(sessions, max_speakers);
		UnicastRemoteObject.setLog(System.out);
		CongressHandlerInterface stub = (CongressHandlerInterface) UnicastRemoteObject.exportObject(handler, 8081);
		Registry reg = LocateRegistry.createRegistry(8080);
		reg.bind("congressHandler", stub);

		System.out.println("Server Started.");
		System.in.read();
		System.out.println("Server Terminating.");

		reg.unbind("congressHandler");
		UnicastRemoteObject.unexportObject(handler, false);		
	}
}
