package com.cs4910.project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

public class CommRatioGUI extends JPanel {
	private CommitmentRatioReport crr; 
	private JPanel graphPanel; // panel for displaying graph
	private int totalRatioCompleted;
	private int lowRatioCompleted;
	private int highRatioCompleted;
	private String projectName;
	private String releaseName;
	final Font big = new Font("Arial", Font.BOLD, 16);
	final Font norm = new Font("Arial", Font.PLAIN, 12);
	final Font notAsBig = new Font("Arial", Font.BOLD, 13);
	final Border mainBorderType = new MatteBorder(2, 0, 0, 0, Color.BLACK);

	public CommRatioGUI(CommitmentRatioReport commratio) {
		super(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		crr = commratio; 
		totalRatioCompleted = crr.calculateCommitmentRatio();
		lowRatioCompleted = crr.getLowRatio();
		highRatioCompleted = crr.getHighRatio();
		projectName = crr.getProjectName(); 
		releaseName = crr.getReleaseName(); 

		// The panel for the top half (other than graph)
		JPanel infoPanel = new InfoPanel();
		add(infoPanel, BorderLayout.NORTH);

		// The panel which is used to display the graph!
		try {
			graphPanel = new DrawGraph();
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
		}

		add(graphPanel);
	}

	// subclass for the Information Panel
	public class InfoPanel extends JPanel {
		public InfoPanel() {
			super(new GridLayout(2, 1, 10, 10));

			// The panel for the basic information
			JPanel basicInfoPanel = new JPanel(new GridLayout(1, 1));
			basicInfoPanel.setBorder(new TitledBorder(mainBorderType,
					"COMMITMENT RATIO REPORT", 0, 0, big));
			JPanel inBasicInfoPanel = new JPanel(new GridLayout(2, 1, 10, 10));
			inBasicInfoPanel.setBorder(new EmptyBorder(5, 10, 15, 10));
			inBasicInfoPanel.add(new JLabel("Project Name: " + projectName));
			inBasicInfoPanel.add(new JLabel("Release #/Name: " + releaseName));
			basicInfoPanel.add(inBasicInfoPanel);
			add(basicInfoPanel);

			// The panel for the commitment numerical info
			JPanel velocityPanel = new JPanel(new BorderLayout());
			velocityPanel.setBorder(new TitledBorder(mainBorderType,
					"COMMITMENT RATIO", 0, 0, notAsBig));
			JPanel inVelocityPanel = new JPanel(new GridLayout(4, 4));
			inVelocityPanel.setBorder(new EmptyBorder(0, 10, 4, 40));

			JLabel lblv1 = new JLabel("Average Ratio Completed: ");
			lblv1.setFont(norm);
			JLabel lblv2 = new JLabel("Low Ratio Completed: ");
			lblv2.setFont(norm);
			JLabel lblv3 = new JLabel("High Ratio Completed: ");
			lblv3.setFont(norm);
			inVelocityPanel.add(lblv1);
			inVelocityPanel.add(new JLabel(totalRatioCompleted
					+ "% of the points committed to"));
			inVelocityPanel.add(lblv2);
			inVelocityPanel.add(new JLabel(lowRatioCompleted + "%"));
			inVelocityPanel.add(lblv3);
			inVelocityPanel.add(new JLabel(highRatioCompleted + "%"));
			JLabel lbl = new JLabel(
					"Compares story points completed with the points committed to");
			lbl.setFont(norm);

			JPanel yetAnotherPanel = new JPanel(new GridLayout(1, 2));
			yetAnotherPanel.add(inVelocityPanel);
			JPanel spacePanel = new JPanel();
			yetAnotherPanel.add(spacePanel);
			velocityPanel.add(lbl, BorderLayout.NORTH);
			velocityPanel.add(yetAnotherPanel);

			add(velocityPanel);
		}
	}

	// subclass for the Graph Panel
	public class DrawGraph extends JPanel {

		ArrayList<Point> completedLinePoints;
		ArrayList<Point> committedLinePoints;

		Color COMMITTED = Color.RED;
		Color COMPLETED = Color.BLUE;

		int maxX;
		int maxY;
		int HORIZ_OFFSET = 60;
		int VERT_OFFSET = 60;

		// Basic Constructor
		public DrawGraph() throws Exception {
			setBorder(new TitledBorder(mainBorderType, "RELEASE FORECAST", 0,
					0, notAsBig));
			completedLinePoints = crr.getCompletedPointsLine();
			committedLinePoints = crr.getCommitedPointsLine(); 

			// end adding dummy data!!!!
			maxY = 50;
			maxX = committedLinePoints.size();

			for (Point p : committedLinePoints) {
				maxY = (int) Math.max(maxY, p.getY());
			}
			for (Point p : completedLinePoints) {
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
			g.setColor(COMMITTED);
			g.drawString("Story Points Committed", 80, 35);
			g.setColor(COMPLETED);
			g.drawString("Story Points Completed", 280, 35);

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
			g2.setColor(COMPLETED);

			// Draw target Forecast Line
			for (int i = 0; i < completedLinePoints.size() - 1; i++) {
				Point p1 = completedLinePoints.get(i);
				Point p2 = completedLinePoints.get(i + 1);
				g2.drawLine(getJavaX((int) p1.getX()),
						getJavaY((int) p1.getY()), getJavaX((int) p2.getX()),
						getJavaY((int) p2.getY()));
			}

			// Draw Total Points Line
			g2.setColor(COMMITTED);
			for (int i = 0; i < committedLinePoints.size() - 1; i++) {
				Point p1 = committedLinePoints.get(i);
				Point p2 = committedLinePoints.get(i + 1);
				g2.drawLine(getJavaX((int) p1.getX()),
						getJavaY((int) p1.getY()), getJavaX((int) p2.getX()),
						getJavaY((int) p2.getY()));
			}

		}

		int roundTo1SigDigit(int num) {
			int largest = (int) Math.log10(num);
			int divisor = (int) Math.ceil((double) num
					/ Math.pow(10, largest));

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
