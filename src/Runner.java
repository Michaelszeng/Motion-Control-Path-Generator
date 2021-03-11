import java.util.ArrayList;

public class Runner {
	
	public static void main(String[] args) {
		ArrayList<Pose> path = generatePath();
		Robot rob = new Robot(new Pose(0.0, 0.0, 0.0, false), 36, path);
		rob.followPath();
	}
	
	public static ArrayList<Pose> generatePath() {
		return null;
	}
	
}
