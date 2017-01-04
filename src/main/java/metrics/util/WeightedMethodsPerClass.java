package metrics.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import dependenceAnalysis.util.cfg.CFGExtractor;
import dependenceAnalysis.util.cfg.Graph;
import dependenceAnalysis.util.cfg.Node;

public class WeightedMethodsPerClass {
	/**
	 * Calculates the weighted method per class value
	 * @param A map with all the cyclomatic complexity scores for each method within a given class
	 * @return A sum of all cyclomatic complexity scores for a given class
     */
	public static int GetWeightedMethodPerClass(Map<String, Integer> ccMap) {
		int totalValue = 0;
		Iterator it = ccMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
        	int num = (Integer)pair.getValue();
        	totalValue = totalValue + num;
	        it.remove(); 
	    }
	    return totalValue;
	}
	
	/**
	 * Calculates the weighted method per class value and gives the average cyclometric complexity score for that class
	 * @param A map with all the cyclomatic complexity scores for each method within a given class
	 * @return A sum of all cyclomatic complexity scores for a given class
     */
	public static int GetWeightedMethodPerClassAverage(Map<String, Integer> ccMap){
		int count = 0;
		int totalValue = 0;
		Iterator it = ccMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	count++;
	        Map.Entry pair = (Map.Entry)it.next();
        	int num = (Integer)pair.getValue();
        	totalValue = totalValue + num;
	        it.remove(); 
	    }
	    if(count != 0)
	    	return totalValue / count;
	    return 0;
	}
}
