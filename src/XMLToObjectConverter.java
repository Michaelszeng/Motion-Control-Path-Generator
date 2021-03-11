import java.io.File;
import java.io.FileNotFoundException; 
  
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;  

public class XMLToObjectConverter {
	Path path;
	
	public XMLToObjectConverter(String filename) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Path.class);
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		     
		    //We had written this file in marshalling example
		    Path path = (Path) jaxbUnmarshaller.unmarshal(new File("src\\" + filename));
		    this.path = path;
//		    printPath();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Path getPath() {
		return path;
	}
	
	public void printPath() {
		for (Pose p : path.getPoints()) {
			System.out.println(p.getX() + ", " + p.getY() + ", " + p.getIsVertex());
		}
	}
}
