import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javax.imageio.ImageIO;
import java.util.ArrayList;

public class PathGenerator extends JPanel implements MouseListener, ActionListener, KeyListener {
	Timer timer;
	
	private static boolean curved = false;
	public static boolean destinationOrControlAlternator = true;
	
	public static double inchPerPx;
	public static int fieldWidth = 288;
	public static int fieldHeight = 288;
	public static int frameWidth = 700;
	public static int frameHeight = 700;
	
	public static ArrayList<Integer> xVertices = new ArrayList<>();
	public static ArrayList<Integer> yVertices = new ArrayList<>();
	public static ArrayList<Integer> hVertices = new ArrayList<>();
	
	public static ArrayList<Integer> xPath = new ArrayList<>();
	public static ArrayList<Integer> yPath = new ArrayList<>();
	public static ArrayList<Integer> headings = new ArrayList<>();
	
	public static ArrayList<Boolean> isVertex = new ArrayList<>();
	public static int currentCoordIndex = 0;
	public static BufferedImage fieldImg;
	public static double resolution = 1;	//constant resolution, smaller the better. 1 is the min. value
	
	static boolean ctrlPressed = false;
	static boolean mPressed = true;
	static boolean shiftPressed = false;
	static boolean altPressed = false;
	static int ctrlPressedX = 350;
	static int ctrlPressedY = 350;
	static int ctrlPressedHeading = 0;
	static int ctrlPressedVertexIndex;
	static boolean ctrlSelected = false;
	
	public PathGenerator(ArrayList<Integer> xVertices, ArrayList<Integer> yVertices, ArrayList<Integer> headings) {
		timer = new Timer(1000 / 60, this);
		this.xVertices = xVertices;
		this.yVertices = yVertices;
		this.headings = headings;
		
		try {
            fieldImg = ImageIO.read(this.getClass().getResourceAsStream("grid.jpg"));
		} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}
		
		inchPerPx = fieldWidth/frameWidth;
		double inchPerPxHeights = fieldHeight/frameHeight;
		if (inchPerPx != inchPerPxHeights) {
			System.out.println("Warning: inchPerPx for width and height not consistent.");
		}
		
		this.setFocusable(true);
	    this.requestFocus();
	    this.addKeyListener(this);
	}
	
	public void start() {
	    timer.start();
	}
	
	public static void plotNewPoint(int x, int y, int heading, int r, Graphics g) {
		xPath.add(x);
		yPath.add(y);
		headings.add(heading);
		plotPoint(x, y, r, g);
		
		if (r > 2) {
			isVertex.add(true);
		}
		else {
			isVertex.add(false);
		}
	}
	
	public static void plotPoint(int x, int y, int r, Graphics g) {
		g.setColor(new Color(255, 197, 36));
		int radius = r;
		int diameter = 2 * radius;
		g.fillOval(x - radius, y - radius, diameter, diameter); 
	}
	
	public static void drawLine(int x1, int y1, int x2, int y2, boolean isNew, int firstVertexIndex, Graphics g) {
		int distance = (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
		int vectorXTotal = x2-x1;
		int vectorYTotal = y2-y1;
		int vectorHTotal = hVertices.get(firstVertexIndex + 1) - hVertices.get(firstVertexIndex);
		if (vectorXTotal == 0) {
			vectorXTotal = 1;
		}
		
		double vectorX;
		double vectorY;
		double vectorH;
		double poseVectorRatio;
		poseVectorRatio = Math.sqrt((Math.pow(vectorXTotal, 2.0) + Math.pow(vectorYTotal, 2.0)) / Math.pow(resolution, 2.0));
		vectorX = vectorXTotal / poseVectorRatio;
		vectorY = vectorYTotal / poseVectorRatio;
		vectorH = vectorHTotal / poseVectorRatio;
//		System.out.println("<" + vectorX + ", " + vectorY + ">");
		
		double currentPointX = x1;
		double currentPointY = y1;
		double currentPointH = hVertices.get(firstVertexIndex);
		double nextPointX;
		double nextPointY;
		double nextPointH;
		int destinationHeading = hVertices.get(firstVertexIndex + 1);
		int loopCycles = (int) ((int) distance/resolution);
		for (int i=0; i<loopCycles; i++) {	//Loops through from x1, y1 to x2, y2 and sequentially draws points
			nextPointX = currentPointX + vectorX;
			nextPointY = currentPointY + vectorY;
			nextPointH = currentPointH + vectorH;
			currentPointX = nextPointX;
			currentPointY = nextPointY;
			currentPointH = nextPointH;
			
			if (isNew) {
				plotNewPoint((int) nextPointX, (int) nextPointY, (int) nextPointH, 1, g);	//Getting the heading of the previous vertex
			}
			else {
				plotPoint((int) nextPointX, (int) nextPointY, 1, g);
			}
		}
		
		//draw last point in line
		if (isNew) {
			plotNewPoint(x2, y2, destinationHeading, 3, g);
		}
		else {
			plotPoint(x2, y2, 2, g);
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(fieldImg, 0, 0, frameWidth, frameHeight, null);
		if (!curved) {
			if (xVertices.size() > 0) {
				plotNewPoint(xVertices.get(0), yVertices.get(0), hVertices.get(0), 3, g);		//start point in path
			}
			for (int i=1; i<xVertices.size(); i++) {
				if (i >= 1) {
					drawLine(xVertices.get(i-1), yVertices.get(i-1), xVertices.get(i), yVertices.get(i), true, i-1, g);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		for (int i=0; i<xPath.size(); i++) {
    		System.out.print(xPath.get(i) + "," + yPath.get(i) + "; ");
    	}
    	System.out.println();
			
		// TODO Auto-generated method stub
		if (!ctrlPressed && !altPressed && !shiftPressed) {
			xVertices.add(arg0.getX()-7);
			yVertices.add(arg0.getY()-30);
			hVertices.add(0);
			
			System.out.print("xVertices");
	    	for (int x: xVertices) {
	    		System.out.print(x + " ");
	    	}
	    	System.out.println();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mPressed = true;
		if (ctrlPressed == true || shiftPressed == true || altPressed == true) {
			int mouseX = arg0.getX()-7;		//add constants because there is a weird (7, 30) offset
			int mouseY = arg0.getY()-30;
			
			ArrayList<Integer> coordIndexesInRange = new ArrayList<>();
			for (int i=0; i<xVertices.size(); i++) {	//Go through all vertices
				if ((Math.abs(mouseX - xVertices.get(i)) < 10) && (Math.abs(mouseY - yVertices.get(i)) < 10)) {	//ensure only vertices in radius 20 of curser can be moved
					coordIndexesInRange.add(i);
//					System.out.println("in range: " + i);
				}
			}
			
			//PURPOSE OF LOOP: FIND THE MOST LIKELY POSE, AND ITS INDEX IN THE VERTICES LISTS, THAT WAS SHIFT CLICKED
			if (coordIndexesInRange.size() > 0) {
				ctrlSelected = true;
				
				double smallestDistance = 99999.9;
				int smallestDistanceIndex = 0;
				int smallestX = 0;	//will be changed no matter what
				int smallestY = 0;	//will be changed no matter what
				double distance;
				for (int index : coordIndexesInRange) {		//check all points in range, then pick the closes one to the mousepress
					int vertexX = xVertices.get(index);
					int vertexY = yVertices.get(index);
					distance = Math.sqrt(Math.pow(mouseX - vertexX, 2) + Math.pow(mouseY - vertexY, 2));
					if (distance < smallestDistance) {
						smallestDistance = distance;
						smallestDistanceIndex = index;
						smallestX = vertexX;
						smallestY = vertexY;
					}
				}
				//set global variables, so in ActionPerformed() it knows what points to move
				ctrlPressedX = smallestX;
				ctrlPressedY = smallestY;
				ctrlPressedHeading = hVertices.get(smallestDistanceIndex);
				ctrlPressedVertexIndex = smallestDistanceIndex;		//Set nindex for use in replotting the shift clicked point
						
				if (shiftPressed == true && ctrlPressed == false && altPressed == false) {		//if it is a shift click, need to additionally make textbox pop up
//					System.out.println("hVertices.get()" + hVertices.get(index));
					
					
					//Text field stuff to get the new Pose Values
					JTextField xField = new JTextField(ctrlPressedX + "");
			        JTextField yField = new JTextField(ctrlPressedY + "");
			        JTextField hField = new JTextField(ctrlPressedHeading + "");
			        xField.setPreferredSize( new Dimension( 40, 24 ) );
			        yField.setPreferredSize( new Dimension( 40, 24 ) );
			        hField.setPreferredSize( new Dimension( 40, 24 ) );

			        JPanel myPanel = new JPanel();
			        myPanel.add(new JLabel(" x:"));
			        myPanel.add(xField);
			        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			        myPanel.add(new JLabel(" y:"));
			        myPanel.add(yField);
			        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			        myPanel.add(new JLabel(" heading:"));
			        myPanel.add(hField);
			        

			        int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter New X and Y Coordinates", JOptionPane.OK_CANCEL_OPTION);
			        if (result == JOptionPane.OK_OPTION) {
			        	try {
			        		System.out.println("parseint: " + Integer.parseInt(hField.getText()));
			        		replotPoint(ctrlPressedVertexIndex, false, Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()), Integer.parseInt(hField.getText()));
			        	}
			        	catch (Exception e) {
			        		e.printStackTrace();
			        		JOptionPane.showMessageDialog(null, "Integer Coordinates Required. Action Canceled");
			        	}
			        }
				}
				else if (altPressed == true && ctrlPressed == false && shiftPressed == false) {
					if (JOptionPane.showConfirmDialog(null, "Delete this vertex?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					    // yes option
						xVertices.remove(ctrlPressedVertexIndex);
						yVertices.remove(ctrlPressedVertexIndex);
					}
				}
			}
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mPressed = false;
		ctrlSelected = false;	//Once mouse is released after "ctrl moving" a point, deactive the control select on that point
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
//		System.out.println(e.getKeyCode());
		
		if (e.getKeyCode() == 17) {
			ctrlPressed = true;
		}
		if (e.getKeyCode() == 16) {
			shiftPressed = true;
		}
		if (e.getKeyCode() == 18) {
			altPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == 17) {
			ctrlPressed = false;
		}
		if (e.getKeyCode() == 16) {
			shiftPressed = false;
		}
		if (e.getKeyCode() == 18) {
			altPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
		//Clear all coordinate lists, will be remade every update
		for (int i=0; i<xPath.size(); i++) {
			xPath.remove(0);
			yPath.remove(0);
			headings.remove(0);
			isVertex.remove(0);
		}
		try {
			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			Point mousePoint = pointerInfo.getLocation();
			
			Point screenLocation = this.getLocationOnScreen();
			int mouseX = (int) mousePoint.getX() - (int) screenLocation.getX();
			int mouseY = (int) mousePoint.getY() - (int) screenLocation.getY();
//			System.out.println(mouseX + ", " + mouseY);
			
			if (ctrlSelected && ctrlPressed && mPressed && !shiftPressed) {		//Ensure a point is moved only if ctrl is held and the point is in range
				int vectorX = mouseX - ctrlPressedX;
				int vectorY = mouseY - ctrlPressedY;
				if (Math.abs(vectorX) > 1 || Math.abs(vectorY) > 1) {
					replotPoint(ctrlPressedVertexIndex, false, mouseX, mouseY, hVertices.get(ctrlPressedVertexIndex));
				}
			}
		}
		catch(Exception exc) {
//			exc.printStackTrace();
		}
		repaint();
    }
	
	public void replotPoint(int index, boolean headingCtrlPoint, int mouseX, int mouseY, int newHeading) {
		//Change the Pose of a vertex (after shift click)
		if (!headingCtrlPoint) {
			xVertices.set(index, mouseX);
			yVertices.set(index, mouseY);
			hVertices.set(index, newHeading);
		}
		else {
			
		}
	}
 
}
