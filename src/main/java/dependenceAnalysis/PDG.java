package dependenceAnalysis;

import org.objectweb.asm.tree.AbstractInsnNode;
import util.cfg.Graph;
import util.cfg.Node;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class PDG extends Graph {
	
	protected Graph cdg;
	protected Graph dfg;
	
	public PDG(Graph cdg, Graph dfg){
		super();
		this.cdg = cdg;
		this.dfg = dfg;
		for(Node n : cdg.getNodes()){
			addNode(n);
		}
		for(Node n : dfg.getNodes()){
			addNode(n);
		}
		for(Node n : getNodes()){
			
			for(Node s: cdg.getSuccessors(n)){
				assert(graph.containsVertex(s));
				assert(graph.containsVertex(n));
				addEdge(n, s);
			}
			if(!dfg.getNodes().contains(n))
				continue;
			for(Node s: dfg.getSuccessors(n)){
				assert(graph.containsVertex(s));
				assert(graph.containsVertex(n));
				addEdge(n, s);
			}
		}
	}
	
	private Collection<Node> transitivePredecessors(Node m, Set<Node> done){
		Collection<Node> preds = new HashSet<Node>();
		for(Node n : getPredecessors(m)){
			if(!done.contains(n)) {
				preds.add(n);
				done.add(n);
				preds.addAll(transitivePredecessors(n, done));
			}
		}
		return preds;
	}
	
	public Collection<AbstractInsnNode> slice(AbstractInsnNode criterion){
		Node critNode = findNode(criterion);
		Collection<Node> sliceNodes= transitivePredecessors(critNode, new HashSet<Node>());
		Collection<AbstractInsnNode> instructions = new HashSet<AbstractInsnNode>();
		for(Node sliceNode : sliceNodes){
			instructions.add(sliceNode.getInstruction());
		}
		return instructions;
	}
	
	private Node findNode(AbstractInsnNode criterion) {
		for(Node n : graph.vertexSet()){
			if(n.getInstruction().equals(criterion))
				return n;
		}
		return null;
	}

	public String toString(){
		String dotString = "digraph pdg{\n";
		for (Node node : dfg.getNodes()) {
			for (Node succ: dfg.getSuccessors(node)) {
				dotString+=node.toString()+"->"+succ.toString()+"[style=\"dashed\"]\n";
			}
		}
		for (Node node : cdg.getNodes()) {
			for (Node succ: cdg.getSuccessors(node)) {
				dotString+=node.toString()+"->"+succ.toString()+"\n";
			}
		}
			
		dotString+="}";
		return dotString;
	}

}
