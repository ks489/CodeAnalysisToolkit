package dependenceAnalysis;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import util.DominanceTreeGenerator;
import util.cfg.CFGExtractor;
import util.cfg.Graph;
import util.cfg.Node;

import java.util.Collection;
import java.util.HashSet;


public class ControlDependenceComputation {
	
	protected String path;
	protected MethodNode mn;
	protected Graph cfg;
	
	public ControlDependenceComputation(String pathToClass, MethodNode mn) throws AnalyzerException{
		this.path = pathToClass;
		this.mn = mn;
		cfg = CFGExtractor.getCFG(path, mn);
	}
	
	public ControlDependenceComputation(Graph cfg){
		this.cfg = cfg;
	}
	
	public Graph computeControlDependenceEdges(){
		Graph controlDependenceTree = new Graph();
		addStartNode(cfg);
		try{
			DominanceTreeGenerator dtg = new DominanceTreeGenerator(cfg);
			Graph postdomtree= dtg.postDominatorTree();
			for(Node cfgNode : cfg.getNodes()){
				for(Node successor : cfg.getSuccessors(cfgNode)){
					if(!cfg.isDecisionEdge(cfgNode, successor))
						continue;
					// if cfgNode post-dominates successor node...
					if(postdomtree.getTransitiveSuccessors(successor).contains(cfgNode)) 
						continue;
					assert(!cfgNode.equals(successor));
					Node leastCommonAncestor = postdomtree.getLeastCommonAncestor(cfgNode,successor);
					//System.out.println(leastCommonAncestor+", "+cfgNode+", "+successor);
					Collection<Node> path = new HashSet<Node>();
					if(leastCommonAncestor.equals(cfgNode)){
						path.add(cfgNode);
						path.addAll(getNodesOnPath(postdomtree,leastCommonAncestor,successor));
					}
					else{
						path.addAll(getNodesOnPath(postdomtree,leastCommonAncestor,successor));
						path.remove(cfgNode);
						//assert(!path.contains(cfgNode));
					}
					for(Node target : path){
						controlDependenceTree.addNode(cfgNode);
						controlDependenceTree.addNode(target);
						controlDependenceTree.addEdge(cfgNode, target);
					}
				}
			}
		//Graph postDominatorTree = 
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return controlDependenceTree;
	}
	

	private Collection<? extends Node> getNodesOnPath(Graph tree, Node from,
			Node to) {
		Collection<Node> path = new HashSet<Node>();
		Node current = to;
		assert(tree.getTransitiveSuccessors(from).contains(to));
		while(!current.equals(from)){
			if(!current.equals(from))
				path.add(current);
			Collection<Node> predecessors = tree.getPredecessors(current);
			if(predecessors.isEmpty()){
				System.err.println("Failed to find parent node for "+current);
				break;
			}
			//Because we are walking up a tree, we know that each node can only have a single predecessor.
			assert(predecessors.size()==1);
			current = predecessors.iterator().next(); //take what should be the only node in the set.
		}
		assert(!path.contains(from));
		return path;
	}

	private void addStartNode(Graph edges) {
		Node start = new Node("start");
		edges.addNode(start);
		edges.addEdge(start, edges.getEntry());
		edges.addEdge(start, edges.getExit());
	}

}
