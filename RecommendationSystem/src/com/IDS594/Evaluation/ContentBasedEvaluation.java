package com.IDS594.Evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ContentBasedEvaluation {

	public final static int max_limit = 5;
	public static void main(String[] args) {
		//Create required Matrix
		String[] freelancerMatrix = new String[3900];
		String[] projectMatrix = new String[30000];
		String[] skills = new String[1000];
		int[][] user_Skill_Matrix = new int[3900][max_limit];
		int[][] project_Skill_Matrix = new int[30000][max_limit];
		double recall_total = 0;
		double precision_total = 0;
		//Read all the required files
		String freelancerData = "textFiles/test2.txt";
		String projectData = "textFiles/ProjectAndSkills.txt";
		
		try{
			int skill = 0;
			//Read content
			BufferedReader freelancerFile = new BufferedReader(new FileReader(freelancerData));
			BufferedReader projectFile = new BufferedReader(new FileReader(projectData));
			int projectId = 0;
			//From the txt files generated into textfiles folder separate the content for the matrix
			//Populate Project Skills Matrix
			String line = projectFile.readLine();
			while(line != null){
				String[] content = line.split("@    ");
				String projectName = content[1];
				projectMatrix[projectId] = projectName;
				line = projectFile.readLine();
				String[] skillContent = line.split(",");
				//populateProjectSkillMatrix(skillContent,skill,project_Skill_Matrix,index,skills);
				for(int Subindex = 0; Subindex < skillContent.length; Subindex++){
					if(Subindex >= max_limit){
						//we just need top 5 skills for developer
						break;
					}
					else{
						//Read Skills
						String temp = "";
						temp = skillContent[Subindex];
						//populate project skill matrix
						boolean found = false;
						for (int subInd = 0; subInd < skill; subInd++){
							//TODO: Change datastructure to arraylist at least to search
							if (temp.equals(skills[subInd])){
								project_Skill_Matrix[projectId][Subindex] = subInd + 1;
								found = true;
								break;
							}
						}
						if (!found){
							skills[skill] = temp;
							project_Skill_Matrix[projectId][Subindex]= skill + 1;
							skill++;
						}
					}
				}
				line = projectFile.readLine();
				projectId++;
			}
			projectFile.close();//At this step I have all the projects stored in projectMatrix
			//Edit: Content is written to a file so that we can use it while performing CF Algorithms
			String projects = "output/ProjectNames_For_CF.txt";
			BufferedWriter projectNames_ForCF = new BufferedWriter(new FileWriter(projects));
			writeToFile(projectNames_ForCF,projectId,projectMatrix);
			//Now read content from SuccessfulBidsWithSkills file
			//BufferedReader successfulBids = new BufferedReader(new FileReader("textFiles/SuccessfulBidsWithSkills.txt"));
			BufferedReader successfulBids = new BufferedReader(new FileReader("textFiles/test.txt"));
			String readTextContent = successfulBids.readLine();
			while(readTextContent != null){
				//Read line with Projects and skills
				readTextContent = successfulBids.readLine();
				//boolean emptySkillscondition = (readTextContent.length() == 0) || readTextContent.contains("Name:");
				//boolean skillscondition = (readTextContent.length() == 0) && (!readTextContent.contains("Name:"));
				//Handle condition if a user has no skill sets
				while((readTextContent.length() == 0) || readTextContent.contains("Name:")){
					readTextContent = successfulBids.readLine();	
				}
				while((readTextContent != null) && (!readTextContent.contains("Name:"))){
					/*if(readTextContent.equalsIgnoreCase("Wordpress theme modification->CSS,HTML,PHP,Wordpress")){
						System.out.println("blah");
					}*/
					String[] proj_Skills = readTextContent.split(":::");
					String pName = proj_Skills[0];
					String sSet = proj_Skills[1];
					boolean found = false;
					int foundAt = 0;
					for(int index = 0; index < projectId; index++){
						//Check if project exists in our actual array
						if (projectMatrix[index].equals(pName)){
							found = true;
							foundAt = index;
							break;
						}
						else{
							projectMatrix[projectId] = pName;
						}
					}
					//Read all skills individually
					String[] skill_set = sSet.split(",");
					for(int ind = 0; ind < skill_set.length; ind++){
						if (ind >= max_limit){
							break;
						}
						String temp = "";
						temp = skill_set[ind];
						boolean hit = false;
						for (int skillIndex = 0; skillIndex < skill; skillIndex++){
							//Check existence in actual skill array
							if (temp.equals(skills[skillIndex])){
								project_Skill_Matrix[projectId][ind] = skillIndex+1;
								hit = true;
								break;
							}
						}
						//if not found add to actual skill array
						if (!hit){
							skills[skill] = temp;
							project_Skill_Matrix[projectId][ind] = skill+1;
							skill++;
						}
					}
					readTextContent = successfulBids.readLine();
					while((readTextContent!=null) && ((readTextContent.length() == 0) || (readTextContent.contains("Name:")))){
						readTextContent = successfulBids.readLine();	
					}
					if (found){
						boolean matchProj_Skills = true;
						for (int skillInd = 0; skillInd < max_limit; skillInd++){
							if (project_Skill_Matrix[projectId][skillInd] != project_Skill_Matrix[foundAt][skillInd])matchProj_Skills = false;
						}
						if (matchProj_Skills)continue;						
					}
					projectId++;
				}
			}
			successfulBids.close();
			
			//Read Freelancer names in to Array now
			int freelancerId = 0;
			//Populate User_Skill matrix
			String uLine = freelancerFile.readLine();
			while(uLine != null){
				String[] userContent =  uLine.split(":");
				String[] token = userContent[1].split(" ");
				freelancerMatrix[freelancerId] = token[0];
				//populateUserSkillMatrix(token,skill,user_Skill_Matrix,index,skills);
				for(int Subindex = 0; Subindex < token.length - 1; Subindex++){
					if(Subindex >= max_limit){
						//we just need top 5 skills for developer
						break;
					}
					String temp = token[Subindex+1];
					boolean found = false;
					for (int subInd = 0; subInd < skill; subInd++){
						//TODO: Change data structure to arraylist at least to search
						if (temp.equals(skills[subInd])){
							user_Skill_Matrix[freelancerId][Subindex] = subInd + 1;
							found = true;
							break;
						}
					}
					if (!found){
						skills[skill] = temp;
						user_Skill_Matrix[freelancerId][Subindex]= skill + 1;
						skill++;
					}
				}
				uLine = freelancerFile.readLine();
				freelancerId++;
			}
			freelancerFile.close();//At this step I have all the freelancers stored in freelancerMatrix
			//Edit: Content is written to a file so that we can use it while performing CF Algorithms
			String freelancer_ForCF = "output/FreelancerNames_For_CF.txt";
			String skills_ForCF = "output/SkillNames_For_CF.txt";
			BufferedWriter freelancerNames_ForCF = new BufferedWriter(new FileWriter(freelancer_ForCF));
			BufferedWriter skillNames_ForCF = new BufferedWriter(new FileWriter(skills_ForCF));
			writeToFile(freelancerNames_ForCF,freelancerId,freelancerMatrix);
			writeToFile(skillNames_ForCF,skill,skills);
			successfulBids = new BufferedReader(new FileReader("textFiles/test.txt"));
			readTextContent = null;
			readTextContent = successfulBids.readLine();
			//prepare an output file to write values
			String output = "output/ContentBasedEvaluation_Result.txt";
			String rating = "output/ContentBasedEvaluation_Rating.txt";
			BufferedWriter result = new BufferedWriter(new FileWriter(output));
			BufferedWriter ratings = new BufferedWriter(new FileWriter(rating));
			int userId = 0;			
			while (readTextContent != null){
				String[] pretokens = readTextContent.split("Name:");
				String freelancerName = pretokens[1].substring(0, pretokens[1].length()-1);
				int userid;
				for (userid = 0; userid < freelancerId; userid++){
					if (freelancerMatrix[userid].equals(freelancerName)){
						break;
					}
				}
				int[] userProjs = new int [1000];
				int userPid = 0;
				readTextContent = successfulBids.readLine();
				//boolean emptySkillscondition = (readTextContent.contains("Name:")) || (readTextContent.length()==0);
				//boolean skillscondition = (!readTextContent.contains("Name:")) || (readTextContent.length() == 0);
				while((readTextContent.length()==0) || (readTextContent.contains("Name:"))){
					readTextContent = successfulBids.readLine();
				}
				while((readTextContent != null) && (!readTextContent.contains("Name:"))){
					String [] proj_skills = readTextContent.split(":::");
					String proj = proj_skills[0];
					String skillSet = proj_skills[1];
					for (int projInd = 0; projInd < projectId; projInd++){
						if (projectMatrix[projInd].equals(proj)){
							userProjs[userPid] = projInd;
							//write freelancer id and project id that he bid successfully
							ratings.write(userid + "," + projInd +"\n");				
							userPid++;
							break;
						}
					}
					readTextContent = successfulBids.readLine();
				}
				//Same logic as in recommend project
				int[] recommendation = new int [max_limit];
				for (int ind2 = 0; ind2 < max_limit; ind2++){
					recommendation[ind2] = ind2;
				}
				double [] scores = new double [max_limit];
				//initialize with zeroes
				scores = initialize(max_limit,scores);
				for (int projId = 0; projId<projectId; projId++){
					double value = 0;
					double required_score = 0;
					for (int sid = 0; sid < max_limit; sid++){
						if (project_Skill_Matrix[projId][sid]!=0){
							required_score++;
						}
					}
					for (int ssid=0; ssid < max_limit; ssid++){
						for (int skillId = 0; skillId < max_limit; skillId++){
							if ((user_Skill_Matrix[userid][ssid] == project_Skill_Matrix[projId][skillId])&&(project_Skill_Matrix[userid][ssid]!=0)){
								value++;
								break;
							}
						}
					}
					value = value/required_score;
					for (int tempInd = 0; tempInd < max_limit; tempInd++){
						if (scores[tempInd] < value){
							for (int tempJ = max_limit - 1; tempJ > tempInd; tempJ--){
								scores[tempJ] = scores[tempJ-1];
								recommendation[tempJ] = recommendation[tempJ-1];
							}
							scores[tempInd] = value;
							recommendation[tempInd] = projId;
							break;
						}
					}					
				}
				//Compute Precision, Recall
				int relevant = 0;
				for(int ind = 0; ind < max_limit; ind++){
					for (int uid = 0; uid < userPid; uid++){
						if (recommendation[ind] == userProjs[uid]){
							relevant++;
						}
					}
				}
				double precision = (double)relevant/(double)userPid;
				
				if (userPid==0){
					precision=0;
				}
				double recall = (double)relevant/(double)max_limit;
				System.out.println(userId + " " +freelancerName + " : Precision:: " + precision + ", Recall:: " + recall + "\n");
				result.write(userId + " " +freelancerName + " : Precision:: " + precision + ", Recall:: " + recall + "\n");
				if(userId == 1091){
					System.out.println("Test");
				}
				recall_total = recall_total + recall;
				precision_total = precision_total + precision;
				userId++;
			}
			result.write("Avgerage Precision: " + precision_total/userId + ",Avgerage Recall: " + recall_total/userId + "\n");
			successfulBids.close();
			result.close();
			ratings.close();
		}
		catch(Exception e){
			System.out.println("Exception in main function of RecommendProject"+e);
		}
		System.out.println("----Program done and exited----");
	}
	//Write Content to a file
	private static void writeToFile(BufferedWriter bufferWriter,
			int id, String[] matrix) throws IOException {
		for (int index = 0 ; index < id; index++){
			bufferWriter.write(matrix[index]+"\n");
		}
		bufferWriter.close();
		
	}
	private static double[] initialize(int limit, double[] array) {
		for (int ind = 0; ind < limit; ind++){
			array[ind] = 0;
		}
		return array;
	}
}
