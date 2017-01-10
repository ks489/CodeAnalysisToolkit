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
    String suffixClass = ".class";
    String suffixJava = ".java";    
    //Class root directories
    String rootDirectoryClass = "../../jfreechart-fse/target/classes";    
    //Java root directories
    String rootDirectoryJava = "../../jfreechart-fse/src/main/java";    
    //Refactored directories
    String rootDirectoryClassRefactor = "../../jfreechart-fse/target/classes/org/jfree/chart/charttypes";
    String rootDirectoryJavaRefactor = "../../jfreechart-fse/src/main/java/org/jfree/chart/charttypes";
    
    String outputDirectory = "./outputfiles/";
	
    /**
     * Gets all class files within a directory
     * @param The root file
     * @param suffix of the file to be retrieved
     * @return a list of files within the directory
     */
	public List<File> GetAllClasses(File root, String suffix){
		files = new ArrayList<File>();
        populateFiles(root, suffix);
        return files;
	}
	
	/**
     * Populates a list of files
     * @param The root file
     * @param suffix of the file to be retrieved
     */
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
    
    /**
     * Performs a Weighted Methods Per Class Analysis. It will do a normal MPCA and an average MPCA
     * @param List of files
     */
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
				
				//Cyclomatic Complexity
				ccMap = CyclomaticComplexity.GetAllCyclomaticComplexityPerClass(file.getPath());
				ccMapAverage = CyclomaticComplexity.GetAllCyclomaticComplexityPerClass(file.getPath());
				
				//Populate the maps with the cc per class
				weightedMap.put(file.getName(), WeightedMethodsPerClass.GetWeightedMethodPerClass(ccMap));
				averageWeightedMap.put(file.getName(), WeightedMethodsPerClass.GetWeightedMethodPerClassAverage(ccMapAverage));
				
				counter++;
				System.out.println(counter + " - Computed Weight Value For Class " + file.getName());				
			}
			//WMPC is weighted methods per class
			System.out.println("Writing out WMPC to CSV file");
			String[] headers = new String[] { "Class","WMPC"};
			
			//Prints out the Weighted Methods Per Class
			CSVConverter.convertMapToCSV(headers, weightedMap, "WeightedMethodsPerClass", this.outputDirectory);
			//Prints out the Weighted Methods Per Class Average
			CSVConverter.convertMapToCSV(headers, averageWeightedMap, "WeightedMethodsPerClassAverage", this.outputDirectory);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * This will perform a duplicate file analysis
     */
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
    
    /**
     * This will get all the lines of code for a list of files
     * @param A list of files
     */
    private void PerformTotalLinesOfCodeAnalysis(List<File> filesJava){
    	try {
	    	int counter = 0;
			System.out.println("Calculating Lines Of Code Analysis");
			Map<String, Integer> locMap = new HashMap<String, Integer>();
			//Get all weighted methods per class
			for (File file : filesJava) {
				//Gets the lines of code for a file
				int val = LinesOfCode.GetTotalLinesOfCode(file);				
				locMap.put(file.getName(), val);
				counter++;
				System.out.println(counter + " - Computed Total Lines of Code For Class " + file.getName());				
			}
			System.out.println("Writing out Lines of Code to CSV file");
			String[] headers = new String[] { "Class","LOC"};
			
			CSVConverter.convertMapToCSV(headers, locMap, "LinesOfCode", this.outputDirectory);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * this performs all analysis for the refactored part of the system
     * @param ToolKitApplication
     */
    private void RefactoredAnalysis(ToolKitApplication app){
    	//rootDirectoryJavaRefactor
    	System.out.println("Starting Refactored Analysis");
    	File rootClass = new File(app.rootDirectoryClassRefactor);
    	List<File> filesClass = app.GetAllClasses(rootClass, app.suffixClass);
    	app.PerformWeightedMethodsPerClassAnalysis(filesClass);	
    	System.out.println("Done with Weighted Per Class Analysis on Refactored Code");
    	
    	
    	File rootJava = new File(app.rootDirectoryJavaRefactor);
		List<File> filesJava = app.GetAllClasses(rootJava, app.suffixJava);
		app.PerformTotalLinesOfCodeAnalysis(filesJava);
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
		File rootClass = new File(app.rootDirectoryClass);
		List<File> filesClass = app.GetAllClasses(rootClass, app.suffixClass);
		
		app.PerformWeightedMethodsPerClassAnalysis(filesClass);		
		app.PerformDuplicateFileAnalysis();
		
		//Gets total lines of code analysis
		File rootJava = new File(app.rootDirectoryJava);
		List<File> filesJava = app.GetAllClasses(rootJava, app.suffixJava);
		app.PerformTotalLinesOfCodeAnalysis(filesJava);

        //Run dynamic analysis
        DynamicAnalysisTracer.RunDynamicAnalysis();
        
        //Refactored Analysis
		app.RefactoredAnalysis(app);
		
		System.out.println("Analysis Ended");
	}
	
	
}
