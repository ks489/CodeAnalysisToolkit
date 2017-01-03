package dynamicAnalysis.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle Spindler on 30/11/2016.
 */
public class TraceMergeHelper {	
	
	String exeOutFile = "totalTmteTrace.log";
	String callOutFile = "totalTmdcTrace.log";
	String tracerFileDirectory = "./traces/";
	
	List<String> fileNames = new ArrayList<String>();
	
	public void mergeTraceFilesExecutions(String directory){
		List<String> exeFiles = getTraceExecutionTraceFiles();		
		mergeTraceFiles(exeFiles, directory,  exeOutFile);
	}
	
	public void mergeTraceFilesCalls(String directory){
		List<String> callFiles = getTraceCallsTraceFiles();
		mergeTraceFiles(callFiles, directory, callOutFile);
	}
	
	/**
     * Merges all files listed into one file
     * @param file input name and the directory path
     * @return file content as a list of strings
     */
	private void mergeTraceFiles(List<String> fileList, String fileDirectory, String outputFile){
		List<String> records = new ArrayList<String>();
		try {
			for (String file : fileList) {
				BufferedReader reader = new BufferedReader(new FileReader(fileDirectory + file));
				String line;
				while((line = reader.readLine()) != null){
					records.add(line);
				}
				reader.close();	
			}
			
			//Write the total trace files into one file
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileDirectory + outputFile));
			for (String item : records) {
				fileWriter.append(item);				
				fileWriter.newLine();
			}
			
			fileWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	

	//Static trace file names
	private List<String> getTraceExecutionTraceFiles(){
		List<String> list = new ArrayList<String>();
		list.add("tmteTrace.log");
		list.add("tmteTrace1.log");
		list.add("tmteTrace2.log");
		list.add("tmteTrace3.log");
		list.add("tmteTrace4.log");
		list.add("tmteTrace5.log");
		list.add("tmteTrace6.log");
		list.add("tmteTrace7.log");
		list.add("tmteTrace8.log");
		list.add("tmteTrace9.log");
		list.add("tmteTrace10.log");
		list.add("tmteTrace11.log");
		list.add("tmteTrace12.log");
		list.add("tmteTrace13.log");
		list.add("tmteTrace14.log");
		list.add("tmteTrace15.log");
		list.add("tmteTrace16.log");
		list.add("tmteTrace17.log");
		list.add("tmteTrace18.log");
		list.add("tmteTrace19.log");
		list.add("tmteTrace20.log");
		list.add("tmteTrace21.log");
		list.add("tmteTrace22.log");

		return list;
	}
	
	//Static trace file names
	private List<String> getTraceCallsTraceFiles(){
		List<String> list = new ArrayList<String>();
		list.add("tmdcTrace.log");
		list.add("tmdcTrace1.log");
		list.add("tmdcTrace2.log");
		list.add("tmdcTrace3.log");
		list.add("tmdcTrace4.log");
		list.add("tmdcTrace5.log");
		list.add("tmdcTrace6.log");
		list.add("tmdcTrace7.log");
		list.add("tmdcTrace8.log");
		list.add("tmdcTrace9.log");
		list.add("tmdcTrace10.log");
		list.add("tmdcTrace11.log");
		list.add("tmdcTrace12.log");
		list.add("tmdcTrace13.log");
		list.add("tmdcTrace14.log");
		list.add("tmdcTrace15.log");
		list.add("tmdcTrace16.log");
		list.add("tmdcTrace17.log");
		list.add("tmdcTrace18.log");
		list.add("tmdcTrace19.log");
		list.add("tmdcTrace20.log");
		list.add("tmdcTrace21.log");
		list.add("tmdcTrace22.log");
		
		return list;
	}
}
