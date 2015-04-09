package src;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import api.AlchemyAPI;

public class SentimentAnalyzer {
	
	private AlchemyAPI alchemyObj;
	
	public SentimentAnalyzer() throws FileNotFoundException, IOException
	{
		alchemyObj = AlchemyAPI.GetInstanceFromFile("api_key.txt");
	}

	public int getSentiment(String text) throws DOMException, XPathExpressionException, IOException, SAXException, ParserConfigurationException
	{
		String sentiment = alchemyObj.TextGetTextSentiment(text).getElementsByTagName("type").item(0).getTextContent();
	  
	    if(sentiment.equals("positive"))
	    	return 1;
	    else 
	    	return 0;
	}
}
