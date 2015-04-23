package com.cs4910.project;

import java.util.List;

import com.danube.scrumworks.api2.client.ScrumWorksAPIService;


/**
*<!--end-user-doc-->
 * @generated
 */

public class Main
{
	APISoapClient client;
	ScrumWorksAPIService service;
	
	public String name;
	
	public ReleaseForecastReport releaseReport;
	
	public ExportTool exportTool;
	
	//public UserInterface userInterface;
	
	public CommitmentRatioReport ratioReport;
	
	public Main()
	{
		super();
		load();
		System.out.println("Loaded...");
	}

	
	public void load() 
	{
		// TODO implement me	
		client = new APISoapClient();
		service = client.getAPIservice();
		releaseReport = new ReleaseForecastReport();
		
	}
	
	public static void main(String[] args) 
	{
		Main me = new Main();
	}
	
}

