import java.sql.Date;


public class TweetObj {
	public String username;
	public String content;
	public Date creatDate;
	public int commentCount, retweetCount;
	
	public TweetObj(String usrname, String contnt, Date cDate,
			int comments, int retweets){
		username = usrname;
		content = contnt;
		creatDate = cDate;
		commentCount = comments;
		retweetCount = retweets;
	}
}
