package labRetiAssignments.ex11;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestRemoteInterface extends Remote
{
	public String test() throws RemoteException;
}
