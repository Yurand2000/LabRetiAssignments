package labRetiAssignments.ex11;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;

public class ClientMain
{
	public static void main(String[] args) throws NotBoundException, IOException
	{		
		RMISocketFactory.setSocketFactory(new MySocketFactory());
		
		Registry reg = LocateRegistry.getRegistry(8080);
		CongressHandlerInterface ifc = (CongressHandlerInterface) reg.lookup("congressHandler");
		ifc.registerNewSpeaker(0, "Yuri");
		ifc.registerNewSpeaker(0, "Alberto");
		
		TestRemoteInterface test1 = ifc.getRemote();
		TestRemoteInterface test2 = ifc.getRemote();
		
		System.out.println(ifc.getProgram().toString());
		
		CongressHandlerInterface ifc2 = (CongressHandlerInterface) reg.lookup("congressHandler");
		ifc2.registerNewSpeaker(0, "Yuri");
		ifc2.registerNewSpeaker(0, "Alberto");
		
		TestRemoteInterface test3 = ifc.getRemote();
		
		System.out.println(ifc2.getProgram().toString());
		
		System.out.println(test1.test());
		System.out.println(test2.test());
		System.out.println(test3.test());
	}
}
