package com.IDS594.APITests;

import java.io.FileReader;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
public class GenerateFreelancerSkills {
	//This class takes the JSON file generated in previous step using GetUserSkills.java and write into a text file
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Read data from JSON
		/*JSONParser parser = new JSONParser();
		 
		try {
	 
			Object obj = parser.parse(new FileReader("JSONFiles//freelancerSkills.json"));
	 
			JSONObject jsonObject = (JSONObject) obj;
			// loop array
			JSONArray msg = (JSONArray) jsonObject.get("freelancerArray");
			Iterator<String> iterator = ((Object) msg).iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
	}*/
		System.out.println("Nka");
	}
}
