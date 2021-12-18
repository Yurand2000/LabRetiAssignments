package labRetiAssignments.ex11;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class ClientMain
{
	private static String[] names = {"Yuri", "Alberto", "Luigi", "Nicola", "Galileo", "Simba", "Alice", "Bob", "Eve"};
	private static int max_days = 3;
	private static int max_sessions = 12;
	private static int max_speakers = 5;
	private static Random random = new Random();
	
	public static void main(String[] args) throws NotBoundException, IOException
	{		
		Registry reg = LocateRegistry.getRegistry(8080);
		CongressHandlerInterface ifc = (CongressHandlerInterface) reg.lookup("congressHandler");
		
		random.setSeed(12568);
		for(int i = 0; i < maxAllowedSpeakers() + 1; i++)
		{
			int day = generateRandomDay();
			int session = generateRandomSession();
			String speaker = generateRandomName();
			tryRegisterSpeaker(ifc, day, session, speaker);
		}
		
		tryRegisterSpeaker(ifc, max_days, 0, "WrongDay");
		tryRegisterSpeaker(ifc, -1, 0, "WrongDay");
		tryRegisterSpeaker(ifc, 0, max_sessions, "WrongSession");
		tryRegisterSpeaker(ifc, 0, -1, "WrongSession");
		
		System.out.println();
		System.out.println(ifc.getProgram().toString());
	}
	
	private static void tryRegisterSpeaker(CongressHandlerInterface ifc, int day, int session, String speaker) throws RemoteException
	{
		try
		{
			ifc.registerNewSpeaker( day, session, speaker );
		}
		catch(MaxSpeakersException e)
		{
			System.out.println("There is already too many people for day " + day + " session " + session + ".");
		}
		catch(InvalidDayException e)
		{
			System.out.println("Given day is invalid. Given value was: " + day + ".");
		}
		catch(InvalidSessionException e)
		{
			System.out.println("Given session for the day " + day + " is invalid. Given value was: " + session + ".");
		}
	}
	
	private static int generateRandomDay()
	{
		return random.nextInt(max_days);
	}
	
	private static int generateRandomSession()
	{
		return random.nextInt(max_sessions);
	}
	
	private static String generateRandomName()
	{
		return names[random.nextInt(names.length)];
	}
	
	private static int maxAllowedSpeakers()
	{
		return max_days * max_sessions * max_speakers;
	}
}
