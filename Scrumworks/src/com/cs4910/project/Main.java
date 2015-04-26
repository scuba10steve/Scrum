package com.cs4910.project;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import com.danube.scrumworks.api2.client.Product;
import com.danube.scrumworks.api2.client.Release;
import com.danube.scrumworks.api2.client.ScrumWorksAPIService;
import com.danube.scrumworks.api2.client.ScrumWorksException;


public class Main extends JApplet {
	APISoapClient client;
	ScrumWorksAPIService service;
	public ReleaseForecastReport releaseReport;
	private List<Product> products ; 
	private List<Release> releases ; 
	public CommitmentRatioReport ratioReport;
	private JButton okBtn;
	private JPanel mainPanel;
	private JComboBox productBox; 
	private JComboBox releaseBox; 
	private JRadioButton releaseForecastRadio; 
	private JRadioButton commRatioRadio; 

	public Main() {
		// load service from ScrumWorks, so we can access ScrumWorks data
		loadService();
		
		//get all products from ScrumWorks, so the user can select them. 
		try {
			products = service.getProducts();
		} catch (ScrumWorksException e) {
			System.out.println(e.getMessage()) ;
			e.printStackTrace();
			return; 
		} 
		if ( products.size() == 0 ) { 
			System.out.println("The system has no products"); 
			return; 
		}
		//get an array of the product names to use for the GUI
		String[] prodStrs = new String[products.size()] ;
		for (int i =  0; i < prodStrs.length ; i++ ) { 
			prodStrs[i] = products.get(i).getName(); 
		}
		
		//get all releases from the initially selected product
		try {
			releases = service.getReleasesForProduct(products.get(0).getId()) ;
		} catch (ScrumWorksException e) {
			System.out.println(e.getMessage()) ;
			e.printStackTrace();
		}
		if (releases.size() == 0 ) { 
			System.out.println("product has no associated releases"); 
			return; 
		}
		//get an array of all release names to use for the GUI
		String[] releaseStrs = new String[releases.size()]; 
		for ( int i = 0 ; i < releases.size(); i++ ) { 
			releaseStrs[i] = releases.get(i).getName(); 
		}
		
		//Start building the GUI/Applet
		mainPanel = new JPanel(new GridLayout(1,1)); 
		JPanel tempPanel = new JPanel(new GridLayout(20,1)); 
		tempPanel = new JPanel(new GridLayout(20, 1));
		tempPanel.add(new JLabel("Select Product:"));
		productBox = new JComboBox(prodStrs);
		productBox.addActionListener(new ProductComboBoxListener()); 
		releaseBox = new JComboBox(releaseStrs);
		tempPanel.add(productBox);
		tempPanel.add(new JLabel("Select Release:"));
		tempPanel.add(releaseBox);
		tempPanel.add(new JLabel("Select a Report Type:")); 
		JPanel radioPanel = new JPanel(new GridLayout(1,2)); 
		releaseForecastRadio = new JRadioButton("Release Forecast",true);
		releaseForecastRadio.addActionListener(new RelForecastRadioListener()); 
		radioPanel.add(releaseForecastRadio); 
		commRatioRadio = new JRadioButton("Commitment Ratio", false); 
		commRatioRadio.addActionListener(new CommRatioRadioListener());
		radioPanel.add(commRatioRadio); 
		tempPanel.add(radioPanel); 
		
		okBtn = new JButton("Continue!");
		okBtn.addActionListener(new OKBtnActionListener());
		tempPanel.add(okBtn);
		tempPanel.setSize(400,620);
		mainPanel.add(tempPanel); 
		add(mainPanel); 
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JApplet applet = new Main();

		applet.setSize(1020, 620);
		frame.add(applet);
		frame.setTitle("Commerce");
		frame.setSize(1050, 650);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void loadService() {
		client = new APISoapClient();
		service = client.getAPIservice();
		// releaseReport = new ReleaseForecastReport();
	}
	
	private void updateReleaseComboBox() { 
		if (releases.size() == 0 )  {
			releaseBox.setModel((new JComboBox()).getModel()); 
			return; 
		}
		String[] releaseStrs = new String[releases.size()]; 
		for ( int i = 0 ; i < releases.size(); i++ ) { 
			releaseStrs[i] = releases.get(i).getName(); 
		}
		releaseBox.setModel((new JComboBox(releaseStrs)).getModel()); 
		
	}

	private class OKBtnActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mainPanel.removeAll(); 
			mainPanel.revalidate(); 
			mainPanel.repaint(); 
			JPanel reportPanel; 
			Product product = products.get(productBox.getSelectedIndex());
			Release release = releases.get(releaseBox.getSelectedIndex()); 
			if (releaseForecastRadio.isSelected()){
				ReleaseForecastReport rfr ; 
				try {
					rfr = new ReleaseForecastReport(service, product, release);
				} catch (ScrumWorksException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				} 
				reportPanel = new RFRGUI(rfr); 
			}
			else { 
				CommitmentRatioReport crr; 
				crr = new CommitmentRatioReport(service, product, release); 
				reportPanel = new CommRatioGUI(crr); 
			}
			mainPanel.add(reportPanel); 
			mainPanel.revalidate();
			mainPanel.repaint();
		}
	}
	
	private class ProductComboBoxListener implements ActionListener { 
		@Override
		public void actionPerformed(ActionEvent e){ 
			int newIndex = productBox.getSelectedIndex(); 
			Product newProd = products.get(newIndex); 
			try {
				releases = service.getReleasesForProduct(newProd.getId());
			} catch (ScrumWorksException e1) {
				System.out.println("ERROR: " + e1.getMessage());
				e1.printStackTrace();
			} 
			updateReleaseComboBox(); 
		}
	}
	
	private class RelForecastRadioListener implements ActionListener { 
		@Override
		public void actionPerformed(ActionEvent e){ 
			if (commRatioRadio.isSelected()){
				commRatioRadio.setSelected(false);
			}
		}
	}
	
	private class CommRatioRadioListener implements ActionListener { 
		@Override
		public void actionPerformed(ActionEvent e){ 
			if (releaseForecastRadio.isSelected()){
				releaseForecastRadio.setSelected(false);
			}
		}
	}

}
