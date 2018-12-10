package ie.gmit.sw.ds.carhire;

//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
//import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
//import javax.xml.namespace.QName;

import ie.gmit.sw.ds.carhire.model.Booking;
import ie.gmit.sw.ds.carhire.model.Car;
import ie.gmit.sw.ds.carhire.model.Country;
import ie.gmit.sw.ds.carhire.model.Customer;
import ie.gmit.sw.ds.carhire.model.ObjectFactory;;

public class Runner {

	public static void main(String[] args) {
		try {
			DatabaseService ds = new DatabaseServiceImpl();
			
			// Booking
			System.out.println("ds.readBookings()");
			
			List<Booking> bookings = ds.readBookings();
			for (Booking booking : bookings) {
				System.out.println(booking.getNumber());
				System.out.println(booking.getDate());
				
				Customer customer = booking.getCustomer();
				System.out.println(customer.getId());
				System.out.println(customer.getFirstName());
				System.out.println(customer.getLastName());
				System.out.println(customer.getMobileNumber());
				System.out.println(customer.getCountry());
				System.out.println(customer.getEmail());
				
				for (Car car: booking.getCar()) {
					System.out.println(car.getId());
					System.out.println(car.getBrand());
					System.out.println(car.getColor());
					System.out.println(car.getModel());
					System.out.println(car.getPurchaseDate());
				}
				
			}
			
			
			System.out.println("\nds.readBooking('AAA1234')");
			
			Booking booking1 = ds.readBooking("AAA1234");
			System.out.println(booking1.getNumber());
			System.out.println(booking1.getDate());
			
			Customer customer = booking1.getCustomer();
			System.out.println(customer.getId());
			System.out.println(customer.getFirstName());
			System.out.println(customer.getLastName());
			System.out.println(customer.getMobileNumber());
			System.out.println(customer.getCountry());
			System.out.println(customer.getEmail());
			
			for (Car car: booking1.getCar()) {
				System.out.println(car.getId());
				System.out.println(car.getBrand());
				System.out.println(car.getColor());
				System.out.println(car.getModel());
				System.out.println(car.getPurchaseDate());
			}
			
			
			System.out.println("\nds.createBooking('AAB1234')");
			ObjectFactory objectFactory = new ObjectFactory();
			Booking newBooking = objectFactory.createBooking();
			Customer newCustomer = objectFactory.createCustomer();
			Car newCar = objectFactory.createCar();
			
			
			newBooking.setNumber("AAB1234");
			String newBookingDateString = "2017-04-09";
			Date newBookingDate= new Date(new SimpleDateFormat("yyyy-MM-dd").parse(newBookingDateString).getTime()); 
			GregorianCalendar newBookingCal = new GregorianCalendar();
  	        newBookingCal.setTime(newBookingDate);
  	        XMLGregorianCalendar newBookingXMLGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(newBookingCal);
  	        newBooking.setDate(newBookingXMLGC);
			
			newCustomer.setId(1);
			newCustomer.setFirstName("Tom");
			newCustomer.setLastName("Kelly");
			newCustomer.setMobileNumber("0893672867");
			newCustomer.setEmail("TomKelly@yahoo.ie");
			newCustomer.setCountry(Country.IRELAND);
			
			newBooking.setCustomer(newCustomer);
			
			newCar.setId(2);
			newCar.setBrand("Ford Fiesta");
			newCar.setColor("red");
			newCar.setModel("Fiesta");
			String newCarDateString = "2018-12-09";
			Date newCarDate=new Date(new SimpleDateFormat("yyyy-MM-dd").parse(newCarDateString).getTime()); 
			GregorianCalendar newCarCal = new GregorianCalendar();
  	        newCarCal.setTime(newCarDate);
  	        XMLGregorianCalendar carXMLGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(newCarCal);
			newCar.setPurchaseDate(carXMLGC);
			
			newBooking.getCar().add(newCar);
			
			ds.createBooking(newBooking);
			
			
			System.out.println("\nds.readBooking('AAB1234')");
			
			Booking booking3 = ds.readBooking("AAB1234");
			System.out.println(booking3.getNumber());
			System.out.println(booking3.getDate());
			
			Customer customer2 = booking3.getCustomer();
			System.out.println(customer2.getId());
			System.out.println(customer2.getFirstName());
			System.out.println(customer2.getLastName());
			System.out.println(customer2.getMobileNumber());
			System.out.println(customer2.getCountry());
			System.out.println(customer2.getEmail());
			
			for (Car car: booking3.getCar()) {
				System.out.println(car.getId());
				System.out.println(car.getBrand());
				System.out.println(car.getColor());
				System.out.println(car.getModel());
				System.out.println(car.getPurchaseDate());
			}
			
			
			try {
				System.out.println("\nds.deleteBooking('AAB1234')");
				ds.deleteBooking(newBooking);
				
				System.out.println("\nds.readBooking('AAB1234')");
				
				Booking booking4 = ds.readBooking("AAB1234");
				System.out.println(booking4.getNumber());
				System.out.println(booking4.getDate());
				
				Customer customer3 = booking4.getCustomer();
				System.out.println(customer3.getId());
				System.out.println(customer3.getFirstName());
				System.out.println(customer3.getLastName());
				System.out.println(customer3.getMobileNumber());
				System.out.println(customer3.getCountry());
				System.out.println(customer3.getEmail());
				
				for (Car car: booking4.getCar()) {
					System.out.println(car.getId());
					System.out.println(car.getBrand());
					System.out.println(car.getColor());
					System.out.println(car.getModel());
					System.out.println(car.getPurchaseDate());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			// Cars
			System.out.println("ds.readCars()");
			
			List<Car> cars = ds.readCars();
			for (Car car : cars) {
				System.out.println(car.getId());
				System.out.println(car.getBrand());
				System.out.println(car.getColor());
				System.out.println(car.getModel());
				System.out.println(car.getPurchaseDate());
			}
			
			System.out.println("\nds.createCar(newCar1)");
			Car newCar1 = objectFactory.createCar();
			newCar1.setId(4);
			newCar1.setBrand("Mazda 626");
			newCar1.setColor("yellow");
			newCar1.setModel("626");
			String newCarDateString2 = "1988-10-19";
			Date newCarDate2=new Date(new SimpleDateFormat("yyyy-MM-dd").parse(newCarDateString2).getTime()); 
			GregorianCalendar newCarCal2 = new GregorianCalendar();
  	        newCarCal.setTime(newCarDate2);
  	        XMLGregorianCalendar carXMLGC2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(newCarCal2);
			newCar1.setPurchaseDate(carXMLGC2);
			ds.createCar(newCar1);
		
			System.out.println("\nds.readCar(4)");
			Car car = ds.readCar(4);
			System.out.println(car.getId());
			System.out.println(car.getBrand());
			System.out.println(car.getColor());
			System.out.println(car.getModel());
			System.out.println(car.getPurchaseDate());
			
			System.out.println("\nds.updateCar(newCar1)");
			newCar1.setModel("323");
			newCar1.setBrand("Mazda 323");
			ds.updateCar(newCar1);
			car = ds.readCar(4);
			System.out.println(car.getId());
			System.out.println(car.getBrand());
			System.out.println(car.getColor());
			System.out.println(car.getModel());
			System.out.println(car.getPurchaseDate());
			
			System.out.println("\nds.deleteCar(newCar1)");
			ds.deleteCar(newCar1);
			try {
			System.out.println("\nds.readCar(4)");
			car = ds.readCar(4);
			System.out.println(car.getId());
			System.out.println(car.getBrand());
			System.out.println(car.getColor());
			System.out.println(car.getModel());
			System.out.println(car.getPurchaseDate());
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			// Customers
			System.out.println("\nds.createCustomer(4)");
			newCustomer.setId(4);
			newCustomer.setFirstName("Gumercindo");
			newCustomer.setLastName("Kelly");
			newCustomer.setMobileNumber("0897777777");
			newCustomer.setEmail("GumercindoKelly@yahoo.ie");
			newCustomer.setCountry(Country.fromValue("Ireland"));
			ds.createCustomer(newCustomer);
			
			System.out.println("\nds.readCustomer(4)");
			Customer customer1 = ds.readCustomer(4);
			System.out.println(customer1.getId());
			System.out.println(customer1.getFirstName());
			System.out.println(customer1.getLastName());
			System.out.println(customer1.getMobileNumber());
			System.out.println(customer1.getCountry());
			System.out.println(customer1.getEmail());
			
			System.out.println("\nds.readCustomers()");
			List<Customer> customers = ds.readCustomers();
			for (Customer customer3 : customers) {
				System.out.println(customer3.getId());
				System.out.println(customer3.getFirstName());
				System.out.println(customer3.getLastName());
				System.out.println(customer3.getMobileNumber());
				System.out.println(customer3.getCountry());
				System.out.println(customer3.getEmail());
			}
			
			System.out.println("\nds.updateCustomer(customer1)");
			customer1.setLastName("Carrascal");
			customer1.setCountry(Country.fromValue("Colombia"));
			customer1 = ds.updateCustomer(customer1);
			System.out.println(customer1.getId());
			System.out.println(customer1.getFirstName());
			System.out.println(customer1.getLastName());
			System.out.println(customer1.getMobileNumber());
			System.out.println(customer1.getCountry());
			System.out.println(customer1.getEmail());
			
			System.out.println("\nds.deleteCustomer(customer1)");
			ds.deleteCustomer(customer1);
			try {
				System.out.println("\nds.readCustomer(4)");
				customer1 = ds.readCustomer(4);
				System.out.println(customer1.getId());
				System.out.println(customer1.getFirstName());
				System.out.println(customer1.getLastName());
				System.out.println(customer1.getMobileNumber());
				System.out.println(customer1.getCountry());
				System.out.println(customer1.getEmail());
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} // end try - catch
		

	} // end method

} // end class
