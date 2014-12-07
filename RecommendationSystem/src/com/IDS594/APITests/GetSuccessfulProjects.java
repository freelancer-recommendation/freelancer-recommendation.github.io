package com.IDS594.APITests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetSuccessfulProjects {
	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		BufferedReader randomIds;
		JSONObject freelancerSkills = new JSONObject();
		freelancerSkills.put("code", "finishedProjects");
		
		try {
			randomIds = new BufferedReader(new FileReader("input//randomFreelancers.txt"));
			//Read the freelancers in to an arraylist so that accessing becomes easy
			ArrayList<String> listOfFreelancers = readFreelancerIds(randomIds);
			JSONArray freelancerDetails = new JSONArray();
			for(int index = 2400; index < 2500;index++){
				String userName = listOfFreelancers.get(index);
				JSONObject jsonObj = getResponse("https://api.freelancer.com/Project/Search.json?winner="+userName);
				//System.out.println(jsonObj.toString());
				//Push these JSON Objects in to JSON Array
				freelancerDetails.put(jsonObj);
			}
			//Write this array in to another JSON File
			freelancerSkills.put("freelancerArray", freelancerDetails);
			writeJSONFile(freelancerSkills);
			System.out.println("Completed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//write content to a JSON file
	private static void writeJSONFile(JSONObject freelancerSkills) throws IOException{
		
		FileWriter file = new FileWriter("JSONFiles//freelancerInfo//freelancerFinishedJobs2401-2500.json");
		file.write(freelancerSkills.toString());
		file.flush();
		file.close();
		
	}

	private static ArrayList<String> readFreelancerIds(BufferedReader randomIds) {
		ArrayList<String> ids = new ArrayList<String>();
		try {
			while (randomIds.ready()) {
				ids.add(randomIds.readLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			randomIds.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}

	private static JSONObject getResponse(String requestURL) throws Exception{
		InputStream input;
		input = new URL(requestURL).openStream();
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
		String content = fetchContent(bufferReader);
		JSONObject obj;
		obj = new JSONObject(content);
		input.close();
		return obj;
	}

	private static String fetchContent(BufferedReader bufferReader) throws Exception{
		StringBuilder builder = new StringBuilder();
	    int retVal;
	    while ((retVal = bufferReader.read()) != -1) {
	      builder.append((char) retVal);
	    }
	    return builder.toString();
	}

}
