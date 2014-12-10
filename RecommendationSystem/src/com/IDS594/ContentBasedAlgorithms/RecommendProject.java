package com.IDS594.ContentBasedAlgorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
/*
 * This program recommends Projects to all freelancers
 */
public class RecommendProject {
	public final static int max_limit = 5;
	public static void main(String[] args) {
		//Create required Matrix
		String[] freelancerMatrix = new String[3900];
		String[] projectMatrix = new String[9750];
		String[] skills = new String[1000];
		int[][] user_Skill_Matrix = new int[3900][max_limit];
		int[][] project_Skill_Matrix = new int[9750][max_limit];
		//Read all the required files
		String freelancerData = "textFiles/UserAndSkills.txt";
		String projectData = "textFiles/ProjectAndSkills.txt";
		
		try{
			int skill = 0;
			//Read content
			BufferedReader freelancerFile = new BufferedReader(new FileReader(freelancerData));
			BufferedReader projectFile = new BufferedReader(new FileReader(projectData));
			String output = "output/RecommendProject_ContentBased.txt";
			BufferedWriter result = new BufferedWriter(new FileWriter(output));
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
			projectFile.close();
			int freelancerId = 0;
			//Populate User_Skill matrix
			String uLine = freelancerFile.readLine();
			while(uLine != null){
				String[] userContent =  uLine.split("@");
				String[] token = userContent[1].split(",");
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
						//TODO: Change datastructure to arraylist at least to search
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
			freelancerFile.close();
			
			for (int freelancer_id = 0; freelancer_id < freelancerId; freelancer_id++){
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
							if ((user_Skill_Matrix[freelancer_id][ssid] == project_Skill_Matrix[projId][skillId])&&(project_Skill_Matrix[freelancer_id][ssid]!=0)){
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
				result.write("Freelancer " + freelancer_id + "(" + freelancerMatrix[freelancer_id] + "): ");
				for (int val = 0 ; val < max_limit; val++){
					if(val != max_limit - 1){
						result.write("Project" + recommendation[val] + "(" +projectMatrix[recommendation[val]] + ")[" + scores[val] + "], ");
					}
					else{
						result.write("Project" + recommendation[val] + "(" +projectMatrix[recommendation[val]] + ")[" + scores[val] + "] ");
					}
					
				}
				result.write("\n");
			}
			result.close();
		}
		catch(Exception e){
			System.out.println("Exception in main function of RecommendProject"+e);
		}
		System.out.println("Program executed");
	}
	private static double[] initialize(int limit, double[] array) {
		for (int ind = 0; ind < limit; ind++){
			array[ind] = 0;
		}
		return array;
	}
}
