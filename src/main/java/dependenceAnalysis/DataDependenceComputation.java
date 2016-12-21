package dependenceAnalysis;

import br.usp.each.saeg.asm.defuse.Variable;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import util.DataFlowAnalysis;
import util.cfg.CFGExtractor;
import util.cfg.Graph;
import util.cfg.Node;

import java.util.Collection;
import java.util.HashSet;


public class DataDependenceComputation {
	
	protected String path;
	protected MethodNode mn;
	protected Graph cfg;
	
	public DataDependenceComputation(String pathToClass, MethodNode mn) throws AnalyzerException{
		this.path = pathToClass;
		this.mn = mn;
		cfg = CFGExtractor.getCFG(path, mn);
	}
	
	
	public Graph computeDataDependenceEdges(){
		Graph defUse = new Graph();
		DataFlowAnalysis dfa = new DataFlowAnalysis();
		try {
			for(Node n : cfg.getNodes()){
				Collection<Node> defs = new HashSet<Node>();
				if(n.getInstruction()==null)
					continue;
				for(Variable use : dfa.usedBy(path, mn, n.getInstruction())){
					defs.addAll(bfs(dfa, cfg,n,use, new HashSet<Object>()));
				}
				for(Node def : defs){
					if(def.equals(n))
						continue;
					defUse.addNode(n);
					defUse.addNode(def);
					defUse.addEdge(def,n);
				}
			}
		}catch (AnalyzerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return defUse;
		}
	
	

	private Collection<Node> bfs(DataFlowAnalysis dfa, Graph cfg, Node n, Variable use,Collection<Object> done) throws AnalyzerException {
		Collection<Node> uses = new HashSet<Node>();
		for(Node pred : cfg.getPredecessors(n)){
			if(done.contains(pred))
				continue;
			done.add(pred);
			if(pred.getInstruction()== null)
				continue;
			if(dfa.definedBy(path, mn, pred.getInstruction()).contains(use)){
				uses.add(pred);
				continue;
			}
			else{
				uses.addAll(bfs(dfa,cfg,pred,use,done));
			}
				
		}
		return uses;
	}
	
		
}


