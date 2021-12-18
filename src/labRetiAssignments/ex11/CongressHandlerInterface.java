package labRetiAssignments.ex11;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CongressHandlerInterface extends Remote
{
	public void registerNewSpeaker(int day, int session, String speaker) throws RemoteException;
	public CongressTable getProgram() throws RemoteException;
}
