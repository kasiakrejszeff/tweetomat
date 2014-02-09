//import java.sql.*;

public class Main {
	 public static void main( String args[] )
	  {
		 SQLiteJDBC.createDatabase();
		 TDemo.login();	 
		 TDemo.getFriends();
		 TDemo.updateStatus("Jest 5:30 rano.");
		 TDemo.chooseTweets();
	  }
	 
}
