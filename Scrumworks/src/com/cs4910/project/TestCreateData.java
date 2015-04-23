package com.cs4910.project;

import java.util.ArrayList;

import com.danube.scrumworks.api2.ScrumWorksService;
import com.danube.scrumworks.api2.client.*;


public class TestCreateData
{
	public static BacklogItem bi;
	public static Product p;
	static ArrayList<Product> products;
	static ArrayList<Long> tIDs;
	public static void main(String[] args)
	{
		tIDs = new ArrayList<Long>();
		tIDs.add((long) 101);
		Team t = new Team((long) 101, "sex panthers", "Sprint");
		p = new Product();
		p.setName("Test Product");
		p.setEffortUnits("500");
		p.setId((long) 111);
		p.setTeamIds(tIDs);
		p.setHardWIPLimitEnabled(false);
        try
        {
            ScrumWorksService.getConnection("http://localhost:8080/scrumworks-api/api2/scrumworks?wsdl", "administrator", "password");
            System.out.println(p.getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		System.out.println("it works!");
	}
}
