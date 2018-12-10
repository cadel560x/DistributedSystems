package ie.gmit.sw.ds.carhire;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ie.gmit.sw.ds.carhire.model.Car;
import ie.gmit.sw.ds.carhire.model.Country;
import ie.gmit.sw.ds.carhire.model.Customer;
import ie.gmit.sw.ds.carhire.model.ObjectFactory;
import ie.gmit.sw.ds.carhire.model.Booking;

public class DatabaseServiceImpl extends UnicastRemoteObject implements DatabaseService, Serializable {
	private static final long serialVersionUID = 1L;
	
//	Data members
	private Connection connection = null;
	private Statement stmt;
	
	private ObjectFactory objectFactory;
	

	
	
// Constructors
	public DatabaseServiceImpl() throws RemoteException {
//		super();
		objectFactory = new ObjectFactory();
		
        try {
            if( connection == null )
//                connection = DriverManager.getConnection("jdbc:mysql://localhost/carhire?user=carhire&password=carhire");
            		connection = DriverManager.getConnection("jdbc:h2:mem:carhire;DB_CLOSE_DELAY=-1", "", "");
            	    stmt = connection.createStatement();
            	    String sql = "CREATE TABLE IF NOT EXISTS customers (\r\n" + 
            	    		"    id int NOT NULL,\r\n" + 
            	    		"    first_name VARCHAR(255) NOT NULL,\r\n" + 
            	    		"    last_name VARCHAR(255) NOT NULL,\r\n" + 
            	    		"    mobile VARCHAR(255),\r\n" + 
            	    		"    country VARCHAR(255),\r\n" + 
            	    		"    email VARCHAR(255),\r\n" + 
            	    		"    PRIMARY KEY (id)\r\n" + 
            	    		")";
            	    stmt.execute(sql);
            	    
            	    	sql ="CREATE TABLE IF NOT EXISTS cars(\r\n" + 
            	    		"	id int NOT NULL,\r\n" + 
            	    		"	color VARCHAR(255) NOT NULL,\r\n" + 
            	    		"	brand VARCHAR(255) NOT NULL,\r\n" + 
            	    		"	model VARCHAR(255) NOT NULL,\r\n" + 
            	    		"	purchase_date DATE,\r\n" + 
            	    		"	PRIMARY KEY (id)\r\n" + 
            	    		")";
            	    	stmt.execute(sql);
            	    	
            	    	sql ="CREATE TABLE IF NOT EXISTS bookings (\r\n" + 
            	    		"    number VARCHAR(255) NOT NULL,\r\n" + 
            	    		"    date DATE Not Null,\r\n" + 
            	    		"    cust_id int DEFAULT -1,\r\n" + 
            	    		"    car_id int DEFAULT -1,\r\n" + 
            	    		"    PRIMARY KEY (number, car_id),\r\n" + 
            	    		"    FOREIGN KEY (cust_id) REFERENCES customers(id) ON DELETE SET DEFAULT,\r\n" + 
            	    		"	 FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE SET DEFAULT\r\n" + 
            	    		")";
            	    	stmt.execute(sql);
            	    		
            	    	sql=	"INSERT INTO customers (id, first_name, last_name, mobile, country, email)\r\n" + 
            	    		"VALUES (1,'Tom','Kelly','0893672867','Ireland','TomKelly@yahoo.ie'),\r\n" + 
            	    		"	   (2,'John','Kelly','0896757835','Ireland','JohnKelly@yahoo.ie'),\r\n" + 
            	    		"	   (3,'Mark','Healy','0896757835','Ireland','Mark@gmail.com');\r\n" + 
            	    		"\r\n" + 
            	    		"	   \r\n" + 
            	    		"	\r\n" + 
            	    		"INSERT INTO cars (id, color, brand, model, purchase_date)\r\n" + 
            	    		"VALUES (1,'blue','Hyundai I10','I10',CURDATE()),\r\n" + 
            	    		"	   (2,'red','Ford Fiesta ','Fiesta',CURDATE()),\r\n" + 
            	    		"	   (3,'white','Hyundai Tucson','Tucson',CURDATE());\r\n" + 
            	    		"	   \r\n" + 
            	    		"\r\n" + 
            	    		"\r\n" + 
            	    		"INSERT INTO bookings (number, date, cust_id, car_id)\r\n" + 
            	    		"VALUES ('AAA1234',CURDATE(),1,1),\r\n" + 
            	    		"	   ('AAA5678',CURDATE(),2,2),\r\n" + 
            	    		"	   ('AAA9101',CURDATE(),3,3);";
            	    
            	    stmt.execute(sql);
            	    
        } catch (Exception e) {
 
            e.printStackTrace();
             
        }
        
	}
	
	
	
	
//	Other methods
	@Override
	public Booking createBooking(Booking booking) throws RemoteException {
		Booking newBooking = objectFactory.createBooking();
		try {
			String newNumber = booking.getNumber();
			newBooking.setNumber(newNumber);
			
			XMLGregorianCalendar newDate = booking.getDate();
			newBooking.setDate(newDate);
			
			Customer bookingCustomer = booking.getCustomer();
			Customer newCustomer = objectFactory.createCustomer();
			newCustomer.setId(bookingCustomer.getId());
			newCustomer.setFirstName(bookingCustomer.getFirstName());
			newCustomer.setLastName(bookingCustomer.getLastName());
			newCustomer.setMobileNumber(bookingCustomer.getMobileNumber());
			newCustomer.setEmail(bookingCustomer.getEmail());
			newCustomer.setCountry(bookingCustomer.getCountry());
			
			newBooking.setCustomer(newCustomer);
			
			Car newCar;
			stmt = connection.createStatement();
			for (Car bookingCar: booking.getCar()) {
				newCar = objectFactory.createCar();
				
				newCar.setId(bookingCar.getId());
				newCar.setBrand(bookingCar.getBrand());
				newCar.setColor(bookingCar.getColor());
				newCar.setModel(bookingCar.getModel());
				newCar.setPurchaseDate(bookingCar.getPurchaseDate());
				
				String sql = "INSERT INTO bookings (number, date, cust_id, car_id) "+
				"VALUES ('" + newBooking.getNumber() + "', '" +
				new SimpleDateFormat("yyyy-MM-dd").format(newBooking.getDate().toGregorianCalendar().getTime()).toString() + "', " +
				newBooking.getCustomer().getId() + ", " +
				newCar.getId() + ")";
				
				newBooking.getCar().add(newCar);
				
				stmt.execute(sql);
			
			} // end loop
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
			
		} // end try - catch
		
		return newBooking;
		
	} // end method
	
	
	@Override
	public Booking readBooking(String number) throws RemoteException {
		Booking booking = null;
		try {
			stmt = connection.createStatement();
			
			booking = objectFactory.createBooking();
			ResultSet resultSetCustomer = null;
            int custId;
            Customer customer;
            
            ResultSet resultSetCar = null;
            Car car;
			
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM bookings WHERE number='" + number + "'");
			if( resultSet.next() ) {
     	       booking = new Booking();
     	       booking.setNumber(resultSet.getString("number"));
     	       
     	       java.sql.Date d =resultSet.getDate("date");    
     	       GregorianCalendar cal = new GregorianCalendar();
     	       cal.setTime(d);
     	       XMLGregorianCalendar gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
     	       booking.setDate(gc);
     	       
     	       custId = Integer.parseInt(resultSet.getString("cust_id"));
     	       customer = new Customer();
     	       resultSetCustomer = connection.createStatement().executeQuery("SELECT * FROM customers WHERE id=" + custId);
     	       if(resultSetCustomer.next()) {
         	       
         	       customer.setId(resultSetCustomer.getInt("id"));
         	       customer.setFirstName(resultSetCustomer.getString("first_name"));
         	       customer.setLastName(resultSetCustomer.getString("last_name"));
         	       customer.setMobileNumber(resultSetCustomer.getString("mobile"));
         	       
         	       Country country =  Country.fromValue(resultSetCustomer.getString("country"));
         	       customer.setCountry(country);
         	       
         	       customer.setEmail(resultSetCustomer.getString("email"));
         	       
     	       } // end if
     	       
     	       booking.setCustomer(customer);
     	       
     	       
     	       resultSetCar = connection.createStatement().executeQuery("SELECT c.* FROM cars c, bookings b WHERE c.id = b.car_id AND b.number='" + booking.getNumber() + "'");
     	       while(resultSetCar.next()) {
     	    	       car = objectFactory.createCar();
	        	       car.setId(resultSetCar.getInt("id"));
	        	       car.setBrand(resultSetCar.getString("brand"));
	        	       car.setModel(resultSetCar.getString("model"));
	        	       car.setColor(resultSetCar.getString("color"));
	        	       
	        	       java.sql.Date carDate = resultSetCar.getDate("purchase_date");
         	       GregorianCalendar carCal = new GregorianCalendar();
         	       carCal.setTime(carDate);
         	       XMLGregorianCalendar carGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
	        	       car.setPurchaseDate(carGC);
	        	       
	        	       booking.getCar().add(car);
	        	       
     	       } // end loop
     	       
			} // end if
			
			stmt.close();
			
		} catch ( Exception e) {
			e.printStackTrace();
			return null;
		} // end try - catch
		
		return booking;
		
	} // end method
	
	
	@Override
	public List<Booking> readBookings() throws RemoteException {
		List<Booking> bookings = new LinkedList<Booking>();
		
        try {
        		  stmt = connection.createStatement();
               ResultSet resultSet = stmt.executeQuery("SELECT * FROM bookings");
               
               ResultSet resultSetCustomer = null;
               int custId;
               Customer customer;
               
               ResultSet resultSetCar = null;
               Car car;
               
               Booking booking = null;
               while( resultSet.next() ) {
            	       booking = objectFactory.createBooking();
            	       booking.setNumber(resultSet.getString("number"));
            	       
            	       java.sql.Date d =resultSet.getDate("date");    
            	       GregorianCalendar cal = new GregorianCalendar();
            	       cal.setTime(d);
            	       XMLGregorianCalendar gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            	       booking.setDate(gc);
            	       
            	       custId = Integer.parseInt(resultSet.getString("cust_id"));
            	       customer = new Customer();
            	       resultSetCustomer = connection.createStatement().executeQuery("SELECT * FROM customers WHERE id=" + custId);
            	       if(resultSetCustomer.next()) {
	            	       
	            	       customer.setId(resultSetCustomer.getInt("id"));
	            	       customer.setFirstName(resultSetCustomer.getString("first_name"));
	            	       customer.setLastName(resultSetCustomer.getString("last_name"));
	            	       customer.setMobileNumber(resultSetCustomer.getString("mobile"));
	            	       
	            	       Country country =  Country.fromValue(resultSetCustomer.getString("country"));
	            	       customer.setCountry(country);
	            	       
	            	       customer.setEmail(resultSetCustomer.getString("email"));
            	       }
            	       booking.setCustomer(customer);
            	       
            	       
            	       resultSetCar = connection.createStatement().executeQuery("SELECT c.* FROM cars c, bookings b WHERE c.id = b.car_id AND b.number='" + booking.getNumber() + "'");
            	       while(resultSetCar.next()) {
            	    	       car = new Car();
		        	       car.setId(resultSetCar.getInt("id"));
		        	       car.setBrand(resultSetCar.getString("brand"));
		        	       car.setModel(resultSetCar.getString("model"));
		        	       car.setColor(resultSetCar.getString("color"));
		        	       
		        	       java.sql.Date carDate = resultSetCar.getDate("purchase_date");
	            	       GregorianCalendar carCal = new GregorianCalendar();
	            	       carCal.setTime(carDate);
	            	       XMLGregorianCalendar carXMLGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		        	       car.setPurchaseDate(carXMLGC);
		        	       
		        	       booking.getCar().add(car);
            	       }
            	       
            	       bookings.add(booking);
            	       
               } // end loop
               
               resultSet.close();
               stmt.close();
               
               resultSetCustomer.close();
               resultSetCar.close();
                
//               closeConnection();
               
           } catch (Exception e) {
               e.printStackTrace();
               
           } // end try - catch
        
//           System.out.println(bookings);
           return bookings;
		
	} // end method

	
	@Override
	public Booking updateBooking(Booking booking) throws RemoteException {
		Booking updatedBooking = null;
		try {
			stmt = connection.createStatement();
			deleteBooking(booking);
			updatedBooking = createBooking(booking);
			
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return updatedBooking;
		
	} // end method

	
	@Override
	public void deleteBooking(Booking booking) throws RemoteException {
		try {
			stmt = connection.createStatement();
			stmt.execute("DELETE FROM bookings WHERE number='" + booking.getNumber() + "'");
			
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	} // end method


	@Override
	public Car createCar(Car car) throws RemoteException {
		Car newCar = null;
		
		try {
			stmt = connection.createStatement();
			
			newCar = objectFactory.createCar();
			newCar.setId(car.getId());
			newCar.setBrand(car.getBrand());
			newCar.setColor(car.getColor());
			newCar.setModel(car.getModel());
			newCar.setPurchaseDate(car.getPurchaseDate());
			
			String sql = "INSERT INTO cars (id, brand, color, model, purchase_date) "+
					"VALUES (" + newCar.getId() + ", '" +
					newCar.getBrand() + "', '" +
					newCar.getColor() + "', '" +
					newCar.getModel() + "', '" +
					new SimpleDateFormat("yyyy-MM-dd").format(newCar.getPurchaseDate().toGregorianCalendar().getTime()).toString() + "')";
					
					stmt.execute(sql);
					
					stmt.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			
			return null;
		} // end try - catch
		
		return newCar;
		
	} // end method


	@Override
	public Car readCar(int id) throws RemoteException {
		Car car = null;
		
        try {
        		   stmt = connection.createStatement();
               ResultSet resultSet = stmt.executeQuery("SELECT * FROM cars WHERE id=" + id);
               
               if(resultSet.next()) {
		    	       car = objectFactory.createCar();
		    	       car.setId(resultSet.getInt("id"));
		    	       car.setBrand(resultSet.getString("brand"));
		    	       car.setModel(resultSet.getString("model"));
		    	       car.setColor(resultSet.getString("color"));
		    	       
		    	       java.sql.Date carDate = resultSet.getDate("purchase_date");
		    	       GregorianCalendar carCal = new GregorianCalendar();
		    	       carCal.setTime(carDate);
		    	       XMLGregorianCalendar carXMLGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(carCal);
		    	       car.setPurchaseDate(carXMLGC);

               } // end if

           resultSet.close();
           stmt.close();
                
           } catch (Exception e) {
               e.printStackTrace();
               
               return null;
            		   
           } // end try - catch
        
           return car;
           
	} // end method


	@Override
	public List<Car> readCars() throws RemoteException {
		List<Car> cars = new LinkedList<Car>();
        try {
        		   stmt = connection.createStatement();	
               ResultSet resultSet = stmt.executeQuery("SELECT * FROM cars");
               
               Car car = null;
               while(resultSet.next()) {
		    	       car = objectFactory.createCar();
		    	       car.setId(resultSet.getInt("id"));
		    	       car.setBrand(resultSet.getString("brand"));
		    	       car.setModel(resultSet.getString("model"));
		    	       car.setColor(resultSet.getString("color"));
		    	       
		    	       java.sql.Date carDate = resultSet.getDate("purchase_date");
		    	       GregorianCalendar carCal = new GregorianCalendar();
		    	       carCal.setTime(carDate);
		    	       XMLGregorianCalendar carXMLGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(carCal);
		    	       car.setPurchaseDate(carXMLGC);
		    	       
		    	       cars.add(car);

               } // end loop
		       

           resultSet.close();
           stmt.close();
                
           } catch (Exception e) {
               e.printStackTrace();
               
               return null;
            		   
           } // end try - catch
        
           return cars;
           
	} // end method


	@Override
	public Car updateCar(Car car) throws RemoteException {
		Car updatedCar = null;
		try {
			stmt = connection.createStatement();
			deleteCar(car);
			updatedCar = createCar(car);
			
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return updatedCar;
		
	} // end method


	@Override
	public void deleteCar(Car car) throws RemoteException {
		try {
			stmt = connection.createStatement();
			stmt.execute("DELETE FROM cars WHERE id='" + car.getId() + "'");
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	} // end method


	@Override
	public Customer createCustomer(Customer customer) throws RemoteException {
		Customer newCustomer = null;
		
		try {
			stmt = connection.createStatement();
			
			newCustomer = objectFactory.createCustomer();
			newCustomer.setId(customer.getId());
			newCustomer.setFirstName(customer.getFirstName());
			newCustomer.setLastName(customer.getLastName());
			newCustomer.setMobileNumber(customer.getMobileNumber());
			newCustomer.setEmail(customer.getEmail());
			newCustomer.setCountry(customer.getCountry());
			
			String sql = "INSERT INTO customers (id, first_name, last_name, mobile, email, country) "+
					"VALUES (" + newCustomer.getId() + ", '" +
					newCustomer.getFirstName() + "', '" +
					newCustomer.getLastName() + "', '" +
					newCustomer.getMobileNumber() + "', '" +
					newCustomer.getEmail() + "', '" +
					newCustomer.getCountry().value() + "')";
					
			stmt.execute(sql);
			
			stmt.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			
			return null;
		} // end try - catch
		
		return newCustomer;
		
	} // end method


	@Override
	public Customer readCustomer(int id) throws RemoteException {
	       Customer newCustomer = null;
			
			try {
			   stmt = connection.createStatement();
			   ResultSet resultSetCustomer = connection.createStatement().executeQuery("SELECT * FROM customers WHERE id=" + id);
		       
			   if(resultSetCustomer.next()) {
		    	   	   newCustomer = objectFactory.createCustomer();
			    	   newCustomer.setId(resultSetCustomer.getInt("id"));
			    	   newCustomer.setFirstName(resultSetCustomer.getString("first_name"));
			    	   newCustomer.setLastName(resultSetCustomer.getString("last_name"));
			    	   newCustomer.setMobileNumber(resultSetCustomer.getString("mobile"));
			 	       
			 	   Country country =  Country.fromValue(resultSetCustomer.getString("country"));
			 	   newCustomer.setCountry(country);
			 	       
			 	   newCustomer.setEmail(resultSetCustomer.getString("email"));
			 	   
		       } // end if
		       
		       stmt.close();
			}catch (Exception e) {
				e.printStackTrace();
				
				return null;
			} // end try - catch
			
			return newCustomer;
	       
	       
	} // end method


	@Override
	public List<Customer> readCustomers() throws RemoteException {
		List<Customer> customers = new LinkedList<Customer>();
        try {
        	
        		stmt = connection.createStatement();
        	   ResultSet resultSetCustomer = connection.createStatement().executeQuery("SELECT * FROM customers");
               
           Customer customer = null;     
           while(resultSetCustomer.next()) {
        	       customer = objectFactory.createCustomer();
		    	   customer.setId(resultSetCustomer.getInt("id"));
		    	   customer.setFirstName(resultSetCustomer.getString("first_name"));
		    	   customer.setLastName(resultSetCustomer.getString("last_name"));
		    	   customer.setMobileNumber(resultSetCustomer.getString("mobile"));
		 	       
		 	   Country country = Country.fromValue(resultSetCustomer.getString("country"));
		 	   customer.setCountry(country);
		 	       
		 	   customer.setEmail(resultSetCustomer.getString("email"));
		 	   
		 	  customers.add(customer);
	 	   
           } // end while
           
           resultSetCustomer.close();
           stmt.close();
                
           } catch (Exception e) {
               e.printStackTrace();
               
               return null;
            		   
           } // end try - catch
        
           return customers;
           
	} // end method
	

	@Override
	public Customer updateCustomer(Customer customer) throws RemoteException {
		Customer updatedCustomer = null;
		try {
			stmt = connection.createStatement();
			String sql = "UPDATE customers " +
					  "SET first_name = '" + customer.getFirstName() + "', " +
					  "last_name = '" + customer.getLastName() + "', " +
					  "email = '" + customer.getEmail() + "', " +
					  "mobile = '" + customer.getMobileNumber() + "', " +
					  "country = '" + customer.getCountry().value() + "' " +
					  "WHERE id = " + customer.getId() + "";
			
			stmt.execute(sql);
			
//			deleteCustomer(customer);
			updatedCustomer = readCustomer(customer.getId());
			
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return updatedCustomer;
		
	} // end method


	@Override
	public void deleteCustomer(Customer customer) throws RemoteException {
		try {
			stmt = connection.createStatement();
			stmt.execute("DELETE FROM customers WHERE id='" + customer.getId() + "'");
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	} // end method
	

	public void closeConnection(){
        try {
              if (connection != null) {
                  connection.close();
              }
            } catch (Exception e) { 
                //do nothing
            }
        
    } // end method
	
} // end class
