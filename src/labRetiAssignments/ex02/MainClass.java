package labRetiAssignments.ex02;

public class MainClass {

	private static int clienti = 5000;
	
	public static void main(String[] args) {
		UfficioPostale ufficio = new UfficioPostale();
		
		ufficio.apriUfficioPostale();		
		for(int i = 0; i < clienti; i++)
			ufficio.entraUnNuovoCliente();
		ufficio.chiudiUfficioPostale();
	}

}
