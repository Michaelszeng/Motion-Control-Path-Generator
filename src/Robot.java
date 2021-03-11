import java.util.ArrayList;

public class Robot {
	public double x;
	public double y;
	public double heading;
	public double r;
	public ArrayList<Pose> path;
	public Pose prevTargetPose;
	public Pose currTargetPose;
	
	public Robot(Pose startPose, double startR, ArrayList<Pose> path) {
		this.x = startPose.getX();
		this.y = startPose.getY();
		this.heading = startPose.getHeading();
		this.r = startR;
		this.path = path;
		
		//Getting initial prevTargetPose value
		double distance;
		double smallestDistance = 9999;
		for (Pose pathPose : path) {
			distance = Math.sqrt(Math.pow(2, x-pathPose.getX()) + Math.pow(y, y-pathPose.getY()));
			if (distance < smallestDistance) {
				
			}
		}
	}
	
	public void followPath() {
		Pose intersect = getIntersect();
	}
	
	public Pose getIntersect() {
		
		Pose intersect = prevTargetPose;
		
		double intersectScore;		//smaller the better
		double bestIntersectScore = 9999.0;
		for (Pose pathPose : path) {
			//intersectScore = (x-h)^2 + (y-k)^2 - r^2
			intersectScore = ((pathPose.getX() - x) * (pathPose.getX() - x)) + ((pathPose.getY() - y) * (pathPose.getY() - y)) - (r * r);
			if (intersectScore < bestIntersectScore) {
				bestIntersectScore = intersectScore;
				intersect = pathPose;
			}
		}
		
		if (Math.abs(bestIntersectScore) > 2) {		//if the circle is not near any points (aka no intersect)
			intersect = prevTargetPose;
		}
		
		return intersect;
	}
	
	public boolean checkPathEnd(Pose targetPose, ArrayList<Pose> path) {
		Pose pathEnd = path.get(path.size()-1);
		if ((targetPose.getX() - pathEnd.getX() < 0.001) && (targetPose.getY() - pathEnd.getY() < 0.001) && (targetPose.getHeading() - pathEnd.getHeading() < 0.001)) {		//comparing is Poses are the same (but double comparisons are sketchy)
			return true;
		}
		return false;
	}
	
}
