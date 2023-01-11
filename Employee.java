//Justin Chun 300131838
//Ziyad Gaffar 300148116
//Siana Kong 300110500

package code;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import java.util.ArrayList;

public class Employee {

	public static void main(String[] args) throws Exception{
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
			int bookingID = 0;
			int roomID = 0;
			int numOfOcc = 0;
			int employee_id = 0;
			int capacity = 0;
			Date indate = new Date(1,1,1);
			Date outdate = new Date(1,1,1);
			boolean book = false;
			boolean room_existx = true;
			String custName = "";
			ArrayList<Integer> rooms = new ArrayList<Integer>();
			
			boolean loop = true;
			//employee authentication
			System.out.println("Welcome to DB Hotel lookup app for employees.");
			while(loop) {
				System.out.print("Enter your SIN number to access information: ");
				long sin = scan.nextLong();
	            ps = db.prepareStatement("SELECT employee_id FROM \"Project\".employee WHERE sin = ?");
	            //***check if employee exists on system***
	            ps.setLong(1,sin);
	            rs = ps.executeQuery();
	            while(rs.next()) {
	            	employee_id = rs.getInt(1);
	            }
				if(employee_id==0) {
					System.out.println("Employee with the SIN " + sin + " does not exist\n");
				}else {
					loop=false;
				}
			}
			loop=true;
            
            ps = db.prepareStatement("SELECT e.name, c.hotel_name, h.city "
            		+ "FROM \"Project\".employee e, \"Project\".chain c, \"Project\".hotel h "
            		+ "WHERE employee_id = ? "
            		+ "AND e.hotel_id = h.hotel_id "
            		+ "AND h.chain_id = c.chain_id ");
            ps.setInt(1,employee_id);
            rs = ps.executeQuery();
            String employeename = "";
            String hotelname = "";
            String hotelcity = "";
            while(rs.next()) {
                employeename = rs.getString(1);
                hotelname = rs.getString(2);
                hotelcity = rs.getString(3);
            }
            System.out.println("Welcome "+ employeename + ", your employee ID is: "+ employee_id);
			
            //view rooms by availability
            System.out.println();
            while(loop&&room_existx) {
	            System.out.print("To view available rooms, enter 1\nTo view booked rooms, enter 2: ");
	            int viewOption = scan.nextInt();
	            switch(viewOption) {
	            	case 1:
	            		//show available rooms
	            		System.out.println();
	            		System.out.println("You are viewing all the available rooms from " + hotelname + ", " + hotelcity);
	            		ps = db.prepareStatement("SELECT s.room_id, r.room_number, r.price, r.view, r.extendable, r.capacity, r.amenities "
	            				+ "FROM \"Project\".employee E, \"Project\".Hotel H, \"Project\".Room R, \"Project\".Chain C, \"Project\".System S "
	            				+ "WHERE e.employee_id = ? "
	            				+ "AND H.hotel_id = E.hotel_id AND R.hotel_id = H.hotel_id AND S.room_id = R.room_id "
	            				+ "AND C.chain_id = H.chain_id AND S.room_status = 'Avail' "
	            				+ "ORDER BY r.room_number");
	            		ps.setInt(1,employee_id);
	                    rs = ps.executeQuery();
						System.out.println();
						System.out.println("Room ID     Room Number Price       View        Extendable  Capacity    Amenities");
						System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
						while(rs.next()) {
							room_existx = false;
							for(int i = 1; i <= 7; i++) {
								System.out.printf("%-12s", rs.getString(i));
							}
							rooms.add(rs.getInt(1));
							System.out.println();
						}
						if(room_existx==true) {
							System.out.println("There are no available rooms");
							System.out.println();
						}
						else {
							//retrieve required information for renting
				            boolean aRoom_x = true;
				            while(aRoom_x) {
				            	System.out.println();
								System.out.print("Enter the Room ID of the room you wish to transform to rented: ");
					            roomID = scan.nextInt();
					            if(rooms.contains(roomID)) {
					            	aRoom_x = false;
					            }else {
					            	System.out.println("The entered Room ID is not one of the available rooms");
					            }
				            }
							System.out.print("Enter the check in date(YYYY-MM-DD): ");
							String checkinDate = scan.next();
							String[] tempin = checkinDate.split("-");
							indate = new Date(Integer.parseInt(tempin[0])-1900,Integer.parseInt(tempin[1])-1,Integer.parseInt(tempin[2]));
							
							System.out.print("Enter the check out date(YYYY-MM-DD): ");
							String checkoutDate = scan.next();
							String[] tempout = checkoutDate.split("-");
							outdate = new Date(Integer.parseInt(tempout[0])-1900,Integer.parseInt(tempout[1])-1,Integer.parseInt(tempout[2]));
							
							boolean capNum = true;
							while(capNum){
								System.out.print("Enter the number of occupants: ");
								numOfOcc = scan.nextInt();
								ps = db.prepareStatement("SELECT capacity FROM \"Project\".system WHERE room_id=?");
								ps.setInt(1,roomID);
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
							loop=false;
						}
	            		break;
	            	case 2:
	            		//show booked rooms
	            		book = true;
	            		System.out.println();
	            		System.out.println("You are viewing all the booked rooms from " + hotelname + ", " + hotelcity);
	            		ps = db.prepareStatement("SELECT s.room_id, r.room_number, r.price, r.view, r.extendable, r.capacity, r.amenities "
	            				+ "FROM \"Project\".employee E, \"Project\".Hotel H, \"Project\".Room R, \"Project\".Chain C, \"Project\".System S "
	            				+ "WHERE e.employee_id = ? "
	            				+ "AND H.hotel_id = E.hotel_id AND R.hotel_id = H.hotel_id AND S.room_id = R.room_id "
	            				+ "AND C.chain_id = H.chain_id AND S.room_status = 'Book' "
	            				+ "ORDER BY R.room_number");
	            		ps.setInt(1,employee_id);
	                    rs = ps.executeQuery();
						System.out.println();
						System.out.println("Room ID     Room Number Price       View        Extendable  Capacity    Amenities");
						System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
						while(rs.next()) {
							room_existx = false;
							for(int i = 1; i <= 7; i++) {
								System.out.printf("%-12s", rs.getString(i));
							}
							rooms.add(rs.getInt(1));
							System.out.println();
						}
						if(room_existx==true) {
							System.out.println("There are no booked rooms");
							System.out.println();
						}
						else {
							boolean bRoom_x = true;
							while(bRoom_x) {
								System.out.println();
								System.out.print("Enter the Room ID of the room you wish to transform to rented: ");
					            roomID = scan.nextInt();
					            if(rooms.contains(roomID)) {
					            	bRoom_x = false;
					            }else {
					            	System.out.println("The entered Room ID is not one of the booked rooms");
					            }
							}
							loop=false;
						}
	            		break;
	            	default:
	            		System.out.println("The entered value is neither 1 nor 2\n");
	            		break;
	            }
            }
            
            //get customer information
            boolean cust_exist = true;
            while(cust_exist) {
	            if(book==false) {
	            	//Entering customer id
	                System.out.print("Enter the Booking ID of the customer: ");
	                bookingID = scan.nextInt();
	                ps = db.prepareStatement("SELECT name FROM \"Project\".customer WHERE booking_id=?");
	                ps.setInt(1,bookingID);
	                rs = ps.executeQuery();
	                while(rs.next()) {
	                	custName = rs.getString(1);
	                }
	                if(custName.equals("")) {
		            	System.out.println("Customer with Booking ID " + bookingID + " does not exist");
		            	System.out.println();
		            }else {
		            	cust_exist = false;
		            }
	            }else {
	            	//retrieving customer id
	                ps = db.prepareStatement("SELECT c.name "
	                		+ "FROM \"Project\".customer c, \"Project\".system s "
	                		+ "WHERE s.room_id=? "
	                		+ "AND c.booking_id = s.booking_id ");
	                ps.setInt(1,roomID);
	                rs = ps.executeQuery();
	                while(rs.next()) {
	                	custName = rs.getString(1);
	                }
	                cust_exist = false;
	            } 
            }
            
            //check if customer has paid
            scan.nextLine();
            
            boolean yorn_x = true;
            while(yorn_x) {
            	System.out.print("Has the customer '" + custName + "' paid for the room successfully(yes/no): ");
                String yorn = scan.nextLine();
                yorn = yorn.toLowerCase();
	            switch(yorn) {
	            	case "yes":
	            		ps = db.prepareStatement("UPDATE \"Project\".system SET paid=TRUE WHERE room_id=?");
	            		ps.setInt(1,roomID);
	            		ps.executeUpdate();
	            		if (book==false) {
	            			ps = db.prepareStatement("UPDATE \"Project\".system SET booking_id = ?, start_date = ?, "
	            					+ "end_date = ?, number_of_occupants = ? WHERE room_id = ?");
	            			ps.setInt(1,bookingID);
	            			ps.setDate(2,indate);
	            			ps.setDate(3,outdate);
	            			ps.setInt(4,numOfOcc);
	            			ps.setInt(5,roomID);
	            			ps.executeUpdate();
	            		}
	            		ps = db.prepareStatement("UPDATE \"Project\".system SET employee_id = ?, "
	            				+ "room_status = 'Rent' WHERE room_id=?");
	            		ps.setInt(1,employee_id);
			            ps.setInt(2,roomID);
			            ps.executeUpdate();
	            		ps = db.prepareStatement("SELECT room_number FROM \"Project\".room WHERE room_id=?");
	            		ps.setInt(1,roomID);
	            		rs = ps.executeQuery();
	            		int roomNumb = 0;
	            		while(rs.next()) {
	                    	roomNumb = rs.getInt(1);
	                    }
	            		System.out.println("Successfully booked Room " + roomNumb + " for '" + custName +"'");
	            		yorn_x = false;
	            		break;
	            	case "no":
	            		System.out.println("Customer must pay for the room to rent it");
	            		yorn_x = false;
	            		break;
	            	default:
	            		System.out.println("The entered value is neither yes nor no");
	            		System.out.println();
	            		break;
	            }
            }
            
            System.out.println("Thank you for using our DB Hotel lookup app");
            
			rs.close();
			st.close();
			db.close();
		}
		catch (SQLException ex){
			Logger lgr = Logger.getLogger(Employee.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
}