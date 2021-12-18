package labRetiAssignments.ex11;

import java.rmi.RemoteException;

public class CongressHandlerImpl implements CongressHandlerInterface
{
	private CongressTableServer table;
	
	public CongressHandlerImpl(int days, int sessions, int max_speakers) throws RemoteException
	{
		table = new CongressTableServer(days, sessions, max_speakers);
	}

	@Override
	public void registerNewSpeaker(int day, int session, String speaker) throws RemoteException
	{
		table.addSpeaker(day, session, speaker);
	}

	@Override
	public CongressTable getProgram()
	{
		return table;
	}
}
