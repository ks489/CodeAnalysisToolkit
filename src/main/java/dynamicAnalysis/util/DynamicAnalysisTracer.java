package dynamicAnalysis.util;

import java.util.List;

/**
 * Created by Kyle Spindler on 30/11/2016.
 */
public class DynamicAnalysisTracer {
	
	public static void RunDynamicAnalysis(){
		String executionInputFileName = "totalTmteTrace";
		String executionOutputFileName = "methodExecution";
		String executionFileDirectory = "./dynamicAnalysis/TraceMethodTimeExecution/TraceLogFiles/";
		
		String callInputFileName = "totalTmdcTrace";
		String callOutputFileName = "methodCalls";
		String callFileDirectory = "./dynamicAnalysis/TraceMethodDependencyCalls/TraceLogFiles/";
		//Merges all trace files
		TraceMergeHelper merger = new TraceMergeHelper();
		merger.mergeTraceFilesExecutions(executionFileDirectory);
		merger.mergeTraceFilesCalls(callFileDirectory);
		//merger.mergeTraceFiles();
		
		String[] methodHeaders = new String[] {
			"ExecutionTime",
			"Method"
		};
		String[] classHeaders = new String[] {
			"ExecutionTime",
			"Class",
		};
		
		
		
		
		TraceHelper tHelper = new TraceHelper();
		List<String> csvData = tHelper.getFileRecords(executionInputFileName, executionFileDirectory);
		
		//Sort out list for graph outputs for method / classes execution times and call counts
		List<String> methodExecutionTimes = tHelper.getCsvMethodExecutionAverage(csvData);
		List<String> classExecutionTimes = tHelper.GetCsvClassExecutionAverage(csvData);
		List<String> methodCount = tHelper.getCsvMethodExecutionCount(csvData);
		List<String> classCount = tHelper.GetCsvClassExecutionCount(csvData);
		
		TraceConverterHelper converter = new TraceConverterHelper();
		converter.convertLogToCSV(methodHeaders, methodExecutionTimes, "methodTimes", executionFileDirectory);
		converter.convertLogToCSV(classHeaders, classExecutionTimes, "classTimes", executionFileDirectory);
		
		converter.convertLogToCSV(methodHeaders, methodCount, "methodCount", executionFileDirectory);
		converter.convertLogToCSV(classHeaders, classCount, "classCount", executionFileDirectory);
		
		

		converter.convertLogToDigraph(callInputFileName, callOutputFileName, callFileDirectory);
	}
	

}
