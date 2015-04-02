package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SQSManager 
{
	
	private static final String CREDENTIALS_FILE = "AwsCredentials.properties";
	private static final String QUEUE_NAME = "TweetQueue";
	private AmazonSQS sqs;
	private String myQueueUrl = null;
	
	public SQSManager()
	{
		AWSCredentials credentials = null;
		// Load the AWS credentials file
		try {
			credentials = new PropertiesCredentials(
					ElasticBean.class.getResourceAsStream(CREDENTIALS_FILE));
		} catch (Exception e) {
			throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct, and is in valid format.", e);
		}

        sqs = new AmazonSQSClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);

/*        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");*/

        try {
            // Create a queue
            System.out.println("Creating TweetQueue.\n");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(QUEUE_NAME);
            myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();

/*            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();*/
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        }
	}
	
	public void sendMessages(String message)
	{
		if(myQueueUrl != null)
		{
			try{
	            // Send a message
	            System.out.println("Sending a message to MyQueue.\n");
	            sqs.sendMessage(new SendMessageRequest(myQueueUrl, message));

	        } catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered " +
	                    "a serious internal problem while trying to communicate with SQS, such as not " +
	                    "being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
		}
	}
	
	public List<Message> receiveMessages()
	{
		List<Message> messages = new ArrayList<Message>();
		
		if(myQueueUrl != null)
		{
			try{
				// Receive messages
		        System.out.println("Receiving messages from MyQueue.\n");
		        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
		        messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		        
		        /* 
		        for (Message message : messages) 
		        {
		           System.out.println("  Message");
		            System.out.println("    MessageId:     " + message.getMessageId());
		            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
		            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
		            System.out.println("    Body:          " + message.getBody());
		            for (Entry<String, String> entry : message.getAttributes().entrySet()) {
		                System.out.println("  Attribute");
		                System.out.println("    Name:  " + entry.getKey());
		                System.out.println("    Value: " + entry.getValue());
		            }
		        }*/
			} catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered " +
	                    "a serious internal problem while trying to communicate with SQS, such as not " +
	                    "being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
		}
        return messages;
        
	}
	
	public void deleteMessages(List<Message> messages)
	{
		if(myQueueUrl != null)
		{
			try{
				// Delete a message
		        System.out.println("Deleting a message.\n");
		        String messageReceiptHandle = messages.get(0).getReceiptHandle();
		        sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));
			} catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered " +
	                    "a serious internal problem while trying to communicate with SQS, such as not " +
	                    "being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
		}
        
	}
	
	public void deleteQueue()
	{
		if(myQueueUrl != null)
		{
			try{
				// Delete a queue
		        System.out.println("Deleting the test queue.\n");
		        sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
			} catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered " +
	                    "a serious internal problem while trying to communicate with SQS, such as not " +
	                    "being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
		}
	}
	
	
}
