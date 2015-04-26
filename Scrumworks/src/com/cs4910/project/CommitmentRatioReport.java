package com.cs4910.project;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.danube.scrumworks.api2.client.*;

public class CommitmentRatioReport {

	protected ScrumWorksAPIService service;
	private Product product; 
	private Release release; 
	public List<Sprint> sprints;

	public CommitmentRatioReport(ScrumWorksAPIService svc, Product prod, Release rel) {
		super();
		service = svc; 
		product = prod; 
		release = rel; 
		
		//get the sprints associated to the given product
		try {
			sprints = new ArrayList<Sprint>(service.getSprintsForProduct(product.getId()));
		} catch (ScrumWorksException e) {
			e.printStackTrace();
		} 
	}

	public int calculateCommitmentRatio() {
		// TODO implement me
		return 49;
	}

	public int getLowRatio() {
		// TODO implement me
		return 24;
	}

	public int getHighRatio() {
		// TODO implement me
		return 76;
	}
	
	public String getProjectName() { 
		return product.getName(); 
	}
	
	public String getReleaseName() { 
		return release.getName(); 
	}

	public ArrayList<Point> getCommitedPointsLine() {
		ArrayList<Point> committedLinePoints = new ArrayList<Point>();
		committedLinePoints.add(new Point(1, 42));
		committedLinePoints.add(new Point(2, 61));
		committedLinePoints.add(new Point(3, 98));
		committedLinePoints.add(new Point(4, 138));
		committedLinePoints.add(new Point(5, 118));
		return committedLinePoints; 
	}

	public ArrayList<Point> getCompletedPointsLine() {
		ArrayList<Point> completedLinePoints = new ArrayList<Point>(); 
		completedLinePoints.add(new Point(1, 18));
		completedLinePoints.add(new Point(2, 15));
		completedLinePoints.add(new Point(3, 42));
		completedLinePoints.add(new Point(4, 79));
		completedLinePoints.add(new Point(5, 84));
		return completedLinePoints; 
	}

	public void exportToPDF() {
		// TODO implement me
	}

}