package com.IDS594.APITests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetProjectDetails {
	//Get some projects from the API to perform recommendation
	public static void main(String[] args) throws JSONException{
		// TODO Auto-generated method stub
				BufferedReader randomIds;
				JSONObject freelancerSkills = new JSONObject();
				freelancerSkills.put("code", "projects");
				
				try {
					randomIds = new BufferedReader(new FileReader("input//randomFreelancers.txt"));
					JSONArray projectDetails = new JSONArray();
					for(int index = 9500; index < 10500;index++){
						JSONObject jsonObj = getResponse("https://api.freelancer.com/Project/Properties.json?id="+index);
						//Push these JSON Objects in to JSON Array
						projectDetails.put(jsonObj);
					}
					//Write this array in to another JSON File
					freelancerSkills.put("projectArray", projectDetails);
					writeJSONFile(freelancerSkills);
					System.out.println("Completed");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			//write content to a JSON file
			private static void writeJSONFile(JSONObject freelancerSkills) throws IOException{
				
				FileWriter file = new FileWriter("JSONFiles//projectDetails9501-10500.json");
				file.write(freelancerSkills.toString());
				file.flush();
				file.close();
				
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
