package dynamicAnalysis.util;

import java.util.List;

/**
 * Created by Kyle Spindler on 30/11/2016.
 */
public class DynamicAnalysisTracer {
	
	public static void RunDynamicAnalysis(){
		
		//Chart Test Cases
		String executionInputFileName = "totalTmteTrace";
		String executionOutputFileName = "methodExecution";
		String executionFileDirectory = "./dynamicAnalysis/TraceMethodTimeExecution/TraceLogFiles/";		
		String callInputFileName = "totalTmdcTrace";
		String callOutputFileName = "methodCalls";
		String callFileDirectory = "./dynamicAnalysis/TraceMethodDependencyCalls/TraceLogFiles/";
		
		//Refactored Directories
		String executionFileDirectoryRefactor = "./dynamicAnalysis/TraceMethodTimeExecution/TraceLogFiles/";
		String callFileDirectoryRefactor = "./dynamicAnalysis/TraceMethodDependencyCalls/TraceLogFiles/";
		
		//Renderer Test Cases
		String executionFileDirectoryRenderer = "./dynamicAnalysis/TraceMethodTimeExecution/RefactoredLogFiles/";
		String callFileDirectoryRenderer = "./dynamicAnalysis/TraceMethodDependencyCalls/RefactoredLogFiles/";
		
		//Merges all trace files
		TraceMergeHelper merger = new TraceMergeHelper();
		//merger.mergeTraceFiles(executionFileDirectory);
		//merger.mergeTraceFiles(callFileDirectory);
		
		//merger.mergeTraceFiles(executionFileDirectoryRenderer);
		//merger.mergeTraceFiles(callFileDirectoryRenderer);
		
		//Refactored
		merger.mergeTraceFiles(executionFileDirectoryRenderer);
		//merger.mergeTraceFiles(callFileDirectoryRenderer);
		
		
		String[] methodHeaders = new String[] {
			"ExecutionTime",
			"Method"
		};
		String[] classHeaders = new String[] {
			"ExecutionTime",
			"Class",
		};

		TraceHelper tHelper = new TraceHelper();
		//List<String> csvData = tHelper.getFileRecords(executionInputFileName, executionFileDirectory);
		//List<String> csvDataRenderer = tHelper.getFileRecords(executionInputFileName, executionFileDirectoryRenderer);
		
		List<String> csvDataRefactor = tHelper.getFileRecords(executionInputFileName, executionFileDirectoryRefactor);
		//List<String> csvDataRendererRefactor = tHelper.getFileRecords(executionInputFileName, callFileDirectoryRenderer);
		
		List<String> methodExecutionTimesRefactor = tHelper.getCsvMethodExecutionAverage(csvDataRefactor);
		List<String> classExecutionTimesRefactor = tHelper.GetCsvClassExecutionAverage(csvDataRefactor);
		List<String> methodCountRefactor = tHelper.getCsvMethodExecutionCount(csvDataRefactor);
		List<String> classCountRefactor = tHelper.GetCsvClassExecutionCount(csvDataRefactor);
		
		//Sort out list for graph outputs for method / classes execution times and call counts for the charts
		//List<String> methodExecutionTimes = tHelper.getCsvMethodExecutionAverage(csvData);
		//List<String> classExecutionTimes = tHelper.GetCsvClassExecutionAverage(csvData);
		//List<String> methodCount = tHelper.getCsvMethodExecutionCount(csvData);
		//List<String> classCount = tHelper.GetCsvClassExecutionCount(csvData);
		
		//List<String> methodExecutionTimesRenderer = tHelper.getCsvMethodExecutionAverage(csvDataRenderer);
		//List<String> classExecutionTimesRenderer = tHelper.GetCsvClassExecutionAverage(csvDataRenderer);
		//List<String> methodCountRenderer = tHelper.getCsvMethodExecutionCount(csvDataRenderer);
		//List<String> classCountRenderer = tHelper.GetCsvClassExecutionCount(csvDataRenderer);
		
		TraceConverterHelper converter = new TraceConverterHelper();
		//converter.convertLogToCSV(methodHeaders, methodExecutionTimes, "methodTimes", executionFileDirectory);
		//converter.convertLogToCSV(classHeaders, classExecutionTimes, "classTimes", executionFileDirectory);
		
		//converter.convertLogToCSV(methodHeaders, methodCount, "methodCount", executionFileDirectory);
		//converter.convertLogToCSV(classHeaders, classCount, "classCount", executionFileDirectory);
		//converter.convertLogToDigraph(callInputFileName, callOutputFileName, callFileDirectory);
		
		//converter.convertLogToCSV(methodHeaders, methodExecutionTimesRenderer, "methodTimesRenderer", executionFileDirectoryRenderer);
		//converter.convertLogToCSV(classHeaders, classExecutionTimesRenderer, "classTimesRenderer", executionFileDirectoryRenderer);
		
		//converter.convertLogToCSV(methodHeaders, methodCountRenderer, "methodCountRenderer", executionFileDirectoryRenderer);
		//converter.convertLogToCSV(classHeaders, classCountRenderer, "classCountRenderer", executionFileDirectoryRenderer);
		//converter.convertLogToDigraph(callInputFileName, callOutputFileName, callFileDirectoryRenderer);
		
		//Refactored
		//converter.convertLogToDigraph(callInputFileName, callOutputFileName, callFileDirectoryRenderer);	
		converter.convertLogToCSV(methodHeaders, methodExecutionTimesRefactor, "methodTimes", executionFileDirectoryRefactor);
		converter.convertLogToCSV(classHeaders, classExecutionTimesRefactor, "classTimes", executionFileDirectoryRefactor);
		converter.convertLogToCSV(methodHeaders, methodCountRefactor, "methodCount", executionFileDirectoryRefactor);
		converter.convertLogToCSV(classHeaders, classCountRefactor, "classCount", executionFileDirectoryRefactor);
		
	}
	

}
