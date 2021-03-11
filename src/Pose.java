import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;  

@XmlRootElement(name="Pose")  
@XmlAccessorType(XmlAccessType.FIELD)
public class Pose {
	private double x;
	private double y;
	private double heading;
	private boolean isVertex;
	
	public Pose() {}
	
	public Pose(double x, double y, double heading, boolean isVertex) {
		this.x = x;
		this.y = y;
		this.heading = heading;
		this.isVertex = isVertex;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}
	
	public boolean getIsVertex() {
		return isVertex;
	}

	public void setIsVertex(boolean isVertex) {
		this.isVertex = isVertex;
	}
}
