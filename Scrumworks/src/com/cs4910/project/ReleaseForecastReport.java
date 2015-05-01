package com.cs4910.project;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import com.danube.scrumworks.api2.client.*;

public class ReleaseForecastReport {
<<<<<<< HEAD
	// protected APISoapClient client;
	protected ScrumWorksAPIService service;

	public Date endDate;

	public Date startDate;

	public int totalCompletedPoints;
	private int remainingPoints; 

	public double avgSprintVel;
=======
	protected ScrumWorksAPIService service;

	public List<Date> endDate;

	public List<Date> startDate;

	public int totalStoryPoints;

	public int avgSprintVel;
>>>>>>> origin/master

	public Date averageReleaseDate;

	public Date earlyReleaseDate;

	public Date lateReleaseDate;

<<<<<<< HEAD
	public ArrayList<Sprint> prevSprints;
	public ArrayList<Sprint> futureSprints; 
	public ArrayList<Integer> pointsPerPrevSprint;
	public ArrayList<Integer> pointsPerFutureSprint; 

	private Product product;
	private Release release;
	private double stdDeviation; 
	private int variance;
	private long sprintDuration; 
=======
	public ArrayList<Sprint> sprints;
	public ArrayList<Integer> pointsPerSprint;
	public ArrayList<String> earlyDates;
	public ArrayList<String> lateDates;

	private Product product;
	private Release release;
	private int variance;
>>>>>>> origin/master

	public ReleaseForecastReport(ScrumWorksAPIService srvc, Product prod,
			Release rel) throws ScrumWorksException {
		service = srvc;
		product = prod;
		release = rel;
<<<<<<< HEAD
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
=======
		try {
			sprints = new ArrayList<Sprint>(
					service.getSprintsForProduct(product.getId()));
		} catch (ScrumWorksException e) {
			e.printStackTrace();
		}
		pointsPerSprint = new ArrayList<Integer>();

		/**
		 * Just ignore this. System.out.println(sprints.size());
		 * totalStoryPoints = 0; for (Sprint sp : sprints){
		 * System.out.println("******"); System.out.println(sp.getStartDate() +
		 * " => " + sp.getEndDate()); List<BacklogItem> blitems =
		 * service.getBacklogItemsForSprint(sp.getId(), false); int
		 * totalStoryPointsInSprint = 0 ; for (BacklogItem bli : blitems ){ int
		 * tempPts = bli.getEstimate(); totalStoryPointsInSprint += tempPts;
		 * totalStoryPoints += tempPts; } System.out.println("Total points: " +
		 * totalStoryPointsInSprint);
		 * pointsPerSprint.add(totalStoryPointsInSprint); }
		 **/
		endDate = new ArrayList<Date>();
		startDate = new ArrayList<Date>();
		// getData();
	}

	public void getData() {
		try {
			List<Product> products = service.getProducts();
			List<Long> pIds = new ArrayList<Long>();
			List<Sprint> sprList = new ArrayList<Sprint>();
			int i = 0;
			for (Product p : products) {
				pIds.add(p.getId());

				// i++;
			}
			sprList = service.getSprintsForProduct(products.get(1).getId());
			sprints = (ArrayList<Sprint>) sprList;
			// System.out.println(sprints.get(0).getStartDate());
			for (i = 0; i < sprints.size(); i++) {
				// System.out.println(i);
				// Each item is in same order of sprints.
				startDate.add(sprints.get(i).getStartDate());
				endDate.add(sprints.get(i).getEndDate());
				// System.out.println("Start: " + startDate.get(i) + "\nEnd: " +
				// endDate.get(i));
			}

		} catch (ScrumWorksException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
>>>>>>> origin/master
		}
		
		
		startDate = prevSprints.get(0).getStartDate();
		calculateAverageVelocity(); 
		calculateStdDeviation(); 
		calculateAverageReleaseDate(); 
		calculateEarlyReleaseDate(); 
		calculateLateReleaseDate(); 
	}

<<<<<<< HEAD
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

	/**
	 * public void getData() { try { List<Product> products =
	 * service.getProducts(); List<Long> pIds = new ArrayList<Long>();
	 * List<Sprint> sprList = new ArrayList<Sprint>(); int i = 0; for (Product p
	 * : products) { pIds.add(p.getId());
	 * //i++; } sprList = service.getSprintsForProduct(products.get(1).getId());
	 * sprints = (ArrayList<Sprint>) sprList;
	 * System.out.println(sprints.get(0).getStartDate()); for (i = 0; i <
	 * sprints.size(); i++) { //System.out.println(i); //Each item is in same
	 * order of sprints. startDate.add(sprints.get(i).getStartDate());
	 * endDate.add(sprints.get(i).getEndDate()); //System.out.println("Start: "
	 * + startDate.get(i) + "\nEnd: " + endDate.get(i)); } } catch
	 * (ScrumWorksException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 **/

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
=======
	public int calculateAvgSprintVel() {
		return (int) (getLowVelocity() / getHighVelocity());
	}

	// return average sprint velocity
	public int getAvgSprintVel() {
		// setup dummy data
		avgSprintVel = 50;
		return avgSprintVel;
	}

	public void calculateStdDev() {
		// TODO implement me
	}

	public int getLowVelocity() {
		// dummy data
		return 15;
	}

	public int getHighVelocity() {
		// dummy data
		return 84;
	}

	public String calculateAverageReleaseDates() {
		String result = "";
		return result;
	}

	public String getAverageReleaseDate() {
		// dummy data
		return "4/22/2015";
	}

	public String getEarlyReleaseDate() {
		// dummy data
		return "3/18/15";
	}

	public String getLateReleaseDate() {
		// dummy data
		return "11/2/15";
	}

	public int getVariance() {
		// dummy data
		variance = 50;
		return variance;
	}

	public void exportToPDF() {

	}
>>>>>>> origin/master

	public void getTotalPoints() {
		// TODO implement me
	}

	public ArrayList<Point> getLateReleaseLine() {
<<<<<<< HEAD
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
=======
		ArrayList<Point> lateReleasePoints = new ArrayList<Point>();
		for (int i = 1; i <= 18; i++) {
			lateReleasePoints.add(new Point(i, 15 * i));
		}
		return lateReleasePoints;
	}

	public ArrayList<Point> getAverageReleaseLine() {
		ArrayList<Point> averageReleasePoints = new ArrayList<Point>();
		for (int i = 1; i <= 9; i++) {
			averageReleasePoints.add(new Point(i, 50 * i));
		}
		return averageReleasePoints;
	}

	public ArrayList<Point> getEarlyReleaseLine() {
		ArrayList<Point> earlyReleasePoints = new ArrayList<Point>();
		for (int i = 1; i <= 5; i++) {
			earlyReleasePoints.add(new Point(i, 84 * i));
		}
		return earlyReleasePoints;
	}

	public ArrayList<Point> getTotalReleaseLine() {
		ArrayList<Point> totalPointsInRel = new ArrayList<Point>();
		totalPointsInRel.add(new Point(1, 230));
		totalPointsInRel.add(new Point(2, 230));
		totalPointsInRel.add(new Point(3, 265));
		totalPointsInRel.add(new Point(4, 285));
		totalPointsInRel.add(new Point(5, 345));
		totalPointsInRel.add(new Point(6, 450));
		totalPointsInRel.add(new Point(7, 450));
		totalPointsInRel.add(new Point(8, 450));
		totalPointsInRel.add(new Point(9, 450));
>>>>>>> origin/master
		return totalPointsInRel;
	}

	public ArrayList<Point> getTargetForecastLine() {
		ArrayList<Point> targetForecastPoints = new ArrayList<Point>();
<<<<<<< HEAD
		for (int i = 0; i < pointsPerPrevSprint.size(); i++ ){ 
			targetForecastPoints.add(new Point(i + 1, pointsPerPrevSprint.get(i))); 
		}
		return targetForecastPoints;
=======
		targetForecastPoints.add(new Point(1, 210));
		targetForecastPoints.add(new Point(2, 230));
		targetForecastPoints.add(new Point(3, 205));
		targetForecastPoints.add(new Point(4, 195));
		targetForecastPoints.add(new Point(5, 200));
		targetForecastPoints.add(new Point(6, 0));
		targetForecastPoints.add(new Point(7, 0));
		targetForecastPoints.add(new Point(8, 0));
		targetForecastPoints.add(new Point(9, 0));
		return targetForecastPoints;

>>>>>>> origin/master
	}

	public String getProductName() {
		return product.getName();
	}

	public String getReleaseName() {
		return release.getName();
	}
<<<<<<< HEAD
	
	public String formatDate(Date d){ 
		String s = d.getMonth() + "/" + d.getDate() + "/" + (d.getYear() + 1900); 
		return s; 
	}
=======
>>>>>>> origin/master

}
