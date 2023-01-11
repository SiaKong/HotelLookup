//Justin Chun 300131838
//Ziyad Gaffar 300148116
//Siana Kong 300110500

package code;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Scanner;

public class Customer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String user = "";
		String password = "";
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter your pgAdmin ID: ");
		user = scan.nextLine();
		System.out.print("Enter your pgAdmin password: ");
		password = scan.nextLine();
		System.out.println();
		
		try {
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e){
			System.err.println("Where is your PostgreSQL JDBC Driver?");
			e.printStackTrace();
		}
		
		try(Connection db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/group_b02_g43",user,password)){
			Statement st = db.createStatement();
			PreparedStatement ps;
			ResultSet rs = st.executeQuery("SELECT hotel_name FROM \"Project\".chain");
			
			System.out.println("Welcome to DB Hotel app.");
			//user authentication
			System.out.print("Are you already Registered(yes/no): ");
            String answer = scan.nextLine();
            answer= answer.toLowerCase();
            
            //Initializing Variables
            String hotelName="";
            int booking_id = 0;
            String customername = "";
            String customercountry = "";
            String customercity = "";
            String customerstate = "";
            int customerSIN;
            int capacity = 0;
            Long customerphone;
            String customerPostalcode = "";
            int customerstreetnum;
            int numOfOcc = 0;
            String customerstreetname = "";
            java.sql.Date dateofregistration;
            boolean bool = false;
            boolean bool2 = false;
            boolean bool3 = false;
            boolean bool4 = false;
            boolean bool5 = false;
            boolean capNum = true;
            ArrayList<String> cities = new ArrayList<String>();
            
            boolean run = true;
            
	        while(run) {    
	            switch(answer) {
	            	//The customer is registered in the database
	                case "yes":
	                    System.out.print("First please enter your phone number:  ");
	                    customerphone = scan.nextLong();
	                    ps = db.prepareStatement("SELECT booking_id FROM \"Project\".customer WHERE phone_number = ?");
	                    ps.setLong(1,customerphone);
	                    rs = ps.executeQuery();
	                    while(rs.next()) {
	                    	booking_id = rs.getInt(1);
	                    }
	                    //To catch if the phone number doesn't exist in database
	                    while(booking_id == 0 || bool) {
	                    	System.out.print("This phone number does not exist. Please enter a valid phone number: ");
	                    	customerphone = scan.nextLong();
	                        ps = db.prepareStatement("SELECT booking_id FROM \"Project\".customer WHERE phone_number = ?");
	                        ps.setLong(1,customerphone);
	                        rs = ps.executeQuery();
	                        while(rs.next()) {
	                        	booking_id = rs.getInt(1);
	                        }
	                        if(booking_id != 0) {
	                        	bool = true;
	                        	break;
	                        }
	                    }
	                    
	                    rs = st.executeQuery("SELECT name FROM \"Project\".customer WHERE booking_id = " + booking_id);
	                    while(rs.next()) {
	                        customername = rs.getString(1);
	                    }
	                    System.out.println("Welcome "+ customername + " here is your booking_id: "+ booking_id);
	                    run = false;
	                    break;
	                
	                //The customer isn't registered in the database. Customer Registration    
	                case "no":
	                	 System.out.print("You Need to register. First, Enter your Name: ");
	                	 customername = scan.nextLine();
	                	 
	                	 System.out.print("Enter your Phone number: ");
	                	 customerphone = scan.nextLong();
	                	 scan.nextLine();
	                	 while(!(1000000000 < customerphone && customerphone < 9999999999L)&&bool2) {
	                		 System.out.println("This phone number is invalid. Enter a vaild phone number: ");
	                		 customerphone = scan.nextLong();
	                    	 scan.nextLine();
	                    	 if(1000000000 < customerphone && customerphone < 9999999999L) {
	                    		 bool2 = true;
	                    		 break;
	                    	 }
	                	 }
	                	 
	                	 System.out.print("Enter your Country of Location: ");
	                	 customercountry = scan.nextLine();
	                	 
	                	 System.out.print("Enter your State/Province of Location: ");
	                	 customerstate = scan.nextLine();
	                	 
	                	 System.out.print("Enter your SIN Number: ");
	                	 customerSIN = scan.nextInt();
	                	 while(!(100000000 < customerSIN && customerSIN < 999999999) && bool3) {
	                		 System.out.println("This SIN number is invalid. Enter a vaild SIN number: ");
	                		 customerSIN = scan.nextInt();
	                    	 scan.nextLine();
	                    	 if(100000000 < customerSIN && customerSIN < 999999999) {
	                    		 bool3 = true;
	                    		 break;
	                    	 }
	                	 }
	                	 scan.nextLine();
	                	 
	                	 System.out.print("Enter your Postal code: ");
	                	 customerPostalcode = scan.next();
	                	 while(customerPostalcode.length() != 6 && bool4) {
	                		 System.out.println("This Postal Code is invalid. Enter a vaild Postal Code: ");
	                		 customerSIN = scan.nextInt();
	                    	 scan.nextLine();
	                    	 if(customerPostalcode.length() == 6) {
	                    		 bool4 = true;
	                    		 break;
	                    	 }
	                	 }
	                	 scan.nextLine();
	                	 
	                	 System.out.print("Enter your City: ");
	                	 customercity = scan.nextLine();
	                	 
	                	 System.out.print("Enter your Street Number: ");
	                	 customerstreetnum = scan.nextInt();
	                	 scan.nextLine();
	                	 
	                	 System.out.print("Enter your Street Name: ");
	                	 customerstreetname = scan.nextLine();
	                	 
	                	 long millis=System.currentTimeMillis();  
	                	 dateofregistration = new java.sql.Date(millis);
	                	  
	                	 rs= st.executeQuery("SELECT MAX(booking_id) FROM \"Project\".Customer");
	                	 while(rs.next()) {
	                		 booking_id = rs.getInt(1) + 1;
	                	 }
	                	 ps = db.prepareStatement("INSERT INTO \"Project\".customer (Booking_ID, Date_of_registration, Country,"
	                	 		+ "City, State, Postal_code, Street_number, Street_name, "
	                	 		+ "name, SIN, Phone_number) VALUES "
	                	 		+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	                	 ps.setInt(1, booking_id);
	                	 ps.setDate(2, dateofregistration);
	                	 ps.setString(3, customercountry);
	                	 ps.setString(4, customercity);
	                	 ps.setString(5, customerstate);
	                	 ps.setString(6, customerPostalcode);
	                	 ps.setInt(7, customerstreetnum);
	                	 ps.setString(8, customerstreetname);
	                	 ps.setString(9, customername);
	                	 ps.setLong(10, customerSIN);
	                	 ps.setLong(11, customerphone);
	                	 ps.executeUpdate();
	                	 
	                	 System.out.println("You have now been registered. Your booking_id is: " + booking_id + ". Thank you!");
	                	 run = false;
	                	 break;
	           
	                default:
	                	System.out.print("Please enter either yes or no:  ");
	            		answer = scan.nextLine();
	            		answer= answer.toLowerCase();
	            		break;
	            }
	        }
            
			//allow user to search the hotel by hotel name or location
			System.out.print("To select a hotel by name, enter 1. To select a hotel by location, enter 2: ");
			int searchOption = scan.nextInt();
			
			boolean run2 = true;
			while(run2) {
				switch(searchOption) {
					case 1:
						//select room by the hotel brand
						rs = st.executeQuery("SELECT hotel_name, Country FROM \"Project\".chain");
						System.out.println();
						System.out.println("Hotel Name          Country");
						System.out.println("------------------------------------");
						while(rs.next()) {
							for(int i = 1; i <= 2; i++) {
								System.out.printf("%-20s", rs.getString(i));
							}
							System.out.println();
						}
						System.out.println("------------------------------------");
						
						System.out.print("Enter the name of the hotel you want to stay in: ");
						hotelName = scan.next();
						hotelName = hotelName.substring(0,1).toUpperCase() + hotelName.substring(1).toLowerCase();
						ps = db.prepareStatement("SELECT hotel_name FROM \"Project\".chain");
						rs = ps.executeQuery();
						
						ArrayList<String> hotelnameVerify = new ArrayList<String>();
	                    while(rs.next()) {
	                        hotelnameVerify.add(rs.getString(1));
	                    }
	                    
	                    while(hotelnameVerify.contains(hotelName) == false) {
	                        System.out.print("Wrong Entry. Please enter the hotel on the list shown above: ");
	                        hotelName = scan.next();
	                        hotelName = hotelName.substring(0,1).toUpperCase() + hotelName.substring(1).toLowerCase();
	                        if(hotelnameVerify.contains(hotelName) == true) {
	                            break;
	                        }
	                    }
						
						ps = db.prepareStatement("SELECT DISTINCT C.hotel_name, H.city, R.room_number, R.price, R.view, R.extendable, "
																	+ "R.capacity, R.amenities "
																	+ "FROM \"Project\".chain C, \"Project\".hotel H, \"Project\".system S, \"Project\".room R "
																	+ "WHERE S.hotel_id = H.hotel_id AND S.hotel_id = R.hotel_id AND C.hotel_name = ?"
																	+ "AND C.chain_id = H.chain_id AND S.room_id = R.room_id AND S.room_status = 'Avail' "
																	+ "ORDER BY H.city");
						
						ps.setString(1,hotelName);
						rs = ps.executeQuery();
						System.out.println();
						System.out.println("Hotel             City              Room Number       Price             View              Extendable        Capacity          Amenities");
						System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						
						ArrayList<String> cityVerify = new ArrayList<String>();
						ArrayList<Integer> roomVerify = new ArrayList<Integer>();
						while(rs.next()) {
							cityVerify.add(rs.getString(2));
							roomVerify.add(rs.getInt(3));
							for(int i = 1; i <= 8; i++) {
								System.out.printf("%-18s", rs.getString(i));
							}
							System.out.println();
						}
						System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						
						System.out.print("Enter the name of the city you want to stay at: ");
						String bookingCity = scan.next();
						bookingCity = bookingCity.substring(0,1).toUpperCase() + bookingCity.substring(1).toLowerCase();

	                    while(cityVerify.contains(bookingCity) == false) {
	                        System.out.print("Wrong Entry. Please enter the city on the list shown above: ");
	                        bookingCity = scan.next();
	                        bookingCity = bookingCity.substring(0,1).toUpperCase() + bookingCity.substring(1).toLowerCase();
	                        if(cityVerify.contains(bookingCity) == true) {
	                            break;
	                        }
	                    }
						
						System.out.print("Enter the room number: ");
						int bookingRoom = scan.nextInt();
						
						while(roomVerify.contains(bookingRoom) == false) {
	                        System.out.print("Wrong Entry. Please enter the room on the list shown above: ");
	                        bookingRoom = scan.nextInt();
	                        if(roomVerify.contains(bookingRoom) == true) {
	                            break;
	                        }
	                    }
						ArrayList<Integer> roomverify1 = new ArrayList<Integer>();
                        ps = db.prepareStatement("SELECT R.room_number FROM \"Project\".room R, \"Project\".chain C, "
                                + "\"Project\".Hotel H, \"Project\".System S WHERE R.room_id = S.room_id AND "
                                + "H.Chain_id = C.Chain_id AND C.Hotel_name = ? AND S.hotel_id=H.hotel_id AND S.hotel_id=R.hotel_id AND H.city = ? "
                                + "AND S.room_status='Avail';");
                        ps.setString(1, hotelName);
                        ps.setString(2, bookingCity);
                        
                        rs = ps.executeQuery();
                        while(rs.next()) {
                            roomverify1.add(rs.getInt(1));
                        }

                        while((roomverify1.contains(bookingRoom) == false)) {
                            System.out.print("This room number doesn't exist. Please enter an existant room number: ");
                            bookingRoom = scan.nextInt();
                            if(roomverify1.contains(bookingRoom) == true) {
                                break;
                            }
                            scan.nextLine();
                        }
						
						System.out.print("Enter the check in date(YYYY-MM-DD): ");
						String startDate = scan.next();
						String[] tempStart = startDate.split("-");
						Date stDate = new Date(Integer.parseInt(tempStart[0])-1900,Integer.parseInt(tempStart[1])-1,Integer.parseInt(tempStart[2]));
						
						System.out.print("Enter the check out date(YYYY-MM-DD): ");
						String endDate = scan.next();
						String[] tempEnd = endDate.split("-");
						Date enDate = new Date(Integer.parseInt(tempEnd[0])-1900,Integer.parseInt(tempEnd[1])-1,Integer.parseInt(tempEnd[2]));
						
						capNum = true;
						while(capNum){
							System.out.print("Enter the number of occupants: ");
							numOfOcc = scan.nextInt();
							ps = db.prepareStatement("SELECT R.capacity "
									+ "FROM \"Project\".room R, \"Project\".hotel H, \"Project\".chain C "
									+ "WHERE R.room_number=? "
									+ "AND H.city=? "
									+ "AND C.hotel_name=? "
									+ "AND C.chain_id = H.chain_id "
									+ "AND H.hotel_id = R.hotel_id");
							ps.setInt(1,bookingRoom);
							ps.setString(2,bookingCity);
							ps.setString(3,hotelName);
							rs=ps.executeQuery();
							while(rs.next()) {
								capacity = rs.getInt(1);
							}
							if(numOfOcc>=1 && numOfOcc<=capacity) {
								capNum = false;
							}else if(numOfOcc>capacity){
								System.out.println("The number of occupants is greater than the room capacity");
								System.out.println();
							}else if(numOfOcc<1) {
								System.out.println("The number of occupants must be greater than 0");
								System.out.println();
							}
						}
						System.out.println("You are booking room " + bookingRoom + " at " + hotelName + " hotel located at " + bookingCity
											+ " from " + startDate + " to " + endDate);
						ps = db.prepareStatement("UPDATE \"Project\".System "
								+ "SET room_status = 'Book', start_date = ?, end_date = ?, booking_id = ?, number_of_occupants = ? "
								+ "FROM "
								+ "(SELECT R.room_id AS id "
								+ "FROM \"Project\".Room R, \"Project\".Hotel H, \"Project\".Chain C "
								+ "WHERE H.city = ? AND C.hotel_name = ? AND H.hotel_id = R.hotel_id "
								+ "AND H.chain_id = C.chain_id AND R.room_number = ?) AS B "
								+ "WHERE room_id = B.id");
						ps.setDate(1, stDate);
						ps.setDate(2, enDate);
						ps.setInt(3, booking_id);
						ps.setInt(4, numOfOcc);
						ps.setString(5, bookingCity);
						ps.setString(6, hotelName);
						ps.setInt(7, bookingRoom);
						ps.executeUpdate();
						run2 = false;
						break;
					case 2:
						//select room by location
						rs = st.executeQuery("SELECT DISTINCT country, city FROM \"Project\".hotel");
						System.out.println();
						System.out.println("Country             City");
						System.out.println("--------------------------------------");
						while(rs.next()) {
							for(int i = 1; i <= 2; i++) {
								System.out.printf("%-20s", rs.getString(i));
							}
							cities.add(rs.getString(2));
							System.out.println();
						}
						System.out.println("--------------------------------------");
						
						//show all available rooms in selected city
						boolean city_x = true;
						String city = "";
						while(city_x) {
							System.out.print("Enter the name of the city you want to stay in: ");
							city = scan.next();
							city = city.substring(0,1).toUpperCase() + city.substring(1).toLowerCase();
							if(cities.contains(city)) {
								city_x = false;
							}else {
								System.out.println("Please select a city from the provided list\n");
							}
						}
						
						ps = db.prepareStatement("SELECT DISTINCT C.hotel_name, R.room_number, R.price, R.view, R.extendable,"
								+ "R.capacity, R.Amenities "
								+ "FROM \"Project\".hotel H, \"Project\".chain C, \"Project\".System S, \"Project\".Room R "
								+ "WHERE S.hotel_id=H.hotel_id AND S.hotel_id=R.hotel_id "
								+ "AND H.chain_id=C.chain_id AND H.city=? AND S.room_status='Avail' AND S.room_id = R.room_id "
								+ "ORDER BY c.hotel_name");
						ps.setString(1,city);
						rs = ps.executeQuery();
						System.out.println();
						System.out.println("Hotel       Room Number Price       View        Extendable  Capacity    Amenities");
						System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
						ArrayList<String> hotels = new ArrayList<String>();
						while(rs.next()) {
							for(int i = 1; i <= 7; i++) {
								System.out.printf("%-12s", rs.getString(i));
							}
							hotels.add(rs.getString(1));
							System.out.println();
						}
						
						//allow customer to book room
						scan.nextLine();
						String bookingHotel = "";
						System.out.println();
						boolean hotel_x = true;
						while(hotel_x) {
							System.out.print("Enter the name of the hotel you want to book the room from: ");
							bookingHotel = scan.nextLine();
							bookingHotel = bookingHotel.substring(0,1).toUpperCase() + bookingHotel.substring(1).toLowerCase();
							if(hotels.contains(bookingHotel)) {
								hotel_x = false;
							}else {
								System.out.println("Please select a hotel from the provided list\n");
							}
						}
						

						
						System.out.print("Enter the room number: ");
						bookingRoom = scan.nextInt();
						
						//Verification that the room is indeed available
						ArrayList<Integer> roomverify = new ArrayList<Integer>();
						ps = db.prepareStatement("SELECT R.room_number FROM \"Project\".room R, \"Project\".chain C, "
								+ "\"Project\".Hotel H, \"Project\".System S WHERE R.room_id = S.room_id AND "
								+ "H.Chain_id = C.Chain_id AND C.Hotel_name = ? AND S.hotel_id=H.hotel_id AND S.hotel_id=R.hotel_id AND H.city = ? "
								+ "AND S.room_status='Avail';");
						ps.setString(1, bookingHotel);
						ps.setString(2, city);
						rs = ps.executeQuery();
						while(rs.next()) {
							roomverify.add(rs.getInt(1));
						}

						while((roomverify.contains(bookingRoom) == false)) {
							System.out.print("This room number doesn't exist. Please enter an existant room number: ");
							bookingRoom = scan.nextInt();
							if(roomverify.contains(bookingRoom) == true) {
								break;
							}
							scan.nextLine();
						}
						
						System.out.print("Enter the check in date(YYYY-MM-DD): ");
						String checkinDate = scan.next();
						String[] tempin = checkinDate.split("-");
						Date indate = new Date(Integer.parseInt(tempin[0])-1900,Integer.parseInt(tempin[1])-1,Integer.parseInt(tempin[2]));
						
						System.out.print("Enter the check out date(YYYY-MM-DD): ");
						String checkoutDate = scan.next();
						String[] tempout = checkoutDate.split("-");
						Date outdate = new Date(Integer.parseInt(tempout[0])-1900,Integer.parseInt(tempout[1])-1,Integer.parseInt(tempout[2]));
						
						
						capNum = true;
						while(capNum){
							System.out.print("Enter the number of occupants: ");
							numOfOcc = scan.nextInt();
							ps = db.prepareStatement("SELECT R.capacity "
									+ "FROM \"Project\".room R, \"Project\".hotel H, \"Project\".chain C "
									+ "WHERE R.room_number=? "
									+ "AND H.city=? "
									+ "AND C.hotel_name=? "
									+ "AND C.chain_id = H.chain_id "
									+ "AND H.hotel_id = R.hotel_id");
							ps.setInt(1,bookingRoom);
							ps.setString(2,city);
							ps.setString(3,bookingHotel);
							rs=ps.executeQuery();
							while(rs.next()) {
								capacity = rs.getInt(1);
							}
							if(numOfOcc>=1 && numOfOcc<=capacity) {
								capNum = false;
							}else if(numOfOcc>capacity){
								System.out.println("The number of occupants is greater than the room capacity");
								System.out.println();
							}else if(numOfOcc<1) {
								System.out.println("The number of occupants must be greater than 0");
								System.out.println();
							}
						}
						System.out.println("You are booking room " + bookingRoom + " at " + bookingHotel + " hotel "
								+ "from " + checkinDate + " to " + checkoutDate);
						
						ps = db.prepareStatement("UPDATE \"Project\".System "
								+ "SET room_status = 'Book', start_date = ?, end_date = ?, booking_id = ?, number_of_occupants = ? "
								+ "FROM "
								+ "(SELECT R.room_id AS id "
								+ "FROM \"Project\".Room R, \"Project\".Hotel H, \"Project\".Chain C "
								+ "WHERE H.city = ? AND C.hotel_name = ? AND H.hotel_id = R.hotel_id "
								+ "AND H.chain_id = C.chain_id AND R.room_number = ?) AS B "
								+ "WHERE room_id = B.id");
						
						ps.setDate(1, indate);
						ps.setDate(2, outdate);
						ps.setInt(3, booking_id);
						ps.setInt(4, numOfOcc);
						ps.setString(5, city);
						ps.setString(6, bookingHotel);
						ps.setInt(7, bookingRoom);
						ps.executeUpdate();
						
						run2 = false;
						break;
					default:
						System.out.print("The entered value is neither 1 nor 2. Please enter 1 or 2: ");
						searchOption = scan.nextInt();
						break;
				}
			}
		
			rs.close();
			st.close();
			db.close();
		}
		catch (SQLException ex){
			Logger lgr = Logger.getLogger(Customer.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

}