package com.cs4910.project;

import java.util.ArrayList;
import java.util.List;

import com.danube.scrumworks.api2.client.*;


public class CommitmentRatioReport
{
	/**
	 * change to list data type<div>used as placeholder</div>
	 * 
	 * @generated
	 * @ordered
	 */
	protected APISoapClient client;
	protected ScrumWorksAPIService service;
	
	public List<Sprint> sprints;
	
	
	public CommitmentRatioReport()
	{
		super();
		client = new APISoapClient();
		service = client.getAPIservice();
		
		try 
		{
			List<Product> products = service.getProducts();
			List<Long> pIds = new ArrayList<Long>();
			List<Sprint> sprList = new ArrayList<Sprint>();
			//int i = 0;
			for (Product p : products)
			{
				pIds.add(p.getId());
			}
			sprList = service.getSprintsForProduct(products.get(1).getId());
			sprints = (ArrayList<Sprint>) sprList;
		} catch (ScrumWorksException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @generated
	 * @ordered
	 */
	
	public double calculateCommitmentRatio() {
		// TODO implement me
		return 0.0;	
	}
	
	/**
	 * 
	 * @generated
	 * @ordered
	 */
	
	public double getLowRatio() {
		// TODO implement me
		return 0.0;	
	}
	
	/**
	 * 
	 * @generated
	 * @ordered
	 */
	
	public double getHighRatio() {
		// TODO implement me
		return 0.0;	
	}
	
	/**
	 * 
	 * @generated
	 * @ordered
	 */
	
	public void getCommitedPointsLine() {
		// TODO implement me	
	}
	
	/**
	 * 
	 * @generated
	 * @ordered
	 */
	
	public void getCompletedPointsLine() {
		// TODO implement me	
	}
	
	/**
	 * 
	 * @generated
	 * @ordered
	 */
	
	public void exportToPDF() {
		// TODO implement me	
	}
	
}