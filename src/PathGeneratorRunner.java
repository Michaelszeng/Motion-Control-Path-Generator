import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   

public class PathGeneratorRunner {
	final static int width = 700;
	final static int height = 700;
	JFrame window;
	static PathGenerator pg;
	static String pathFile;
	static String pathFileNameRaw = "";
	static String pathVersionStr = "1";
	static int pathVersionInt;
	static ArrayList<Integer> xVerticesIn = new ArrayList<>();
	static ArrayList<Integer> yVerticesIn = new ArrayList<>();
	static ArrayList<Integer> xVertices = new ArrayList<>();
	static ArrayList<Integer> yVertices = new ArrayList<>();
	
	public static void main(String[] args) {
		pathFile = JOptionPane.showInputDialog("Enter filename of Path XML To Import");
		
		xVerticesIn = parseXML("x");
		yVerticesIn = parseXML("y");
		ArrayList<ArrayList<Integer>> pathConvertedUnits = inchToPx(xVerticesIn, yVerticesIn);
		xVertices = pathConvertedUnits.get(0);
		yVertices = pathConvertedUnits.get(1);
		
		System.out.print("x before: ");
    	for (int i : xVertices) {
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.print("y before: ");
		for (int i : yVertices) {
			System.out.print(i + " ");
		}
		System.out.println();
		
		PathGeneratorRunner pgr = new PathGeneratorRunner();
		pgr.setUp();
		pgr.pg.start();
	}
	
	public PathGeneratorRunner() {
		window = new JFrame();
		pg = new PathGenerator(xVertices, yVertices, new ArrayList<Integer>());
	}
	
	void setUp() {
		window.add(pg);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
//		    	for (boolean b : PathGenerator.isVertex) {
//		    		System.out.print(b);
//		    	}
//		    	System.out.println();
//		    	System.out.print("xVertices to be marshalled: ");
//		    	for (int i : PathGenerator.xVertices) {
//		    		System.out.print(i + " ");
//		    	}
//		    	System.out.println();
//		    	System.out.print("yVertices to be marshalled: ");
//		    	for (int i : PathGenerator.yVertices) {
//		    		System.out.print(i + " ");
//		    	}
//		    	System.out.println();
		    	
//		    	System.out.println("xPath size: " + PathGenerator.xPath.size());
//		    	System.out.println("yPath size: " + PathGenerator.yPath.size());
//		    	System.out.println("isVertex size: " + PathGenerator.isVertex.size());
		    	
//		    	for (int i=(int) PathGenerator.xPath.size()/2; i<PathGenerator.xPath.size(); i++) {
//		    		System.out.print(PathGenerator.xPath.get(i) + ", " + PathGenerator.yPath.get(i) + "; ");
//		    	}
		    	
		    	System.out.print("xPath Before buildPath(): ");
		    	for (int x : pg.xPath.subList((int) pg.xPath.size()/2, pg.xPath.size())) {
		    		System.out.print(x + " ");
		    	}
		    	System.out.println();
		    	System.out.print("yPath Before buildPath(): ");
		    	for (int y : pg.yPath.subList((int) pg.yPath.size()/2, pg.yPath.size())) {
		    		System.out.print(y + " ");
		    	}
		    	System.out.println();
		    	System.out.print("xVertices Before buildPath(): ");
		    	for (int x: pg.xVertices) {
		    		System.out.print(x + " ");
		    	}
		    	System.out.println();
		    	System.out.print("hVertices Before buildPath(): ");
		    	for (int h: pg.hVertices) {
		    		System.out.print(h + " ");
		    	}
		    	System.out.println();
		    	System.out.print("isVertex Before buildPath(): ");
		    	for (boolean b : pg.isVertex.subList((int) pg.isVertex.size()/2, pg.isVertex.size())) {
		    		System.out.print(b + " ");
		    	}
		    	System.out.println();
		    	
		    	
//		    	Path pathObj = new Path(pg.xPath.subList((int) pg.xPath.size()/2, pg.xPath.size()), pg.yPath.subList((int) pg.yPath.size()/2, pg.yPath.size()), pg.isVertex.subList((int) pg.isVertex.size()/2, pg.isVertex.size()));
		    	Path pathObj = new Path(pg.xPath.subList((int) pg.xPath.size()/2, pg.xPath.size()), pg.yPath.subList((int) pg.yPath.size()/2, pg.yPath.size()), pg.headings.subList((int) pg.headings.size()/2, pg.headings.size()), pg.isVertex.subList((int) pg.isVertex.size()/2, pg.isVertex.size()));
		    	Path pathObjConvertedUnits = pxToInches(pathObj);
		    	
		    	if (JOptionPane.showConfirmDialog(null, "Save Previous Path In Backup File?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				    // yes option
		    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");  
		    		LocalDateTime now = LocalDateTime.now();  
		    		ObjectToXMLConverter converter1 = new ObjectToXMLConverter(pathObjConvertedUnits, "Path Backups/" + pathFile.substring(0, pathFile.length()-4) + " Backup " + dtf.format(now) + ".xml");
				} else {
				    // no option
				}
		    	
		    	
		    	System.out.print("Path after buildPath(): ");
		    	for (Pose p : pathObjConvertedUnits.getPoints()) {
		    		System.out.print(p.getX() + "," + p.getY() + "; ");
//		    		if (p.getIsVertex()) {
//		    			System.out.print(p.getX() + "," + p.getY() + "; ");
//		    		}
		    	}
		    	System.out.println();
		    	
		    	ObjectToXMLConverter converter1 = new ObjectToXMLConverter(pathObjConvertedUnits, pathFile);
		    	
		        exitProcedure();
		    }
		});
		window.setVisible(true);
		window.addMouseListener(pg);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setPreferredSize(new Dimension(width, height));
		window.pack();
	}
	
	public static ArrayList<Integer> parseXML(String xOrY) {
//		System.out.println("xOrY: " + xOrY);
		XMLToObjectConverter converter2 = new XMLToObjectConverter(pathFile);
		ArrayList<Integer> vertices = new ArrayList<>();
		try {
			ArrayList<Pose> points = converter2.getPath().getPoints();
			for (Pose p : points) {
				if (p.getIsVertex() == true) {
//					System.out.println("Vertices: " + p.getX() + ", " + p.getY());
					if (xOrY.equals("x")) {
						vertices.add((int) p.getX());
					}
					else {
						vertices.add((int) p.getY());
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println("Creating New Path");
		}
//		for (int i : vertices) {
//			System.out.println("vertex: " + i);
//		}
		return vertices;
	}
	
	public Path pxToInches(Path pathPx) {
		ArrayList<Pose> pathConvertedUnits = new ArrayList<>();
		for (Pose p : pathPx.getPoints()) {
    		double convertedX = ((p.getX() * pg.fieldWidth) / 700) - pg.fieldWidth/2;
	    	double convertedY = ((p.getY() * -pg.fieldHeight) / 700) + pg.fieldHeight/2;
    		pathConvertedUnits.add(new Pose(convertedX, convertedY, p.getHeading(), p.getIsVertex()));
    	}
    	Path pathObjConvertedUnits = new Path(pathConvertedUnits);
		return pathObjConvertedUnits;
	}
	
	public static ArrayList<ArrayList<Integer>> inchToPx(ArrayList<Integer> xVerticesIn2, ArrayList<Integer> yVerticesIn2) {
		ArrayList<ArrayList<Integer>> pathConvertedUnits = new ArrayList<>();
		ArrayList<Integer> convertedXVertices = new ArrayList<>();
		for (double d : xVerticesIn2) {
			double convertedX = ((d + pg.fieldWidth/2) * 700) / pg.fieldWidth;
	    	convertedXVertices.add((int) convertedX);
    	}
		ArrayList<Integer> convertedYVertices = new ArrayList<>();
		for (double d : yVerticesIn2) {
			double convertedY = ((pg.fieldHeight/2 - d) * 700) / pg.fieldHeight;
			convertedYVertices.add((int) convertedY);
		}
    	pathConvertedUnits.add(convertedXVertices);
    	pathConvertedUnits.add(convertedYVertices);
		return pathConvertedUnits;
	}
	
//	public void removeOldPath() {
//		try {
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//	        dbf.setValidating(false);
//	        DocumentBuilder db = dbf.newDocumentBuilder();
//	         
//	        Document doc = db.parse(new FileInputStream(new File("src/" + pathFile)));
//	         
//	        // retrieve the element 'link'
//	        Element element = (Element) doc.getElementsByTagName("path").item(0);
//	 
//	        // remove the specific node
//	        element.getParentNode().removeChild(element);
//	         
//	        // Normalize the DOM tree, puts all text nodes in the
//	        // full depth of the sub-tree underneath this node
//	        doc.normalize();
//	         
//	        prettyPrint(doc);
//		}
//		catch(Exception e) {
//			System.out.println("Removing old path failed");
//			e.printStackTrace();
//		}
//	}
//	
//	public static final void prettyPrint(Document xml) throws Exception {
//        Transformer tf = TransformerFactory.newInstance().newTransformer();
//        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//        tf.setOutputProperty(OutputKeys.INDENT, "yes");
//        Writer out = new StringWriter();
//        tf.transform(new DOMSource(xml), new StreamResult(out));
//        System.out.println(out.toString());
//    }
	
	public Path buildPath(List<Integer> xPath, List<Integer> yPath, List<Boolean> isVertex) {
		return new Path(xPath, yPath, isVertex);
	}
	
	public String buildPathString(List<Integer> xPath, List<Integer> yPath) {
		String pathString = "";
		for (int i=0; i< xPath.size(); i++) {
			pathString += (xPath.get(i) + ",");
			pathString += (yPath.get(i) + ",");
		}
		return pathString;
	}
	
	public void exitProcedure() {
	    window.dispose();
	    System.exit(0);
	}
	
}
