package src;

import com.amazonaws.services.dynamodbv2.document.Item;

public class Tweet {
	private String twitID;
	private String userName;
	private double lattitude;
	private double longitude;
	private String content;
	private String display;
	private int topic;
	private int time;	
	private int sentiment;
	
	
	public Tweet(String tid, String unm, double lat, double log, String con, int top, int tim)
	{
		this.twitID = tid;
		this.userName = unm;
		this.lattitude = lat;
		this.longitude = log;
		this.content = con;
		this.topic = top;
		this.time = tim;
		
		if(content.length() > 100)
		{
			display = content.substring(0,101);
		}
		
	}
	
	/*public void computeSentiment()
	{
		Achly.computeSentiment(content);
	}*/
	
	public Item buildItem()
	{
		Item item = new Item()
	    .withPrimaryKey("Name", twitID)
	    .withString("userName", userName)
	    .withDouble("lattitude", lattitude)
	    .withDouble("longitude", longitude)
	    .withString("content", content)
	    .withString("display",  display)
	    .withInt("time", time)
	    .withInt("topic", topic)
	    .withInt("sentiment", sentiment);
		
		return item;
	}
	
	public String getTwitID() {
		return twitID;
	}
	public void setTwitID(String twitID) {
		this.twitID = twitID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public double getLattitude() {
		return lattitude;
	}
	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getContent() {
		return content;
	}
	public void setWords(String content) {
		this.content = content;
	}
	public int getTopic() {
		return topic;
	}
	public void setTopic(int topic) {
		this.topic = topic;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getSentiment() {
		return sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	
}
