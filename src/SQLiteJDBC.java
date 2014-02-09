import java.sql.*;
import java.util.ArrayList;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.User;

public class SQLiteJDBC
{
	private static Connection c = null;
	
	public static void openConnection() throws ClassNotFoundException, SQLException{
		if (c == null){
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:tweetomat.db");
			c.setAutoCommit(false);
		}
	}
	
	public static void createDatabase()
	{
		String createDB[] = new String[7];
		createDB[0] =
				"CREATE TABLE Users " +
				"( id LONG PRIMARY KEY NOT NULL , " +
				"username TEXT NOT NULL )";
		createDB[1] =  
			"CREATE TABLE Tweet " +
			"( tweet_id LONG PRIMARY KEY NOT NULL, " +
			"author TEXT, " + 
			"content TEXT  , " +
			"fav_count LONG , " +
			"retweet_count LONG )";  
		createDB[2] =
			"CREATE TABLE Comment " +
			"( comm_id LONG PRIMARY KEY NOT NULL , " +
			"content TEXT  NOT NULL , " +
			"create_time DATE  NOT NULL , " +
			"tweet_id LONG )";
		createDB[3] = 
			"CREATE TABLE Tag " +
			"( tag TEXT PRIMARY KEY NOT NULL )";
		createDB[4] =
			"CREATE TABLE tag_comment " +
			"( tag TEXT REFERENCES Tag(tag)" + 
				" DEFERRABLE INITIALLY DEFERRED, " +
			"comm_id LONG REFERENCES Comment(comm_id) " +
				"DEFERRABLE INITIALLY DEFERRED)";
		createDB[5] = 
			"CREATE TABLE tweer_tag " +
			"(tweet_id LONG REFERENCES Tweet(tweet_id)" + 
				" DEFERRABLE INITIALLY DEFERRED, " +
			"tag TEXT REFERENCES Tag(tag)" + 
				" DEFERRABLE INITIALLY DEFERRED)";
		createDB[6] = 
			"CREATE TABLE rejected " +
			"(tweet_id LONG REFERENCES Tweet(tweet_id)" + 
				" DEFERRABLE INITIALLY DEFERRED)";  
	  
		
		Statement stmt = null;
		try {
			openConnection();
			stmt = c.createStatement();
			for(int i = 0; i < createDB.length; ++i){
				try{
					stmt.executeUpdate(createDB[i]);
				} catch (SQLException sqle) {
					System.out.println("table " + i + " already exists");
				} 
			}
			c.commit();
			stmt.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Tables created successfully");
	}
	
	public static void addFriends(ArrayList<User> users){
	    Statement stmt = null;
	    try {
	    	openConnection();
	        stmt = c.createStatement();
	        String sql ="";
	        for (User user : users){
	        	sql = "INSERT OR IGNORE INTO Users (id,username) VALUES (" + user.getId() + 
	        			", '" + user.getScreenName() +"' );";
	        	if (stmt.execute(sql)) System.out.print("Inserted into Users values (" +  
	        			user.getId() + ", '" + user.getScreenName()+ ").");
	        }
	        stmt.close();
	        c.commit();
	    } catch ( Exception e ) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    System.out.println("Records created successfully");		
	}
	
	public static ArrayList<String> getFriends() {
		ArrayList <String> result = new ArrayList<String>();

	    Statement stmt = null;
	    try {
	      openConnection();
	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM Users;" );
	      while ( rs.next() ) {
	         String name = rs.getString("username");
	         result.add(name);
	      }
	      rs.close();
	      stmt.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    System.out.println("getFriends done successfully");
		return result;
	}
	
	public static void addTweets(ResponseList<Status> tweets) {
		Statement stmt = null;
	    try {
	    	openConnection();
	        stmt = c.createStatement();
	          for (Status tweet : tweets){
	        	  String sql = "INSERT INTO Tweet (tweet_id, author, " +
	    	        	"fav_count, retweet_count) VALUES (" +
	    	        	tweet.getId() + ", '" + tweet.getUser().getScreenName() +
	    	        	 "', "+ tweet.getFavoriteCount() 
	        			+ ", " + tweet.getRetweetCount() + ");";
	        	  try {
	        		  stmt.execute(sql);
	        	  } catch (SQLException e){
	        		  System.err.println("insert:"+ e.getClass().getName() + ": " + e.getMessage() );
	        	  }
	          }
	          stmt.close();
	          c.commit();
	    } catch ( Exception e ) {
	    	System.err.println("exc: " +e.getClass().getName() + ": " + e.getMessage() );
	    }  
	}

	
	public static ArrayList<Long> choose10MostRetweeted()
	  {
	    ArrayList<Long> result = new ArrayList<Long>();
		Statement stmt = null;
	    try {
	    	openConnection();
	    	stmt = c.createStatement();
	    	ResultSet rs = stmt.executeQuery( 
	    			"SELECT * FROM Tweet ORDER BY retweet_count DESC;" );
	      int i = 0;
	      while ( rs.next() && i < 10) {
	         Long id = rs.getLong("tweet_id");
	         result.add(id);
	         i++;
	      }
	      rs.close();
	      stmt.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("choose retweeted done successfully");
	    return result;
	  }
	public static ArrayList<Long> choose10MostFav()
	  {
	    ArrayList<Long> result = new ArrayList<Long>();
		Statement stmt = null;
	    try {
	    	openConnection();
	    	stmt = c.createStatement();
	    	ResultSet rs = stmt.executeQuery( 
	    			"SELECT * FROM Tweet ORDER BY fav_count DESC;" );
	      int i = 0;
	      while ( rs.next() && i < 10) {
	         Long id = rs.getLong("tweet_id");
	         result.add(id);
	         i++;
	      }
	      rs.close();
	      stmt.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("choose favourited done successfully");
	    return result;
	  }
	
}