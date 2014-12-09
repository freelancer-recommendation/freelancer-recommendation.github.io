package com.IDS594.Evaluation;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class IRStats {
	public final static int max_limit = 5;
	public static void main(String[] args) {
		String evaluatedFile = "output/testRating.txt";
		try{
			//Define the data model
			DataModel dataModel = new FileDataModel(new File(evaluatedFile));
			RandomUtils.useTestSeed();
			RecommenderIRStatsEvaluator irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
			RecommenderBuilder recommenderbuilder = new RecommenderBuilder(){

				@Override
				public Recommender buildRecommender(DataModel arg0)
						throws TasteException {
					UserSimilarity similarity = new LogLikelihoodSimilarity (arg0);
					UserNeighborhood neighborhood = new NearestNUserNeighborhood (max_limit, similarity, arg0);
					return new GenericUserBasedRecommender(arg0, neighborhood, similarity);
				}
			};
			
			DataModelBuilder datamodelbuilder = new DataModelBuilder(){

				@Override
				public DataModel buildDataModel(
						FastByIDMap<PreferenceArray> arg0) {
					// TODO Auto-generated method stub
					return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(arg0));
				}
				
			};
			IRStatistics irStats = irStatsEvaluator.evaluate(recommenderbuilder, datamodelbuilder, dataModel, null, max_limit, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
			System.out.println(" Precision:: " + irStats.getPrecision()+ " Recall:: "+irStats.getRecall());
		}
		catch(Exception e){
			
		}
	}
}
