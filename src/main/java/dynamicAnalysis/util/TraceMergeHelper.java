package dynamicAnalysis.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kyle Spindler on 30/11/2016.
 */
public class TraceMergeHelper {	
	
	String exeOutFile = "totalTmteTrace.log";
	String callOutFile = "totalTmdcTrace.log";
	String tracerFileDirectory = "./traces/";
	
	List<String> fileNames = new ArrayList<String>();
	
	public void mergeTraceFiles(String directory){
		List<File> files = getTraceFiles(directory);		
		mergeTraceFiles(files, directory,  exeOutFile);
	}
	

	
	/**
     * Merges all files listed into one file
     * @param file input name and the directory path
     * @return file content as a list of strings
     */
	private void mergeTraceFiles(List<File> fileList, String fileDirectory, String outputFile){
		List<String> records = new ArrayList<String>();
		try {
			for (File file : fileList) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
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
	private List<File> getTraceFiles(String fileDirectory){
		List<File> list = new ArrayList<File>();
		File root = new File(fileDirectory);
		ArrayList<File> files = new ArrayList<File>(Arrays.asList(root.listFiles()));
        for(File f: files) {
            if(!f.isDirectory())
            	list.add(f);
        }			
	
		return list;
	}

	
}
