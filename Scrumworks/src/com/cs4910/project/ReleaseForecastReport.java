package com.cs4910.project;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.danube.scrumworks.api2.client.*;

public class ReleaseForecastReport
{
	//protected APISoapClient client;
	protected ScrumWorksAPIService service;
	
	//May need to change to an array, for use of multiple sprints
	public List<Date> endDate;
	//Same as above
	public List<Date> startDate;
	
	public int totalStoryPoints;
	
	public int avgSprintVel;
	
	public Date averageReleaseDate;
	
	public Date earlyReleaseDate;
	
	public Date lateReleaseDate;
	
	public ArrayList<Sprint> sprints;
	public ArrayList<Integer> pointsPerSprint; 
	
	private Product product; 
	private Release release; 
	private int variance; 
	
	public ReleaseForecastReport(ScrumWorksAPIService srvc, Product prod, Release rel) throws ScrumWorksException
	{
		service = srvc; 
		product = prod; 
		release = rel; 
		try {
			sprints = new ArrayList<Sprint>(service.getSprintsForProduct(product.getId()));
		} catch (ScrumWorksException e) {
			e.printStackTrace();
		} 
		pointsPerSprint = new ArrayList<Integer>(); 
		
		/** Just ignore this.
		System.out.println(sprints.size()); 
		totalStoryPoints = 0; 
		for (Sprint sp : sprints){
			System.out.println("******"); 
			System.out.println(sp.getStartDate() + " => " + sp.getEndDate()); 
			List<BacklogItem> blitems = service.getBacklogItemsForSprint(sp.getId(), false); 
			int totalStoryPointsInSprint = 0 ; 
			for (BacklogItem bli : blitems ){
				int tempPts = bli.getEstimate(); 
				totalStoryPointsInSprint += tempPts;
				totalStoryPoints += tempPts; 
			}
			System.out.println("Total points: " + totalStoryPointsInSprint); 
			pointsPerSprint.add(totalStoryPointsInSprint); 
		}**/
		endDate = new ArrayList<Date>();
		startDate = new ArrayList<Date>();
		//getData();
	}
	
	public void getData()
	{
		try 
		{
			List<Product> products = service.getProducts();
			List<Long> pIds = new ArrayList<Long>();
			List<Sprint> sprList = new ArrayList<Sprint>();
			int i = 0;
			for (Product p : products)
			{
				pIds.add(p.getId());
				
				//i++;
			}
			sprList = service.getSprintsForProduct(products.get(1).getId());
			sprints = (ArrayList<Sprint>) sprList;
			System.out.println(sprints.get(0).getStartDate());
			for (i = 0; i < sprints.size(); i++)
			{
				//System.out.println(i);
				//Each item is in same order of sprints.
				startDate.add(sprints.get(i).getStartDate());
				endDate.add(sprints.get(i).getEndDate());
				//System.out.println("Start: " + startDate.get(i) + "\nEnd: " + endDate.get(i));
			}
		}
		catch (ScrumWorksException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void calculateAvgSprintVel() 
	{
		// TODO implement me	
	}
	
	//return average sprint velocity
	public int getAvgSprintVel() { 
		//setup dummy data
		avgSprintVel = 50; 
		return avgSprintVel; 
	}
	
	public void calculateStdDev() 
	{
		// TODO implement me	
	}
	
	public int getLowVelocity() {
		//dummy data
		return 15; 
	}
	
	public int getHighVelocity() { 
		//dummy data
		return 84; 
	}
	
	public void calculateAverageReleaseDates()
	{
		// TODO implement me	
	}
	
	public String getAverageReleaseDate() { 
		//dummy data
		return "4/22/2015"; 
	}
	
	public String getEarlyReleaseDate() { 
		//dummy data
		return "3/18/15"; 
	}
	
	public String getLateReleaseDate() { 
		//dummy data
		return "11/2/15"; 
	}
	
	public int getVariance() { 
		//dummy data
		variance = 50 ; 
		return variance; 
	}
	
	public void exportToPDF() 
	{
		// TODO implement me	
	}
	
	public void getTotalPoints() 
	{
		// TODO implement me	
	}
	
	public ArrayList<Point> getLateReleaseLine() 
	{
		ArrayList<Point> lateReleasePoints = new ArrayList<Point>();
		for (int i = 1; i <= 18; i++) {
			lateReleasePoints.add(new Point(i, 15 * i));
		}
		return lateReleasePoints; 
	}
	
	public ArrayList<Point> getAverageReleaseLine() 
	{
		ArrayList<Point> averageReleasePoints = new ArrayList<Point>(); 
		for (int i = 1; i <= 9; i++) {
			averageReleasePoints.add(new Point(i, 50 * i));
		}
		return averageReleasePoints; 
	}
	
	public ArrayList<Point> getEarlyReleaseLine() 
	{
		ArrayList<Point> earlyReleasePoints = new ArrayList<Point>(); 
		for (int i = 1; i <= 5; i++) {
			earlyReleasePoints.add(new Point(i, 84 * i));
		}	
		return earlyReleasePoints; 
	}
	
	public ArrayList<Point> getTotalReleaseLine() 
	{
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
		return totalPointsInRel; 
	}
	
	public ArrayList<Point> getTargetForecastLine() 
	{
		ArrayList<Point> targetForecastPoints = new ArrayList<Point>(); 
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
	
	}
	
	public String getProductName() { 
		return product.getName(); 
	}
	
	public String getReleaseName() { 
		return release.getName(); 
	}
	
}

