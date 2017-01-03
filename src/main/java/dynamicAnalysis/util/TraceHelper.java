package dynamicAnalysis.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kyle Spindler on 30/11/2016.
 */
public class TraceHelper {
	
	/**
     * Removes all duplicated data entries in the digraph and converts to digraph dot format
     * @param list of string data from the merge trace file
     * @return returns a new list of string data with none duplicated entries
     */
	public List<String> removeDiGraphDataDuplicates(List<String> data){
		List<String> newDataList = new ArrayList<String>();
		for (String item : data) {
			if(!newDataList.contains(item)){
				newDataList.add(item);
			}
		}		
		return newDataList;
	}
	
	/**
     * Gets the average execution times for a particular class and converts to csv format
     * @param list of string data from the merge trace file
     * @return returns a new list of string data with the average times
     */
	public List<String> GetCsvClassExecutionAverage(List<String> data){
		List<String> classNameList = new ArrayList<String>();
		List<Double> classTotalExeList = new ArrayList<Double>();
		List<Integer> classAmountList = new ArrayList<Integer>();
		
		List<String> returnList = new ArrayList<String>();
		
		List<String> newList = new ArrayList<String>();
		for (String row : data) {
			String[] items = row.split(",");
			
			if(!classNameList.contains(items[1])){
				classNameList.add(items[1]);
				classTotalExeList.add(Double.parseDouble(items[0]));
				classAmountList.add(1);
				
			}else{
				//Add to the counter list and the total list used later to calculate the average
				int position = classNameList.indexOf(items[1]);
				classTotalExeList.set(position, classTotalExeList.get(position) + Double.parseDouble(items[0]));
				classAmountList.set(position, classAmountList.get(position) + 1);				
			}						
		}
		//Calculate the average
		for (int i = 0; i < classNameList.size(); i++) {
			double totalAmount = classTotalExeList.get(i);
			int times = classAmountList.get(i);
			returnList.add(totalAmount / times + "," +  classNameList.get(i));
		}
		return returnList;
	}
	
	/**
     * Gets the average execution times for a particular method and converts to csv format
     * @param list of string data from the merge trace file
     * @return returns a new list of string data with the average times
     */
	public List<String> getCsvMethodExecutionAverage(List<String> data){	
		List<String> methodNameList = new ArrayList<String>();
		List<Double> methodTotalExeList = new ArrayList<Double>();
		List<Integer> methodAmountList = new ArrayList<Integer>();
		
		List<String> returnList = new ArrayList<String>();
		
		List<String> newList = new ArrayList<String>();
		for (String row : data) {
			String[] items = row.split(",");
			
			if(!methodNameList.contains(items[2])){
				methodNameList.add(items[2]);
				methodTotalExeList.add(Double.parseDouble(items[0]));
				methodAmountList.add(1);				
			}else{
				//Add to the counter list and the total list used later to calculate the average
				int position = methodNameList.indexOf(items[2]);
				methodTotalExeList.set(position, methodTotalExeList.get(position) + Double.parseDouble(items[0]));
				methodAmountList.set(position, methodAmountList.get(position) + 1);				
			}						
		}
		//Calculates the average
		for (int i = 0; i < methodNameList.size(); i++) {
			double totalAmount = methodTotalExeList.get(i);
			int times = methodAmountList.get(i);
			returnList.add(totalAmount / times + "," +  methodNameList.get(i));
		}
		return returnList;
	}
	
	/**
     * Counts the amount of times a particular class gets called and converts to csv format
     * @param list of string data from the merge trace file
     * @return returns the class and amount in a list of strings
     */
	public List<String> GetCsvClassExecutionCount(List<String> data){
		List<String> classNameList = new ArrayList<String>();
		List<Integer> classCount = new ArrayList<Integer>();
		
		List<String> returnList = new ArrayList<String>();
		
		List<String> newList = new ArrayList<String>();
		for (String row : data) {
			String[] items = row.split(",");
			
			if(!classNameList.contains(items[1])){
				classNameList.add(items[1]);				
				classCount.add(1);
				
			}else{
				int position = classNameList.indexOf(items[1]);
				classCount.set(position, classCount.get(position) + 1);				
			}						
		}
		for (int i = 0; i < classNameList.size(); i++) {			
			int count = classCount.get(i);
			returnList.add(count + "," +  classNameList.get(i));
		}
		return returnList;
	}
	
	/**
     * Counts the amount of times a particular method gets called and converts to csv format
     * @param list of string data from the merge trace file
     * @return returns the method and amount in a list of strings
     */
	public List<String> getCsvMethodExecutionCount(List<String> data){	
		List<String> methodNameList = new ArrayList<String>();
		List<Integer> classCount = new ArrayList<Integer>();
		
		List<String> returnList = new ArrayList<String>();
		
		List<String> newList = new ArrayList<String>();
		for (String row : data) {
			String[] items = row.split(",");
			
			if(!methodNameList.contains(items[2])){
				methodNameList.add(items[2]);
				classCount.add(1);
				
			}else{
				int position = methodNameList.indexOf(items[2]);
				classCount.set(position, classCount.get(position) + 1);				
			}						
		}
		for (int i = 0; i < methodNameList.size(); i++) {
			int count = classCount.get(i);
			returnList.add(count + "," +  methodNameList.get(i));
		}
		return returnList;
	}
	
	/**
     * Gets a particular file with all its contents
     * @param file input name and the directory path
     * @return file content as a list of strings
     */
	public List<String> getFileRecords(String inputFileName, String fileDirectory){
		List<String> records = new ArrayList<String>();
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(fileDirectory + inputFileName + ".log"));
			String line;
			while((line = reader.readLine()) != null){
				records.add(line);
			}			
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return records;
	}
}
