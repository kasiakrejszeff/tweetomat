//import java.sql.*;

public class Main {
	 public static void main( String args[] )
	  {
		 SQLiteJDBC.createDatabase();
		 TDemo.login();	 
		 TDemo.getFriends();
		 TDemo.updateStatus("Wiedz, że coś się dzieje.");
		 TDemo.chooseTweets();
	  }
	 
}
