import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  

@XmlRootElement(name="Path")  
@XmlAccessorType(XmlAccessType.FIELD)
public class Path {
	@XmlElement(name="Pose")
	private static ArrayList<Pose> points = new ArrayList<>();
	private int age;
	
	public Path() {}  

	public Path(ArrayList<Pose> points) {
		this.points = points;
	}
	
	public Path(List<Integer> xPath, List<Integer> yPath, List<Boolean> isVertex) {
//		points.add(new Pose(xPath.get(0), yPath.get(0), 0, true));	  //repeating first vertex, for some reason marshaller ignores first Pose
		//delete all points in path first
		points.clear();
		
		System.out.println("Path size before appending: " + points.size());
		for (int i=0; i< xPath.size(); i++) {
			points.add(new Pose(xPath.get(i), yPath.get(i), 0, isVertex.get(i))); 	//for now heading always is 0
		}
	}
	
	public Path(List<Integer> xPath, List<Integer> yPath, List<Integer> headings, List<Boolean> isVertex) {
//		points.add(new Pose(xPath.get(0), yPath.get(0), 0, true));	  //repeating first vertex, for some reason marshaller ignores first Pose
		//delete all points in path first
		points.clear();
		
		System.out.println("Path size before appending: " + points.size());
		for (int i=0; i< xPath.size(); i++) {
			points.add(new Pose(xPath.get(i), yPath.get(i), headings.get(i), isVertex.get(i))); 	//for now heading always is 0
		}
	}
	
//	public String toString() {
//		for (Pose p : points) {
//			System.out.println(p.getX() + ", " + p.getY() + ", " + p.getHeading());
//		}
//		return "";
//	}
	
//	public void printPath() {
//		for (Pose p : points) {
//			System.out.println("(" + p.getX() + ", " + p.getY() + p.getHeading() + ")");
//		}
//	}
	
	public ArrayList<Pose> getPoints() {
		return points;
	}
	
	public static void setPoints(ArrayList<Pose> points) {
		Path.points = points;
	}
}
