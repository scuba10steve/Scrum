package com.cs4910.project;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import com.danube.scrumworks.api2.client.*;

public class ReleaseForecastReport {
	// protected APISoapClient client;
	protected ScrumWorksAPIService service;

	public Date endDate;

	public Date startDate;

	public int totalCompletedPoints;
	private int remainingPoints; 

	public double avgSprintVel;

	public Date averageReleaseDate;

	public Date earlyReleaseDate;

	public Date lateReleaseDate;

	public ArrayList<Sprint> prevSprints;
	public ArrayList<Sprint> futureSprints; 
	public ArrayList<Integer> pointsPerPrevSprint;
	public ArrayList<Integer> pointsPerFutureSprint; 

	private Product product;
	private Release release;
	private double stdDeviation; 
	private int variance;
	private long sprintDuration; 

	public ReleaseForecastReport(ScrumWorksAPIService srvc, Product prod,
			Release rel) throws ScrumWorksException {
		service = srvc;
		product = prod;
		release = rel;
		ArrayList<Sprint> allSprints; 
		try {
			allSprints = new ArrayList<Sprint>(
					service.getSprintsForProduct(product.getId()));
		} catch (ScrumWorksException e) {
			e.printStackTrace();
			return; 
		}
		if (allSprints.size() == 0){
			System.out.println("No sprints"); 
			return; 
		}
		Sprint s1 = allSprints.get(0); 
		sprintDuration = s1.getEndDate().getTime() - s1.getStartDate().getTime();
		prevSprints = new ArrayList<Sprint>(); 
		futureSprints = new ArrayList<Sprint>(); 
		for(Sprint sp: allSprints){
			//if the end date has already passed, it's a previous sprint
			if (sp.getEndDate().getTime() < (new Date()).getTime()){
				prevSprints.add(sp); 
			}
			else { 
				futureSprints.add(sp); 
			}
		}
		pointsPerPrevSprint = new ArrayList<Integer>();
		totalCompletedPoints = 0 ; 
		for (Sprint sp : prevSprints) {
			System.out.println("Past******");
			System.out.println(sp.getStartDate() + " => " + sp.getEndDate());
			List<BacklogItem> blItems = service.getBacklogItemsForSprint(
					sp.getId(), true);
			int totalStoryPointsInSprint = 0;
			for (BacklogItem bli : blItems) {
				totalStoryPointsInSprint += bli.getEstimate();
			}
			System.out.println("Total points: " + totalStoryPointsInSprint);
			pointsPerPrevSprint.add(totalStoryPointsInSprint);
			totalCompletedPoints += totalStoryPointsInSprint; 
		}
		
		remainingPoints = 0; 
		for (Sprint sp: futureSprints){
			System.out.println("Future******"); 
			System.out.println(sp.getStartDate() + " => " + sp.getEndDate());
			List<BacklogItem> blItems = service.getBacklogItemsForSprint(
					sp.getId(), true);
			int totalStoryPointsInSprint = 0;
			for (BacklogItem bli : blItems) {
				totalStoryPointsInSprint += bli.getEstimate();
			}
			System.out.println("Total points: " + totalStoryPointsInSprint);
			pointsPerFutureSprint.add(totalStoryPointsInSprint);
			remainingPoints += totalStoryPointsInSprint; 
		}
		
		
		startDate = prevSprints.get(0).getStartDate();
		calculateAverageVelocity(); 
		calculateStdDeviation(); 
		calculateAverageReleaseDate(); 
		calculateEarlyReleaseDate(); 
		calculateLateReleaseDate(); 
	}

	public void calculateAverageVelocity() {
		int sum = 0; 
		for (Integer points: pointsPerPrevSprint){
			sum += points; 
		}
		avgSprintVel = ((double)sum)/pointsPerPrevSprint.size();   
	}
	
	public void calculateStdDeviation() { 
		double diff = 0; 
		for (Integer points: pointsPerPrevSprint){
			diff += Math.pow(points - avgSprintVel, 2); 
		}
		stdDeviation = Math.sqrt(diff / pointsPerPrevSprint.size()); 
	}

	// return average sprint velocity
	public int getAvgSprintVel() {
		return (int)Math.round(avgSprintVel);
	}

	public int getLowVelocity() {
		return (int)Math.round(avgSprintVel - stdDeviation); 
	}

	public int getHighVelocity() {
		return (int)Math.round(avgSprintVel + stdDeviation); 
	}

	public void calculateAverageReleaseDate() {
		try { 
			if (remainingPoints == 0){
				endDate = prevSprints.get(prevSprints.size() - 1).getEndDate(); 
			}
			else { 
				int sprintsLeft = (int)Math.round(remainingPoints/avgSprintVel); 
				endDate = new Date(); 
				endDate.setTime(prevSprints.get(prevSprints.size() - 1).getEndDate().getTime() + sprintsLeft * sprintDuration);
			}
		}
		catch (Exception ex){
			System.out.println("ERROR: " + ex.getMessage()); 
		}
	}

	public String getAverageReleaseDate() {
		return formatDate(endDate); 
	}

	public void calculateEarlyReleaseDate() { 
		try { 
			if (remainingPoints == 0){
				earlyReleaseDate = prevSprints.get(prevSprints.size() - 1).getEndDate(); 
			}
			else { 
				int sprintsLeft = (int)Math.ceil(remainingPoints/(avgSprintVel + stdDeviation)); 
				earlyReleaseDate = new Date(); 
				earlyReleaseDate.setTime(prevSprints.get(prevSprints.size() - 1).getEndDate().getTime() + sprintsLeft * sprintDuration);
			}
		}
		catch (Exception ex){
			System.out.println("ERROR: " + ex.getMessage()); 
		}
	}
	
	public String getEarlyReleaseDate() {
		return formatDate(earlyReleaseDate);
	}
	
	public void calculateLateReleaseDate() { 
		try { 
			if (remainingPoints == 0){
				lateReleaseDate = prevSprints.get(prevSprints.size() - 1).getEndDate(); 
			}
			else { 
				int sprintsLeft = (int)Math.ceil(remainingPoints/(avgSprintVel - stdDeviation)); 
				lateReleaseDate = new Date(); 
				lateReleaseDate.setTime(prevSprints.get(prevSprints.size() - 1).getEndDate().getTime() + sprintsLeft * sprintDuration);
			}
		}
		catch (Exception ex){
			System.out.println("ERROR: " + ex.getMessage()); 
		}
	}
	
	public String getLateReleaseDate() {
		return formatDate(lateReleaseDate); 
	}

	public int getVariance() {
		return (int)(Math.round((stdDeviation/avgSprintVel)*100)); 
	}

	/**
	 * public void exportToPDF() { ExportTool et = new ExportTool();
	 * 
	 * }
	 **/

	public void getTotalPoints() {
		// TODO implement me
	}

	public ArrayList<Point> getLateReleaseLine() {
		if (remainingPoints != 0){
			ArrayList<Point> list = new ArrayList<Point>(); 
			int sprintsLeft = (int)Math.ceil(remainingPoints/(avgSprintVel - stdDeviation));
			int sprintNum = prevSprints.size() + 1; 
			double pointsPerSprint = avgSprintVel - stdDeviation; 
			for ( int i = 1 ; i <= sprintsLeft ; i++ ) { 
				list.add(new Point(sprintNum++, (int)Math.round(totalCompletedPoints + pointsPerSprint*i))); 
			}
			return list; 
		}
		else {
			return new ArrayList<Point>(); 
		}
	}

	public ArrayList<Point> getAverageReleaseLine() {
		if (remainingPoints != 0){
			ArrayList<Point> list = new ArrayList<Point>(); 
			int sprintsLeft = (int)Math.ceil(remainingPoints/avgSprintVel);
			int sprintNum = prevSprints.size() + 1; 
			double pointsPerSprint = avgSprintVel; 
			for ( int i = 1 ; i <= sprintsLeft ; i++ ) { 
				list.add(new Point(sprintNum++, (int)Math.round(totalCompletedPoints + pointsPerSprint*i))); 
			}
			return list; 
		}
		else {
			return new ArrayList<Point>(); 
		}
	}

	public ArrayList<Point> getEarlyReleaseLine() {
		if (remainingPoints != 0){
			ArrayList<Point> list = new ArrayList<Point>(); 
			int sprintsLeft = (int)Math.ceil(remainingPoints/(avgSprintVel + stdDeviation));
			int sprintNum = prevSprints.size() + 1; 
			double pointsPerSprint = avgSprintVel + stdDeviation; 
			for ( int i = 1 ; i <= sprintsLeft ; i++ ) { 
				list.add(new Point(sprintNum++, (int)Math.round(totalCompletedPoints + pointsPerSprint*i))); 
			}
			return list; 
		}
		else {
			return new ArrayList<Point>(); 
		}
	}

	public ArrayList<Point> getTotalReleaseLine() {
		int totalPoints = 0 ; 
		ArrayList<Point> totalPointsInRel = new ArrayList<Point>(); 
		for (int i = 0; i < pointsPerPrevSprint.size(); ){
			totalPoints += pointsPerPrevSprint.get(i); 
			totalPointsInRel.add(new Point(++i, totalPoints)); 
		}
		return totalPointsInRel;
	}

	public ArrayList<Point> getTargetForecastLine() {
		ArrayList<Point> targetForecastPoints = new ArrayList<Point>();
		for (int i = 0; i < pointsPerPrevSprint.size(); i++ ){ 
			targetForecastPoints.add(new Point(i + 1, pointsPerPrevSprint.get(i))); 
		}
		return targetForecastPoints;
	}

	public String getProductName() {
		return product.getName();
	}

	public String getReleaseName() {
		return release.getName();
	}
	
	public String formatDate(Date d){ 
		String s = d.getMonth() + "/" + d.getDate() + "/" + (d.getYear() + 1900); 
		return s; 
	}

}
