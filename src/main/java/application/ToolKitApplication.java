package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import converter.util.CSVConverter;
import fileComparison.DuplicateDetector;
import fileComparison.FileComparator;
import fileComparison.TablePrinter;
import metrics.util.CyclomaticComplexity;
import metrics.util.WeightedMethodsPerClass;

public class ToolKitApplication {
	
	List<File> files;
    String suffix = ".class";
    String rootDirectory = "/Users/AppleZa-Laptop/Projects/University/co7506/jfreechart-fse/target/classes";
    String outputDirectory = "./outputfiles/";
	
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
		
		//Get all classes
		File root = new File(app.rootDirectory);
		List<File> files = app.GetAllClasses(root, app.suffix);
		
		Map<String, Integer> weightedMap = new HashMap<String, Integer>();
		//Get all weighted methods per class
		for (File file : files) {
			//System.out.println(file.getName());
			//System.out.println(file.getPath());
			Map<String, Integer> ccMap = CyclomaticComplexity.GetAllCyclomaticComplexityPerClass(file.getPath());
			weightedMap.put(file.getName(), WeightedMethodsPerClass.GetWeightedMethodPerClass(ccMap));
			//System.out.println("Weighted Value " + WeightedMethodsPerClass.GetWeightedMethodPerClass(ccMap));
		}
		
		//WMPC is weighted methods per class
		String[] headers = new String[] { "Class","WMPC"};
		
		CSVConverter.convertMapToCSV(headers, weightedMap, "WeightedMethodsPerClass", app.outputDirectory);
		
		/*@Test
	    public void testComparisonMatrix() throws IOException {
	        File root = new File(pathToSourceDirectory);
	        DuplicateDetector dd = new DuplicateDetector(root,".java");
	        double[][] comparisonMatrix = dd.fileComparison(true);
	        TablePrinter.printRelations(comparisonMatrix,new File("fileComparisons.csv"),dd.getFiles());
	    }

	    @Test
	    public void testDetailedComparisonMatrix() throws IOException {
	        File from = new File(pathToFileA);
	        File to = new File(pathToFileB);
	        FileComparator fc = new FileComparator(from,to);
	        boolean[][] comparisonMatrix = fc.detailedCompare();
	        TablePrinter.printRelations(comparisonMatrix,new File("detailedComparisons.csv"));
	    }*/
		
		System.out.println("Ended Analysis");
	}
	
	
}
