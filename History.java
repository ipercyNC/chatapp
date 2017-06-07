import java.io.File;

public class History {
	public boolean exists(String path, String username){
		String fil = username + ".txt";
		File f = new File(fil);
		if(f.exists() && !f.isDirectory()) { 
		   path = fil;
		   return true;
		}
		return false;
	}
}
