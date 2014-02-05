import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TDemo {

	private final static String CONSUMER_KEY = 
			"qEi61p5JVsCCtPPHr5MdBg";
	private final static String CONSUMER_KEY_SECRET = 
			"IQGufiBmzY0X54elYWjOqzUuPPx1u9ywrJMxrzUk";
	
	private static Twitter twitter;
	
	private static Twitter startNew() throws TwitterException, IOException{
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
				
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken){
			RequestToken requestToken = twitter.getOAuthRequestToken();
			System.out.println("Authorization URL: \n" + requestToken.getAuthorizationURL());
			try{
				System.out.print("Input PIN here: ");
				String pin = br.readLine();
				accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			} catch (TwitterException te2){
				System.out.println("Uncorrect PIN, try again.");
			}
		}
		return twitter;
	}
	
	public static void login() {
		if (twitter == null){
			try{
				twitter = startNew();
			} catch (Exception e){
				System.out.println("Unable to connect to twitter.");
			}
		}
	}
	
	public static void updateStatus(String status){
		try{	
			twitter.updateStatus(status);
		} catch (Exception te){
			System.out.println("Failed to update status");
		}
	}
	
	public static void getFriends(){
		PagableResponseList<User> friends = null;
		try {
			friends= twitter.getFriendsList(twitter.getScreenName(), -1);
			ArrayList<User> names = new ArrayList<User>();
			for(User friend : friends){
				names.add(friend);
			}
			SQLiteJDBC.addFriends(names);
		} catch (Exception e) {
			System.out.println("Failed to update friends list.");
		}
		
	}
	
	public static void chooseTweets(){
		ArrayList<String> friends = SQLiteJDBC.getFriends();
		ResponseList<Status> tweets = null;
		for (String friend : friends){
			try {
				tweets = twitter.getUserTimeline(friend);
			} catch (TwitterException e) {
				System.out.println("Failed to get tweets of user " + friend);
			}
			SQLiteJDBC.addTweets(tweets);
		}
		
		System.out.println("*****************************************");
		ArrayList<Long> tw = SQLiteJDBC.choose10MostRetweeted();
		 for (long t : tw){
			 try {
					Status s = twitter.showStatus(t);
					System.out.println("TEXT: " + s.getText());
					System.out.println("retw: " + s.getRetweetCount() + "fav: " + s.getFavoriteCount());
				} catch (TwitterException e) {
					System.out.println("Failed to get tweet " + t);
				}
		 }
			System.out.println("*****************************************");
		tw = SQLiteJDBC.choose10MostFav();
		for (Long t : tw){
			 try {
					Status s = twitter.showStatus(t);
					System.out.println("TEXT: " + s.getText());
					System.out.println("retw: " + s.getRetweetCount() + "fav: " + s.getFavoriteCount());
				} catch (TwitterException e) {
					System.out.println("Failed to get tweet " + t);
				}
		 }
	}
	
}
