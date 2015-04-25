package com.cs4910.project;

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
	
	public void calculateStdDev() 
	{
		// TODO implement me	
	}
	
	public void calculateAverageReleaseDates()
	{
		// TODO implement me	
	}
	
	public void exportToPDF() 
	{
		// TODO implement me	
	}
	
	public void getTotalPoints() 
	{
		// TODO implement me	
	}
	
	public void getLateReleaseLine() 
	{
		// TODO implement me	
	}
	
	public void getAverageReleaseLine() 
	{
		// TODO implement me	
	}
	
	public void getEarlyReleaseLine() 
	{
		// TODO implement me	
	}
	
	public void getTotalReleaseLine() 
	{
		// TODO implement me	
	}
	
	public void getTargetForecastLine() 
	{
		// TODO implement me	
	}
	
<<<<<<< HEAD
	public String getProductName() { 
		return product.getName(); 
	}
	
	public String getReleaseName() { 
		return release.getName(); 
	}
	
}

=======
}
>>>>>>> 8d78ef1b05dca1398a44d18804d76de9dcc884d2
