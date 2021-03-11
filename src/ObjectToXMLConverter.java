import java.io.File;
import java.io.FileNotFoundException; 
  
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;  

public class ObjectToXMLConverter {
	Path path;
	
	public ObjectToXMLConverter(Path p, String filename) {
		path = p;
		
		JAXBContext contextObj;
		try {
			contextObj = JAXBContext.newInstance(Path.class);
		    
		    Marshaller marshaller = contextObj.createMarshaller();
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		    marshaller.marshal(path, new File("src\\" + filename));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
