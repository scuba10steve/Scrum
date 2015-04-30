package com.cs4910.project;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import org.apache.pdfbox.exceptions.COSVisitorException;

import java.util.*;

public class RFRGUI extends JPanel {
	private ReleaseForecastReport rfr ; 
	private JPanel graphPanel; // panel for displaying graph
	private int velocity;
	private int velocityLow;
	private int velocityHigh;
	private int variance;
	private String earlyRelease;
	private String lateRelease;
	private String projectName;
	private String releaseName;
	private String targetReleaseDate;
	final Font big = new Font("Arial", Font.BOLD, 16);
	final Font norm = new Font("Arial", Font.PLAIN, 12);
	final Font notAsBig = new Font("Arial", Font.BOLD, 13);
	final Border mainBorderType = new MatteBorder(2, 0, 0, 0, Color.BLACK);

	public RFRGUI(ReleaseForecastReport report) throws COSVisitorException {
		super(new GridLayout(2, 1, 0, 0));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		rfr = report; 
		targetReleaseDate = rfr.getAverageReleaseDate();
		variance = rfr.getVariance();
		earlyRelease = rfr.getEarlyReleaseDate();
		lateRelease = rfr.getLateReleaseDate();
		projectName = rfr.getProductName();
		releaseName = rfr.getReleaseName();
		velocity = rfr.getAvgSprintVel();
		velocityLow = rfr.getLowVelocity();
		velocityHigh = rfr.getHighVelocity();

		// The panel for the top half (other than graph)
		JPanel infoPanel = new InfoPanel();
		add(infoPanel);

		//The panel which includes the graph
		try {
			graphPanel = new DrawGraph();
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}

		add(graphPanel);
		try
		{
			ExportTool et = new ExportTool();
			et.getDataFromPanel(this);
			et.exportToPDF();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// subclass for the Information Panel
	public class InfoPanel extends JPanel {
		public InfoPanel() {
			super(new GridLayout(3, 1, 10, 10));

			// The panel for the basic information
			JPanel basicInfoPanel = new JPanel(new GridLayout(1, 1));
			basicInfoPanel.setBorder(new TitledBorder(mainBorderType,
					"RELEASE FORECAST REPORT", 0, 0, big));
			JPanel inBasicInfoPanel = new JPanel(new GridLayout(2, 1, 10, 10));
			inBasicInfoPanel.setBorder(new EmptyBorder(5, 10, 15, 10));
			inBasicInfoPanel.add(new JLabel("Project Name: " + projectName));
			inBasicInfoPanel.add(new JLabel("Release #/Name: " + releaseName));
			basicInfoPanel.add(inBasicInfoPanel);
			add(basicInfoPanel);

			// The panel for Scheduling Information
			JPanel schedPanel = new JPanel(new GridLayout(1, 1));
			schedPanel.setBorder(new TitledBorder(mainBorderType,
					"SCHEDULE FORECAST", 0, 0, notAsBig));
			JPanel inSchedPanel = new JPanel(new GridLayout(4, 1));
			inSchedPanel.setBorder(new EmptyBorder(0, 10, 4, 10));
			JLabel lbl1 = new JLabel(
					"Based on the team's velocity and standard deviation history:");
			lbl1.setFont(norm);
			JLabel lbl2 = new JLabel("Target Release Date is: "
					+ targetReleaseDate + " with " + variance + "% variance.");
			lbl2.setFont(norm);
			JLabel lbl3 = new JLabel("Potential Early Release: " + earlyRelease);
			lbl3.setFont(norm);
			JLabel lbl4 = new JLabel("Potential Late Release: " + lateRelease);
			lbl4.setFont(norm);
			inSchedPanel.add(lbl1);
			inSchedPanel.add(lbl2);
			inSchedPanel.add(lbl3);
			inSchedPanel.add(lbl4);
			schedPanel.add(inSchedPanel);
			add(schedPanel);

			// The panel for the velocity info
			JPanel velocityPanel = new JPanel(new GridLayout(1, 2));
			velocityPanel.setBorder(new TitledBorder(mainBorderType,
					"SCHEDULE FORECAST", 0, 0, notAsBig));
			JPanel inVelocityPanel = new JPanel(new GridLayout(3, 2));
			inVelocityPanel.setBorder(new EmptyBorder(0, 10, 4, 20));
			JLabel lblv1 = new JLabel("Overall velocity: ");
			lblv1.setFont(norm);
			JLabel lblv2 = new JLabel("Historical Low Range velocity: ");
			lblv2.setFont(norm);
			JLabel lblv3 = new JLabel("Historical High Range velocity: ");
			lblv3.setFont(norm);
			inVelocityPanel.add(lblv1);
			inVelocityPanel.add(new JLabel(velocity + " points per sprint"));
			inVelocityPanel.add(lblv2);
			inVelocityPanel.add(new JLabel(velocityLow + " points"));
			inVelocityPanel.add(lblv3);
			inVelocityPanel.add(new JLabel(velocityHigh + " points"));

			velocityPanel.add(inVelocityPanel);
			JPanel spacePanel = new JPanel();
			spacePanel.setBorder(new EmptyBorder(550, 200, 0, 0));
			velocityPanel.add(spacePanel);
			add(velocityPanel);
		}
	}

	// subclass for the Graph Panel
	public class DrawGraph extends JPanel {

		ArrayList<Point> targetForecastPoints;
		ArrayList<Point> totalPointsInRel;
		ArrayList<Point> earlyReleasePoints;
		ArrayList<Point> averageReleasePoints;
		ArrayList<Point> lateReleasePoints;

		Color TARGET_FORECAST = Color.RED;
		Color TOT_PTS_IN_REL = Color.BLUE;
		Color EARLY_REL = Color.GREEN.darker();
		Color AVG_REL = Color.ORANGE.darker();
		Color LATE_REL = Color.MAGENTA;
		int maxX;
		int maxY;
		int HORIZ_OFFSET = 60;
		int VERT_OFFSET = 60;

		// Basic Constructor
		public DrawGraph() throws Exception {
			setBorder(new TitledBorder(mainBorderType, "RELEASE FORECAST", 0,
					0, notAsBig));
			targetForecastPoints = rfr.getTargetForecastLine();
			totalPointsInRel = rfr.getTotalReleaseLine(); 
			earlyReleasePoints = rfr.getEarlyReleaseLine(); 
			averageReleasePoints = rfr.getAverageReleaseLine(); 
			lateReleasePoints = rfr.getLateReleaseLine(); 

			maxY = 100;
			maxX = targetForecastPoints.size();
			maxX = Math.max(maxX, Math.max(totalPointsInRel.size(), Math.max(
					earlyReleasePoints.size(),
					Math.max(lateReleasePoints.size(),
							averageReleasePoints.size()))));

			for (Point p : targetForecastPoints) {
				maxY = (int) Math.max(maxY, p.getY());
			}
			for (Point p : totalPointsInRel) {
				maxY = (int) Math.max(maxY, p.getY());
			}
			for (Point p : earlyReleasePoints) {
				maxY = (int) Math.max(maxY, p.getY());
			}
			for (Point p : averageReleasePoints) {
				maxY = (int) Math.max(maxY, p.getY());
			}
			for (Point p : lateReleasePoints) {
				maxY = (int) Math.max(maxY, p.getY());
			}
		}

		@Override
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);

			int vertStep = roundTo1SigDigit(maxY / 10);
			maxY = vertStep * 10;

			// Draw Horizontal Lines

			for (int i = 1; i <= maxX; i++) {
				g.setColor(Color.GRAY);
				g.drawLine(getJavaX(i), VERT_OFFSET, getJavaX(i), getHeight()
						- VERT_OFFSET);
				g.setColor(Color.BLACK);
				g.drawString("" + i, getJavaX(i) - 5, getHeight() - VERT_OFFSET
						+ 18);
			}

			// Draw Vertical Grid Lines
			double vertOffset = (getHeight() - 2 * VERT_OFFSET) / 10.0;
			for (int i = vertStep; i <= maxY; i += vertStep) {
				g.setColor(Color.GRAY);
				g.drawLine(HORIZ_OFFSET, getJavaY(i),
						getWidth() - HORIZ_OFFSET, getJavaY(i));
				g.setColor(Color.BLACK);
				g.drawString("" + i, HORIZ_OFFSET - 30, getJavaY(i) + 7);
			}

			// Draw Key
			g.setFont(notAsBig);
			g.setColor(TOT_PTS_IN_REL);
			g.drawString("Total Pts.", 80, 35);
			g.setColor(TARGET_FORECAST);
			g.drawString("Target Forecast", 200, 35);
			g.setColor(EARLY_REL);
			g.drawString("Early Release Forecast", 330, 35);
			g.setColor(AVG_REL);
			g.drawString("Average Release Forecast", 80, 55);
			g.setColor(LATE_REL);
			g.drawString("Late Release Forecast", 290, 55);
			// Draw Outline
			// Draw Grid
			g.setColor(Color.BLACK);

			g.drawLine(HORIZ_OFFSET, VERT_OFFSET, HORIZ_OFFSET, getHeight()
					- VERT_OFFSET);
			g.drawLine(HORIZ_OFFSET + 1, VERT_OFFSET, HORIZ_OFFSET + 1,
					getHeight() - VERT_OFFSET);

			g.drawLine(HORIZ_OFFSET, getHeight() - VERT_OFFSET, getWidth()
					- HORIZ_OFFSET, getHeight() - VERT_OFFSET);
			g.drawLine(HORIZ_OFFSET, getHeight() - VERT_OFFSET + 1, getWidth()
					- HORIZ_OFFSET, getHeight() - VERT_OFFSET + 1);

			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			g2.setColor(TARGET_FORECAST);

			// Draw target Forecast Line
			for (int i = 0; i < targetForecastPoints.size() - 1; i++) {
				Point p1 = targetForecastPoints.get(i);
				Point p2 = targetForecastPoints.get(i + 1);
				g2.drawLine(getJavaX((int) p1.getX()),
						getJavaY((int) p1.getY()), getJavaX((int) p2.getX()),
						getJavaY((int) p2.getY()));
			}

			// Draw Total Points Line
			g2.setColor(TOT_PTS_IN_REL);
			for (int i = 0; i < totalPointsInRel.size() - 1; i++) {
				Point p1 = totalPointsInRel.get(i);
				Point p2 = totalPointsInRel.get(i + 1);
				g2.drawLine(getJavaX((int) p1.getX()),
						getJavaY((int) p1.getY()), getJavaX((int) p2.getX()),
						getJavaY((int) p2.getY()));
			}

			// Draw Early Release Line
			g2.setColor(EARLY_REL);
			for (int i = 0; i < earlyReleasePoints.size() - 1; i++) {
				Point p1 = earlyReleasePoints.get(i);
				Point p2 = earlyReleasePoints.get(i + 1);
				g2.drawLine(getJavaX((int) p1.getX()),
						getJavaY((int) p1.getY()), getJavaX((int) p2.getX()),
						getJavaY((int) p2.getY()));
			}

			// Draw Average Release Line
			g2.setColor(AVG_REL);
			for (int i = 0; i < averageReleasePoints.size() - 1; i++) {
				Point p1 = averageReleasePoints.get(i);
				Point p2 = averageReleasePoints.get(i + 1);
				g2.drawLine(getJavaX((int) p1.getX()),
						getJavaY((int) p1.getY()), getJavaX((int) p2.getX()),
						getJavaY((int) p2.getY()));
			}

			// Draw Early Release Line
			g2.setColor(LATE_REL);
			for (int i = 0; i < lateReleasePoints.size() - 1; i++) {
				Point p1 = lateReleasePoints.get(i);
				Point p2 = lateReleasePoints.get(i + 1);
				g2.drawLine(getJavaX((int) p1.getX()),
						getJavaY((int) p1.getY()), getJavaX((int) p2.getX()),
						getJavaY((int) p2.getY()));
			}

		}

		int roundTo1SigDigit(int num) {
			int largest = (int) Math.log10(num);
			int divisor = (int) Math.ceil((double) num / Math.pow(10, largest));

			return divisor * (int) Math.pow(10, largest);
		}

		int getJavaX(int x) {
			int offset = (getWidth() - 2 * HORIZ_OFFSET) / (maxX + 1);
			return HORIZ_OFFSET + x * offset;
		}

		int getJavaY(int y) {
			y = maxY - y;
			int verticalPixels = getHeight() - 2 * VERT_OFFSET;
			double scaleY = (double) verticalPixels / maxY;
			return VERT_OFFSET + (int) (scaleY * y);
		}
	}
}
