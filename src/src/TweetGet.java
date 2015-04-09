package src;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.sqs.model.Message;

import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
 * Usage: java twitter4j.examples.PrintSampleStream<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class TweetGet {
	
	private static final String myConsumerKey = "2CSfLvOdt73Vu9maT00FF1Yk1";
	private static final String myConsumerSecret = "d45mzy4nsIKinhlXaPMkd2ynWNenERWWuFzXzZIvKj67urXtRr";
	private static final String myAccessToken = "2791074198-0tunYEUHYKkHUdDnpeE8DrQJtDygFk6jpp4BcAA";
	private static final String myTokenSecret = "oUFn7OhsiuOQB0nLgx2SOUZKxnoJqE8PqqBMrKhaRidnB";
	
	private static SQSManager sqsManager = new SQSManager();
	private static SentimentAnalyzer analyzer;
	
	private static int time = 0;
	private static int delItem = 0;
    

	public static void main(String[] args) throws TwitterException, SQLException, NamingException, FileNotFoundException, IOException
	{
		getTweet(false);
	}
	
    public static void getTweet(boolean createDB) throws TwitterException, SQLException, NamingException, FileNotFoundException, IOException 
    {
    	analyzer = new SentimentAnalyzer();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		   .setOAuthConsumerKey(myConsumerKey)
		   .setOAuthConsumerSecret(myConsumerSecret)
		   .setOAuthAccessToken(myAccessToken)
		   .setOAuthAccessTokenSecret(myTokenSecret);
		
		//Dynamo.createTable(table);
        
		final String table = "TwitterMap";
        final String[] items = {"TwitID", "UserName", "Lattitude", "Longitude", "Content", "Display", "Topic", "Time", "Sentiment"};
		SQLBase.connectDB();
        
        if(createDB)
        {
			try {
				SQLBase.beginCommands();
				SQLBase.dropTable(table);
				SQLBase.endCommands();
				
				SQLBase.beginCommands();
				SQLBase.createTable(table, items);
				SQLBase.endCommands();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
        }
		
		
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {
        	
            public void onStatus(Status status) {
                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            	GeoLocation location = status.getGeoLocation();
            	if(location != null)
            	{
	        		User user = status.getUser();
	        		String content = status.getText();
	        		String display = new String(content);
	        		
	        		Topic topics = new Topic();
	        		topics.computeScores(content);
	        		int topic = topics.getTopic();
	        		
	        		int sentiment = 0;
	        		try {
						sentiment = analyzer.getSentiment(content);
					} catch (DOMException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (XPathExpressionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        		
	        		if(display.length() > 100)
	        		{
	        			display = display.substring(0,101);
	        		}
	        		
	        		      		
	        		if(time == 100)
	        		{
	        			time = 0;		
	        		}
	        		
	        		try {
		        		int cnt = SQLBase.getCount(table);
	        			//int cnt = Dynamo.getCount(table);
		        		System.out.println(cnt);
		        		if(cnt >= 100)
		        		{
		        			for(int i = 0; i < cnt-99; i++)
		        			{
		        				delItem++;		        	
		        				//Dynamo.deleteItem(table, delItem);
		        				SQLBase.beginCommands();
		        				SQLBase.deleteItem(table, "Time", Integer.toString((delItem)));
		        				SQLBase.endCommands();
		        			}
		        		}		  		
	        		} catch (SQLException e) {
						e.printStackTrace();
					}
	        		
	        		if(delItem == 100)
	        		{
	        			delItem = 0;
	        		}
	        		
	        		time++;
	        		
	        		//Tweet tweet = new Tweet(Long.toString(status.getId()), user.getName(), location.getLatitude(), location.getLongitude(),
	        		//				content, topic, time);
	        		String[] values = {Long.toString(status.getId()), user.getName(), 
	        				Double.toString(location.getLatitude()), Double.toString(location.getLongitude()), content, display, 
	        				Integer.toString(topic), Integer.toString(time), Integer.toString(sentiment)};
	        		
	        		try {
	        			
	        			SQLBase.beginCommands();
	        			SQLBase.insertItem(table, items, values);
	        			SQLBase.endCommands();	
	        			
	        			//Item item = tweet.buildItem();
	        				
	        			//Dynamo.putItem(table, item);
	        			
	        			sqsManager.sendMessages("1");
	        			
	        			
	        			
	        			
	        			List<Message> messages = sqsManager.receiveMessages();
	        			for(Message msg : messages)
	        			{
	        				if(msg.getBody().equals("1"))
	        				{
	        					//tweet.computeSentiment();
	        					//Dynamo.updateAttributes(table, tweet.getTwitID(), "sentiment", tweet.getSentiment());;
	        					sqsManager.deleteMessages(messages);
	        					break;
	        				}
	        			}
	        			
	        			
	        			
	        			
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
	        		
	        		//System.out.println("Topic: " + topic);
	        		//System.out.println("ID: " + status.getId() + "\nLocation: " + location);
	        		//System.out.println("User Name: " + user.getName() + "\nScreen Name:" + user.getScreenName());
	        		//for(String a : keywords)
	        			//System.out.println(a);
        		}
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                
				try {
					//Dynamo.deleteItem(table, Long.toString(statusDeletionNotice.getStatusId()));
					SQLBase.beginCommands();
					SQLBase.deleteItem(table, "TwitID", Long.toString(statusDeletionNotice.getStatusId()));
					SQLBase.endCommands();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
            }

            
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.sample();
    }
}