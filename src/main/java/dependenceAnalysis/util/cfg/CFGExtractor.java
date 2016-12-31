package dependenceAnalysis.util.cfg;

import org.jgrapht.graph.DefaultEdge;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CFGExtractor {

	/**
	 * Builds the control flow graph for mn.
	 * @param owner
	 * @param mn
	 * @return
	 * @throws AnalyzerException
     */
	public static Graph getCFG(String owner, MethodNode mn)throws AnalyzerException {
		final Graph g = buildGraph(owner, mn);
		Node entry = new Node("Entry");
		Node exit = new Node("Exit");
		g.addNode(entry);
		g.addNode(exit);
		for(Node n: g.getNodes()){
			if(n.toString().equals("Exit") || n.toString().equals("Entry"))
				continue;
			if(g.getSuccessors(n).isEmpty())
				g.addEdge(n, exit);
			if(g.getPredecessors(n).isEmpty())
				g.addEdge(entry, n);
		}
	return g;
	}

	protected static Graph buildGraph(String owner,
			MethodNode mn) throws AnalyzerException {
		final InsnList instructions = mn.instructions;
		final Map<AbstractInsnNode,Node> nodes = new HashMap<AbstractInsnNode,Node>();
		final Graph g = new Graph();
		Analyzer a =new Analyzer(new BasicInterpreter()) {
			
			
			protected void newControlFlowEdge(int src, int dst) {
				AbstractInsnNode from = instructions.get(src);
                AbstractInsnNode to = instructions.get(dst);
                Node srcNode = nodes.get(from);
                if(srcNode == null){
                	srcNode =  new Node(from);
                	nodes.put(from,srcNode);
                	g.addNode(srcNode);
                }
                Node tgtNode = nodes.get(to);
                if(tgtNode == null){
                	tgtNode = new Node(to);
                	nodes.put(to,tgtNode);
                	g.addNode(tgtNode);
                }
                g.addEdge(srcNode, tgtNode);
			}
		};
		
		a.analyze(owner, mn);
		
		return g;
	}
	
	//cc = Edges-Nodes + 2 * Procedures
	/**
	 * Calculates the extended cyclomatic complexity for each method in a class
	 * @param Class name to get all cyclomatic complexity values for all methods
	 * @param mn
	 * @return
	 * @throws AnalyzerException
     */
	protected static Map<String, Integer> GetCyclomaticComplexityToCSVFile(String className) throws IOException{
		Map<String, Integer> complexityMap = new HashMap<String, Integer>();
		try {
			
			ClassNode cn = new ClassNode(Opcodes.ASM4);
	  
	        InputStream in=CFGExtractor.class.getResourceAsStream(className);
	        ClassReader classReader=new ClassReader(in);
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
	        		
	        		complexityMap.put(cn.name + "." + mn.name + ",", cc);
	        		//System.out.println(cn.name + "." + mn.name + "->" + cc);
	    	}

		} catch (AnalyzerException e) {
			e.printStackTrace();
		} 	
		return complexityMap;
	}

	public static void main(String[] args) throws IOException{
		Map<String, Integer> ccMap = GetCyclomaticComplexityToCSVFile("/java/awt/geom/Area.class");
		/*ClassNode cn = new ClassNode(Opcodes.ASM4);
        //InputStream in=CFGExtractor.class.getResourceAsStream("/java/awt/geom/Area.class");
        InputStream in=CFGExtractor.class.getResourceAsStream("/java/awt/geom/Area.class");
        //isRectangular()Z
        ClassReader classReader=new ClassReader(in);
        classReader.accept(cn, 0);
        for(MethodNode mn : (List<MethodNode>)cn.methods){
        	try {
        		//transform
        		//
        		if(mn.name.equals("isPolygonal")){
        		//if(mn.name.equals("isRectangular")){
        		//if(mn.name.equals("isEmpty")){
        			System.out.println("================CFG FOR: "+cn.name+"."+mn.name+mn.desc+" =================");
        			Graph graph = CFGExtractor.getCFG(cn.name, mn);
            		System.out.println(graph);
            		Map<DefaultEdge, Boolean> des = graph.decisions;
            		System.out.println("The size of the decisions is " + graph.decisions.size());
            		
            		Iterator it = des.entrySet().iterator();
            	    while (it.hasNext()) {
            	        Map.Entry pair = (Map.Entry)it.next();
            	        //System.out.println(pair.getKey() + " = " + pair.getValue());
            	        if(pair.getValue().equals(true)){
            	        	System.out.println(pair.getKey() + " = " + pair.getValue());
            	        }
            	        it.remove(); // avoids a ConcurrentModificationException
            	    }
        		}
        		
			} catch (AnalyzerException e) {
				e.printStackTrace();
			}
        }*/
	}
}
