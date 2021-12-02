package labRetiAssignments.ex11;

import java.rmi.RemoteException;
import java.util.LinkedList;

public class CongressHandlerImpl implements CongressHandlerInterface
{
	private CongressTableServer table;
	private LinkedList<TestRemoteImpl> impls = new LinkedList<TestRemoteImpl>();
	
	public CongressHandlerImpl(int sessions, int max_speakers) throws RemoteException
	{
		table = new CongressTableServer(sessions, max_speakers);
	}

	@Override
	public void registerNewSpeaker(int session, String speaker) throws RemoteException
	{
		table.addSpeaker(session, speaker);
	}

	@Override
	public CongressTable getProgram()
	{
		return table;
	}

	@Override
	public TestRemoteInterface getRemote() throws RemoteException 
	{
		TestRemoteImpl remote = new TestRemoteImpl();
		impls.add(remote);
		return (TestRemoteInterface)remote;
	}

}
