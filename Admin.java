//Justin Chun 300131838
//Ziyad Gaffar 300148116
//Siana Kong 300110500

package code;
import java.sql.*;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Admin {
	public static void main(String args[]) {
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
		
		try(Connection db = DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/group_b02_g43",user,password)) {
			Statement st = db.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM \"Project\".chain");
			
            System.out.println("Welcome to DB Hotel app for Admin.");
			boolean unchecked = true;
            while(unchecked) {
				try {
					System.out.println("Enter your query here: ");
		            String query = scan.nextLine();
		            int result = st.executeUpdate(query);
		            
		            System.out.println("Query Successfully submitted!");
		            
		            unchecked = false;
				} catch (Exception e) {
					System.out.println("Invalid Query. Please try again.");
				}
			}

            rs.close();
            st.close();
        }
        catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Admin.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}