package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;


public class Dynamo {
	
	static DynamoDB database;

	public static DynamoDB getdatabase() {
		return database;
	}

	public static void setdatabase(AWSCredentials credentials) {
		Dynamo.database = new DynamoDB(new AmazonDynamoDBClient(credentials));
	}
	
	public static void createTable(String tableName) {
    	ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));
		
		ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
		keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));
		
		CreateTableRequest request = new CreateTableRequest()
										.withTableName(tableName)
										.withKeySchema(keySchema)
										.withAttributeDefinitions(attributeDefinitions)
										.withProvisionedThroughput(new ProvisionedThroughput()
										.withReadCapacityUnits(5L)
										.withWriteCapacityUnits(6L));
		
		Table table = Dynamo.database.createTable(request);
		try {
			table.waitForActive();
		} catch (InterruptedException e) {
			System.out.println("Fail to create Table:" + tableName);
			e.printStackTrace();
		}
		System.out.println("Successfully create Table:" + tableName);
    }
	
	public static void deleteTable(String tableName) {

        Table table = database.getTable(tableName);
        try {
            table.delete();
            table.waitForDelete();
        } catch (Exception e) {
            System.out.println("Fail to delete Table:" + tableName);
            System.err.println(e.getMessage());
        }
		System.out.println("Successfully delete Table:" + tableName);
    }
	
	public static void updateTable(String tableName) {
		Table table = database.getTable(tableName);
		
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
				.withReadCapacityUnits(15L).withWriteCapacityUnits(12L);
		table.updateTable(provisionedThroughput);
		
		try {
			table.waitForActive();
		} catch (InterruptedException e) {
            System.out.println("Fail to update Table:" + tableName);
			e.printStackTrace();
		}
		System.out.println("Successfully update Table:" + tableName);
	}
	
	public static void putItem(String tableName, Item item) {
		Table table = database.getTable(tableName);
		PutItemOutcome outcome = table.putItem(item);
		if(outcome == null) {
			System.out.println("Fail to put item" + item.toJSON());
		}
	}
	
	
	public static Item getItem(String tableName, String name) {
		Table table = database.getTable(tableName);
		Item item = table.getItem("Name", name);
		return item;
	}
	
	public static void updateAttributes(String tableName, String name, String colName, Object colValue) {
		 Table table = database.getTable(tableName);
		 
		 try {
			 Map<String, String> expressionAttributeNames = new HashMap<String, String>();
			 expressionAttributeNames.put("#a", colName);
			 Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
			 expressionAttributeValues.put(":val", colValue);
			 
			 UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Name", name).withUpdateExpression("add #a :val")
					 						 .withNameMap(expressionAttributeNames)
											 .withValueMap(expressionAttributeValues)
											 .withReturnValues(ReturnValue.ALL_NEW);
			 UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
			 System.out.println("Printing item after multiple attribute update...");
			 System.out.println(outcome.getItem().toJSONPretty());
			 } catch (Exception e) {
			 System.err.println("Failed to update multiple attributes in "
			 + tableName);
			 System.err.println(e.getMessage());
		 }
	}

	
	
	public static void deleteItem(String tableName, String name) {
		Table table = database.getTable(tableName);
		DeleteItemOutcome outcome = table.deleteItem("Name", name);
		if(outcome == null) {
			System.out.println("Fail to get item" + name);
		}
	}
	
	public static void deleteItem(String tableName, int time) {
		Table table = database.getTable(tableName);
		DeleteItemOutcome outcome = table.deleteItem("time", time);
		if(outcome == null) {
			System.out.println("Fail to get item" + time);
		}
	}
	
}
