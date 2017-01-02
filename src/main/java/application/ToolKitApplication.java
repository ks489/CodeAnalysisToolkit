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
	//String suffix = "";
    String suffixClass = ".class";
    String suffixJava = ".java";
    //Class root directory
    String rootDirectoryClass = "/Users/AppleZa-Laptop/Projects/University/co7506/jfreechart-fse/target/classes";
    
    //Java root directory
    String rootDirectoryJava = "/Users/AppleZa-Laptop/Projects/University/co7506/jfreechart-fse/src/main/java";
    
    String outputDirectory = "./outputfiles/";
	
	public List<File> GetAllClasses(File root, String suffix){
		files = new ArrayList<File>();
        populateFiles(root, suffix);
        return files;
	}
	
    private void populateFiles(File root, String suffix) {
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(root.listFiles()));
        for(File f: files) {
            if(f.isDirectory())
                populateFiles(f, suffix);
            else if(f.getName().endsWith(suffix)){
                this.files.add(f);
            }
        }
    }
	
	public static void main(String[] args) throws IOException{
		System.out.println("Analysis Started");
		ToolKitApplication app = new ToolKitApplication();
		
		//Get all classes
		File rootClass = new File(app.rootDirectoryClass);
		List<File> filesClass = app.GetAllClasses(rootClass, app.suffixClass);
		
		Map<String, Integer> weightedMap = new HashMap<String, Integer>();
		//Get all weighted methods per class
		for (File file : filesClass) {
			Map<String, Integer> ccMap = CyclomaticComplexity.GetAllCyclomaticComplexityPerClass(file.getPath());
			weightedMap.put(file.getName(), WeightedMethodsPerClass.GetWeightedMethodPerClass(ccMap));
			//System.out.println("Weighted Value " + WeightedMethodsPerClass.GetWeightedMethodPerClass(ccMap));
		}
		
		//WMPC is weighted methods per class
		String[] headers = new String[] { "Class","WMPC"};
		
		CSVConverter.convertMapToCSV(headers, weightedMap, "WeightedMethodsPerClass", app.outputDirectory);
			
		//testComparisonMatrix
		File rootJava = new File(app.rootDirectoryJava);
		DuplicateDetector dd = new DuplicateDetector(rootJava, app.suffixJava);
        double[][] comparisonMatrix = dd.fileComparison(true);
        TablePrinter.printRelations(comparisonMatrix,new File(app.outputDirectory +"fileComparisons.csv"),dd.getFiles());
        

        String pathToSourceDirectory; // e.g. "/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main"
        String pathToFileA = ""; //e.g. "/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main/java/org/apache/commons/compress/archivers/zip/X0015_CertificateIdForFile.java"
        String pathToFileB = "";//e.g. ""/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main/java/org/apache/commons/compress/archivers/zip/X0016_CertificateIdForCentralDirectory.java"


        //testDetailedComparisonMatrix
        //File from = new File(pathToFileA);
        //File to = new File(pathToFileB);
        //FileComparator fc = new FileComparator(from,to);
        //boolean[][] comparisonMatrix1 = fc.detailedCompare();
        //TablePrinter.printRelations(comparisonMatrix1,new File("detailedComparisons.csv"));

		
		System.out.println("Analysis Ended");
	}
	
	
}
