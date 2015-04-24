package com.cs4910.project;

import com.danube.scrumworks.api2.client.ScrumWorksAPIService;



public class ExportTool
{
	
	private String filename;
	
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
		
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
	public String getFilename()
	{
		return this.filename;
	}
}