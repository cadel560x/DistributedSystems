package ie.gmit.sw;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;

import ie.gmit.sw.ds.carhire.DatabaseService;
import ie.gmit.sw.ds.carhire.DatabaseServiceImpl;

public class ServiceSetup {

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		DatabaseService ds = new DatabaseServiceImpl();

		// Start the RMI registry on port 1099
		LocateRegistry.createRegistry(1099);

		// Bind our remote object to the registry with the human-readable name
		// "howdayService"
		Naming.rebind("databaseservice", ds);
		
		System.out.println("Server is ready");
	}

}
