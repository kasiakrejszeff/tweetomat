import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;


public class TwDemo {

	private final static String CONSUMER_KEY = 
			"qEi61p5JVsCCtPPHr5MdBg";
	private final static String CONSUMER_KEY_SECRET = 
			"IQGufiBmzY0X54elYWjOqzUuPPx1u9ywrJMxrzUk";
	
	private static Twitter twitter;
	
	private static Twitter authenticateApp() throws TwitterException
	{
	ConfigurationBuilder builder = new ConfigurationBuilder();
	builder.setUseSSL(true);
	                builder.setApplicationOnlyAuthEnabled(true);
	builder.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_KEY_SECRET);
	   OAuth2Token token = new TwitterFactory
	   	(builder.build()).getInstance().getOAuth2Token();
	        
	ConfigurationBuilder cb = new ConfigurationBuilder();
	cb.setUseSSL(true)
	 .setApplicationOnlyAuthEnabled(true)
	 .setOAuthConsumerKey(CONSUMER_KEY)
	 .setOAuthConsumerSecret(CONSUMER_KEY_SECRET)
	 .setOAuth2TokenType(token.getTokenType())
	 .setOAuth2AccessToken(token.getAccessToken());
	TwitterFactory tf = new TwitterFactory(cb.build());
	return tf.getInstance();
	}
	
	public static void updateStatus(String status){
		if (twitter == null){
			try{
				twitter = authenticateApp();
			}catch (TwitterException e){
				System.out.println("Failed to authenticate application.");
			}
		}
		try{
			twitter.updateStatus(status);
		} catch (TwitterException te){
			System.out.println("Failed to update status.");
		}
	}
}
