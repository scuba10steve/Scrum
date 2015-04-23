package com.cs4910.project;

import com.danube.scrumworks.api2.client.ScrumWorksAPIService;



public class ExportTool
{
	
	public String filename;
	
	protected APISoapClient client;
	protected ScrumWorksAPIService service;
	
	public ExportTool()
	{
		super();
		client = new APISoapClient();
		service = client.getAPIservice();
	}

	
	public void exportToPDF() 
	{
		// TODO implement me	
	}
	
}

