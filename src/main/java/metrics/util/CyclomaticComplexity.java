package metrics.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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

public class CyclomaticComplexity {
	/**
	 * Calculates the extended cyclomatic complexity for each method in a class
	 * @param Class name to get all cyclomatic complexity values for all methods
	 * @return A map of all methods and their cyclomatic complexity score
	 * @throws IOException
     */
	public static Map<String, Integer> GetAllCyclomaticComplexityPerClass(String className) throws IOException{
		Map<String, Integer> complexityMap = new HashMap<String, Integer>();
		try {
		
			ClassNode cn = new ClassNode(Opcodes.ASM4);
	        //InputStream in=CFGExtractor.class.getResourceAsStream(className);
	        InputStream in=new FileInputStream(className);
	        ClassReader classReader=new ClassReader(in);
	        //CyclomaticComplexity.
	        classReader.accept(cn, 0);
	        for(MethodNode mn : (List<MethodNode>)cn.methods){
	    			Graph graph = CFGExtractor.getCFG(cn.name, mn);
	
	        	    int totalEdges = 0;
	        	    for (Node node : graph.getNodes()) {
	        			for (Node succ: graph.getSuccessors(node)) {
	        				totalEdges++;
	        			}
	        		}
	        	    int totalNodes = graph.getNodes().size();
	        		int cc = totalEdges - totalNodes + 2;
	        		//System.out.println("CC = " + cc);
	        		complexityMap.put(cn.name + "." + mn.name + ",", cc);
	    	}

		} catch (AnalyzerException e) {
			e.printStackTrace();
		} 	
		return complexityMap;
	}
}
