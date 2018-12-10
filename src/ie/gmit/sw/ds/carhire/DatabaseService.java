package ie.gmit.sw.ds.carhire;

import java.rmi.*;
import java.util.List;

import ie.gmit.sw.ds.carhire.model.Booking;
import ie.gmit.sw.ds.carhire.model.Car;
import ie.gmit.sw.ds.carhire.model.Customer;

public interface DatabaseService extends Remote {

	public Booking createBooking(Booking booking) throws RemoteException;

	public Booking readBooking(String number) throws RemoteException;
	
	public List<Booking> readBookings() throws RemoteException;

	public Booking updateBooking(Booking booking) throws RemoteException;

	public void deleteBooking(Booking booking) throws RemoteException;
	
	
	public Car createCar(Car car) throws RemoteException;

	public Car readCar(int id) throws RemoteException;
	
	public List<Car> readCars() throws RemoteException;

	public Car updateCar(Car car) throws RemoteException;

	public void deleteCar(Car car) throws RemoteException;
	
	
	public Customer createCustomer(Customer customer) throws RemoteException;

	public Customer readCustomer(int id) throws RemoteException;
	
	public List<Customer> readCustomers() throws RemoteException;

	public Customer updateCustomer(Customer customer) throws RemoteException;

	public void deleteCustomer(Customer customer) throws RemoteException;
	
}
