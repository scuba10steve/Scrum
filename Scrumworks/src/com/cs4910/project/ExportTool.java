package com.cs4910.project;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

import com.danube.scrumworks.api2.client.ScrumWorksAPIService;




public class ExportTool
{
	
	private String filename;
	private String imageFilename;
	private BufferedImage image;
	
	protected APISoapClient client;
	protected ScrumWorksAPIService service;
	
	JPanel exported;
	
	public ExportTool()
	{
		super();
		client = new APISoapClient();
		service = client.getAPIservice();
		filename = "document.pdf";
		imageFilename = "image.jpg";
	}

	
	public void exportToPDF() throws COSVisitorException
	{
		//wait(250);
		
		try {
			PDDocument document = new PDDocument();
			PDPageContentStream contentStream;
			InputStream in = new FileInputStream(imageFilename);
			PDPage page = new PDPage(PDPage.PAGE_SIZE_LETTER);
			PDJpeg img = new PDJpeg(document, in);
			page.setRotation(90);
			AffineTransform at = new AffineTransform(image.getHeight(), 0, 0, image.getWidth(), 0, 0);
			at.rotate(Math.toRadians(90));
			contentStream = new PDPageContentStream(document, page);
		
			document.addPage(page); 
			
			
			contentStream.drawXObject(img, at);
			contentStream.close();
			document.save(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
	public String getFilename()
	{
		return this.filename;
	}
	public void getDataFromPanel(JPanel panel) throws AWTException
	{
		/*try {
			//wait(250);
		} catch (InterruptedException e1) {
			//  Auto-generated catch block
			e1.printStackTrace();
		}*/
		//panel.setSize((panel.getBounds());
		Robot r = new Robot();
		
		image = r.createScreenCapture(panel.getBounds());
		Graphics2D g = image.createGraphics();
		panel.printAll(g);
		g.dispose();
		try {
		    ImageIO.write(image, "jpg", new File("image.jpg")); 
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}