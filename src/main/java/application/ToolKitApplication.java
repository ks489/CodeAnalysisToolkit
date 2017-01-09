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
import metrics.util.LinesOfCode;
import metrics.util.WeightedMethodsPerClass;

import dynamicAnalysis.util.*;

public class ToolKitApplication {
	
	List<File> files;
	//String suffix = "";
    String suffixClass = ".class";
    String suffixJava = ".java";
    //Class root directory for mac
    //String rootDirectoryClass = "/Users/AppleZa-Laptop/Projects/University/co7506/jfreechart-fse/target/classes";
    //Class root directory for windows
    String rootDirectoryClass = "../../jfreechart-fse/target/classes";
    
    //Java root directory for mac
    //String rootDirectoryJava = "/Users/AppleZa-Laptop/Projects/University/co7506/jfreechart-fse/src/main/java";
    //Java root directory for windows
    String rootDirectoryJava = "../../jfreechart-fse/src/main/java";
    
    //Refactored directories
    //Java root directory for mac
    //String rootDirectoryJava = "/Users/AppleZa-Laptop/Projects/University/co7506/jfreechart-fse/src/main/java";
    //Java root directory for windows
    String rootDirectoryClassRefactor = "../../jfreechart-fse/target/classes/org/jfree/chart/charttypes";
    String rootDirectoryJavaRefactor = "../../jfreechart-fse/src/main/java/org/jfree/chart/charttypes";
    
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
    
    private void PerformWeightedMethodsPerClassAnalysis(List<File> filesClass){
    	try {
	    	int counter = 0;
			System.out.println("Calculating Weighted Methods PerClass");
			Map<String, Integer> weightedMap = new HashMap<String, Integer>();
			Map<String, Integer> averageWeightedMap = new HashMap<String, Integer>();
			//Get all weighted methods per class
			for (File file : filesClass) {
				Map<String, Integer> ccMap;
				Map<String, Integer> ccMapAverage;
				
				ccMap = CyclomaticComplexity.GetAllCyclomaticComplexityPerClass(file.getPath());
				ccMapAverage = CyclomaticComplexity.GetAllCyclomaticComplexityPerClass(file.getPath());
				
				weightedMap.put(file.getName(), WeightedMethodsPerClass.GetWeightedMethodPerClass(ccMap));
				averageWeightedMap.put(file.getName(), WeightedMethodsPerClass.GetWeightedMethodPerClassAverage(ccMapAverage));
				
				counter++;
				System.out.println(counter + " - Computed Weight Value For Class " + file.getName());
				
			}
	
			//WMPC is weighted methods per class
			System.out.println("Writing out WMPC to CSV file");
			String[] headers = new String[] { "Class","WMPC"};
			
			CSVConverter.convertMapToCSV(headers, weightedMap, "WeightedMethodsPerClass", this.outputDirectory);
			//CSVConverter.convertMapToCSV(headers, averageWeightedMap, "WeightedMethodsPerClassAverage", this.outputDirectory);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void PerformDuplicateFileAnalysis(){
    	try {
	    	System.out.println("Performing Duplicate File Analysis");
			File rootJava = new File(this.rootDirectoryJava);
			DuplicateDetector dd = new DuplicateDetector(rootJava, this.suffixJava);
	        double[][] comparisonMatrix = dd.fileComparison(true);
	        System.out.println("Writing out CSV file for Duplicate Files");
       
			TablePrinter.printRelations(comparisonMatrix,new File(this.outputDirectory +"fileComparisons.csv"),dd.getFiles());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void PerformTotalLinesOfCodeAnalysis(List<File> filesJava){
    	try {
	    	int counter = 0;
			System.out.println("Calculating Lines Of Code Analysis");
			Map<String, Integer> locMap = new HashMap<String, Integer>();
			//Get all weighted methods per class
			for (File file : filesJava) {

				int val = LinesOfCode.GetTotalLinesOfCode(file);
				
				locMap.put(file.getName(), val);
				counter++;
				System.out.println(counter + " - Computed Total Lines of Code For Class " + file.getName());
				
			}
	
			//WMPC is weighted methods per class
			System.out.println("Writing out Lines of Code to CSV file");
			String[] headers = new String[] { "Class","LOC"};
			
			CSVConverter.convertMapToCSV(headers, locMap, "LinesOfCode", this.outputDirectory);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void RefactoredAnalysis(ToolKitApplication app){
    	//rootDirectoryJavaRefactor
    	System.out.println("Starting Refactored Analysis");
    	File rootClass = new File(app.rootDirectoryClassRefactor);
    	List<File> filesClass = app.GetAllClasses(rootClass, app.suffixClass);
    	//app.PerformWeightedMethodsPerClassAnalysis(filesClass);	
    	System.out.println("Done with Weighted Per Class Analysis on Refactored Code");
    	
    	
    	File rootJava = new File(app.rootDirectoryJavaRefactor);
		List<File> filesJava = app.GetAllClasses(rootJava, app.suffixJava);
		//app.PerformTotalLinesOfCodeAnalysis(filesJava);
		System.out.println("Done with Total Lines of Code Analysis on Refactored Code");
		DynamicAnalysisTracer.RunDynamicAnalysis();
		System.out.println("Done with Dynamic Analysis on Refactored Code");
    	System.out.println("Refactored Analysis complete");
    }
	
	public static void main(String[] args) throws IOException{
		//Run Reengineering Analysis (Static & Dynamic)
		System.out.println("Analysis Started");
		ToolKitApplication app = new ToolKitApplication();
		
		//Get all classes
		//File rootClass = new File(app.rootDirectoryClass);
		//List<File> filesClass = app.GetAllClasses(rootClass, app.suffixClass);
		
		//app.PerformWeightedMethodsPerClassAnalysis(filesClass);		
		//app.PerformDuplicateFileAnalysis();
		
		//File rootJava = new File(app.rootDirectoryJava);
		//List<File> filesJava = app.GetAllClasses(rootJava, app.suffixJava);
		//app.PerformTotalLinesOfCodeAnalysis(filesJava);
		//GetTotalLinesOfCode
		
		//testComparisonMatrix

        String pathToSourceDirectory; // e.g. "/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main"
        String pathToFileA = ""; //e.g. "/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main/java/org/apache/commons/compress/archivers/zip/X0015_CertificateIdForFile.java"
        String pathToFileB = "";//e.g. ""/Users/neilwalkinshaw/Documents/Research/Software/SubjectSystems/commons-compress/commons-compress/src/main/java/org/apache/commons/compress/archivers/zip/X0016_CertificateIdForCentralDirectory.java"


        //testDetailedComparisonMatrix
        //File from = new File(pathToFileA);
        //File to = new File(pathToFileB);
        //FileComparator fc = new FileComparator(from,to);
        //boolean[][] comparisonMatrix1 = fc.detailedCompare();
        //TablePrinter.printRelations(comparisonMatrix1,new File("detailedComparisons.csv"));

        //Run dynamic analysis
        //DynamicAnalysisTracer.RunDynamicAnalysis();
		app.RefactoredAnalysis(app);
		System.out.println("Analysis Ended");
	}
	
	
}
