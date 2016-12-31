package dependenceAnalysis.util.cfg;

import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.*;

public class Graph {

	protected DirectedMultigraph<Node, DefaultEdge> graph;
	protected Map<DefaultEdge, Boolean> decisions;
	
	public Graph(){
        Node.sNextId = 1;
		decisions = new HashMap<DefaultEdge, Boolean>();
		graph = new DirectedMultigraph<Node, DefaultEdge>(new ClassBasedEdgeFactory<Node, DefaultEdge>(DefaultEdge.class));
	}
	
	public void addNode(Node n){
		graph.addVertex(n);
	}

	public void addEdge(Node a, Node b) {
		graph.addEdge(a,b);
		updateDecisions(a);
	}

	private void updateDecisions(Node a) {
		boolean decision = false;

		if(graph.outgoingEdgesOf(a).size()>1) {
			decision = true;
		}
		for(DefaultEdge outgoing : graph.outgoingEdgesOf(a)) {
			decisions.put(outgoing, decision);
		}

	}

	/**
	 * Is the edge between these two nodes the result of a decision?
	 *
	 * This is the case if the from instruction has two outgoing edges.
	 * @param from
	 * @param to
     * @return
     */
	public boolean isDecisionEdge(Node from, Node to){
		DefaultEdge cfgEdge = graph.getEdge(from,to);
		if(cfgEdge == null){
			return false;
		}
		else{
			return decisions.get(cfgEdge);
		}
	}

	public Set<Node> getPredecessors(Node a){
		Set<Node> preds = new HashSet<Node>();
		for(DefaultEdge de : graph.incomingEdgesOf(a)){
			preds.add(graph.getEdgeSource(de));
		}
		return preds;
	}
	
	public Set<Node> getSuccessors(Node a){

		Set<Node> succs = new HashSet<Node>();
		for(DefaultEdge de : graph.outgoingEdgesOf(a)){
			succs.add(graph.getEdgeTarget(de));
		}
		return succs;
	}
	
	public Set<Node> getNodes(){
		return graph.vertexSet();
	}
	
	public Node getEntry(){
		for(Node n : getNodes()){
			if(graph.incomingEdgesOf(n).isEmpty())
				return n;
		}
		return null;
	}
	
	public Node getExit(){
		for(Node n : getNodes()){
			if(graph.outgoingEdgesOf(n).isEmpty())
				return n;
		}
		return null;
	}
	
	public String toString(){
		String dotString = "digraph cfg{\n";
		for (Node node : getNodes()) {
			for (Node succ: getSuccessors(node)) {
				dotString+=node.toString()+"->"+succ.toString()+"\n";
			}
		}
		dotString+="}";
		return dotString;
	}

	/**
	 * Return all *transitive* successors of m - i.e. any instructions
	 * that could eventually be reached from m.
	 * @param m
	 * @return
     */
	public Collection<Node> getTransitiveSuccessors(Node m){
		return transitiveSuccessors(m, new HashSet<Node>());
	}

	private Collection<Node> transitiveSuccessors(Node m, Set<Node> done){
		Collection<Node> successors = new HashSet<Node>();
		for(Node n : getSuccessors(m)){
			if(!done.contains(n)) {
				successors.add(n);
				done.add(n);
				successors.addAll(transitiveSuccessors(n, done));
			}
		}
		return successors;
	}
	
	public Node getLeastCommonAncestor(Node x, Node y) {
        Node current = x;
        while(!containsTransitiveSuccessors(current,x,y)){
            current = getPredecessors(current).iterator().next();
        }
        return current;
    }

	private boolean containsTransitiveSuccessors(Node x, Node x2, Node y) {
		Collection<Node> transitiveSuccessors = getTransitiveSuccessors(x);
        if(transitiveSuccessors.contains(x2) && transitiveSuccessors.contains(y))
        	return true;
        else
        	return false;
	}

}
