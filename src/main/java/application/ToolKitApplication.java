package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import fileComparison.DuplicateDetector;
import fileComparison.TablePrinter;

public class ToolKitApplication {
	
	List<File> files;
    String suffix = ".java";
    String rootDirectory = "";
	
	public List<File> GetAllClasses(File root, String suffix){
		files = new ArrayList<File>();
        this.suffix = suffix;
        populateFiles(root);
        return files;
	}
	
    private void populateFiles(File root) {
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(root.listFiles()));
        for(File f: files) {
            if(f.isDirectory())
                populateFiles(f);
            else if(f.getName().endsWith(suffix)){
                this.files.add(f);
            }
        }
    }
	
	public static void main(String[] args) throws IOException{
		ToolKitApplication app = new ToolKitApplication();
		File root = new File(app.rootDirectory);
		app.GetAllClasses(root, app.suffix);
		
		//Get all weighted methods per class
		
		//Get lines of code
		
		//Look at code duplication
	}
	
	
}
