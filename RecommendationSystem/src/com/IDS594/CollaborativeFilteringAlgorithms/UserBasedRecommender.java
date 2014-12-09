package com.IDS594.CollaborativeFilteringAlgorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserBasedRecommender {
	public final static int max_limit = 5;
	public final static int nearest_Neigbours = 10;
	public static void main(String[] args) {
		String[] freelancerMatrix = new String[3900];
		String[] projectMatrix = new String[30000];
		
		//Read all the required files
		String evaluatedFile = "output/ContentBasedEvaluation_Rating.txt";
		String projectData = "output/ProjectNames_For_CF.txt";
		String freelancertData = "output/FreelancerNames_For_CF.txt";
		int pid = 0;
		int freelancerId = 0;
		try{
			BufferedReader projectNames = new BufferedReader(new FileReader(projectData));
			BufferedReader freelancerNames = new BufferedReader(new FileReader(freelancertData));
			String projectContent = projectNames.readLine();
			String freelancerContent = freelancerNames.readLine();
			while(freelancerContent!=null){
				freelancerMatrix[freelancerId] = freelancerContent;
				freelancerId++;
				freelancerContent = freelancerNames.readLine();
			}
			while(projectContent!=null){
				projectMatrix[pid] = projectContent;
				pid++;
				projectContent = projectNames.readLine();
			}
			//Define the data model
			DataModel dataModel = new FileDataModel(new File(evaluatedFile));
			//Use Tanimoto co-efficient similarity
			UserSimilarity similarity = new TanimotoCoefficientSimilarity (dataModel);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood (nearest_Neigbours, similarity, dataModel);
			Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
			String result = "output/CF_UserBasedRecommender_Result.txt";
			BufferedWriter res = new BufferedWriter(new FileWriter(result));
			for (LongPrimitiveIterator freelancer = dataModel.getItemIDs(); freelancer.hasNext();){
				long free_id = freelancer.nextLong();
				List<RecommendedItem> recommendations = recommender.recommend(free_id, nearest_Neigbours);
				if (free_id > freelancerMatrix.length){
					break;
				}
				if (recommendations.isEmpty()){
					continue;
				}
				String content = "";
				content = "\n"+freelancerMatrix[(int)free_id-1] + " Can be Recommended ";
				for (RecommendedItem recommendation : recommendations){
					if ((int)recommendation.getItemID() > pid){
						continue;
					}
					content = content + " " + projectMatrix[(int)recommendation.getItemID()-1]	+ ", ";
				}
				/*if(freelancerMatrix[(int)free_id-1].equalsIgnoreCase("kkroz")){
					System.out.println("Blah");
				}*/
				System.out.println(content);
				res.write(content);
			}
			freelancerNames.close();
			projectNames.close();
			res.close();
			System.out.println("--Program Finished--");
		}
		catch(Exception e){
			
		}
	}

}
