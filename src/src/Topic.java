package src;

import java.util.HashMap;


public class Topic {
	private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
	private String[] word = {"party","play","fun","sing","song","draw","pub","paint","dance","music","travel","good",
			"true","hate","sport","football","basketball","tennis","so","that","no","sports","yes","swim","run",
			"fitting","food","restaurant","fruit","coke","love","cook","rice","noodles","pizza","burger","soda",
			"drink","beer","bottle","friends","apple","eat","dessert","computer","java","linux","C++","a","word",
			"write","search","bad","SQL","database","application","software","code","program","computer","windows",
			"Mac","OS","algorithm"};
	private int[] num = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,
							1,1,1,1,1,1,1,1,1,1,1,1,
							2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
							3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3};
	
	private double[] scores = {0,0,0,0};
	
	public Topic()
	{
		readWordMap("words.txt");
	}
	
	public void readWordMap(String filename)
	{
		for(int i = 0; i < word.length; i++)
    	{
    		wordMap.put(word[i], num[i]);
    	}
	}
	
	public void computeScores(String words)
	{
		String[] keywords = words.split(" ");
		int sum = 0;
		for(String word : keywords)
		{
			if(wordMap.containsKey(word))
			{
				scores[wordMap.get(word)]++;
				/*for(String tmp : keywords)
					System.out.println(tmp);*/
				sum++;
			}
		}
		if(sum != 0)
		{
			for(int i = 0; i < scores.length; i++)
				scores[i] /= (float)sum;
		}
	}
	
	public int getTopic()
	{
		double maxScore = 0;
		int index = 0;
		for(int i = 0; i < scores.length; i++)
		{
			if(scores[i] > maxScore)
			{
				maxScore = scores[i];
				index = i+1;
			}
		}
		
		if(maxScore < 0.1)
		{
			index = 0;
		}
		return index;
	}

	public double[] getScores() {
		return scores;
	}

	public void setScores(double[] scores) {
		this.scores = scores;
	}
}
