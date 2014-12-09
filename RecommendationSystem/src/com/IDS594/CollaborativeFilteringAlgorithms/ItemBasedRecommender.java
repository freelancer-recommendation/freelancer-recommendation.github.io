/*
 * Following the instructions as directed by 
 * https://mahout.apache.org/users/recommender/recommender-documentation.html
 * http://kickstarthadoop.blogspot.com/2011/05/generating-recommendations-with-mahout_26.html
 */

package com.IDS594.CollaborativeFilteringAlgorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemBasedRecommender {
	public final static int max_limit = 5;
	
	public static void main(String[] args) {
		String[] projectMatrix = new String[30000];
		//Read all the required files
		String evaluatedFile = "output/ContentBasedEvaluation_Rating.txt";
		String projectData = "output/ProjectNames_For_CF.txt";
		int pid = 0;
		try{
			BufferedReader projectNames = new BufferedReader(new FileReader(projectData));
			String projectContent = projectNames.readLine();
			while(projectContent!=null){
				projectMatrix[pid] = projectContent;
				pid++;
				projectContent = projectNames.readLine();
			}
			//Define the data model
			DataModel dataModel = new FileDataModel(new File(evaluatedFile));
			//PearsonCorrelation Similarity
			ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(dataModel);
			//Use Recommender here
			GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
			String result = "output/CF_ItemBasedRecommender_Result.txt";
			BufferedWriter res = new BufferedWriter(new FileWriter(result));
			for (LongPrimitiveIterator itemsIds = dataModel.getItemIDs(); itemsIds.hasNext();){
				long item = itemsIds.nextLong();
				if ((int)item > pid){
					break;
				}
				/*if(item > 60){
					System.out.println("Blah");
				}*/
				//List<RecommendedItem> recommendations = recommender.recommend(item, max_limit);
				List<RecommendedItem> recommendations = recommender.mostSimilarItems(item, max_limit);
				if (recommendations.isEmpty()){
					continue;
				}
				String content = "";
				
				for (RecommendedItem recommendation: recommendations){
					if ((int)recommendation.getItemID() > pid){
						continue;
					}
					content = content + "\nFreelancers who can do " + projectMatrix[(int)item-1] + " can also be recommended : ";
					content = content + "\"" + projectMatrix[(int)recommendation.getItemID()-1] + "\", ";
				}
				res.write(content);
			}
			res.close();
			projectNames.close();
			System.out.println("--Execution Done--");
		}
		catch(Exception e){
			System.out.println("Exception in Main "+e);
		}
	}
	
}
