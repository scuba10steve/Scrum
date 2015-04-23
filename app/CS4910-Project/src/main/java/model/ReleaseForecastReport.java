package com.cs4910.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.danube.scrumworks.api2.client.*;

public class ReleaseForecastReport
{
	APISoapClient client;
	ScrumWorksAPIService service;
	
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
	
	public ReleaseForecastReport()
	{
		super();
		client = new APISoapClient();
		service = client.getAPIservice();
		endDate = new ArrayList<Date>();
		startDate = new ArrayList<Date>();
		getData();
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
				System.out.println(i);
				startDate.add(sprints.get(i).getStartDate());
				endDate.add(sprints.get(i).getEndDate());
				System.out.println("Start: " + startDate.get(i) + "\nEnd: " + endDate.get(i));
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
	
}

