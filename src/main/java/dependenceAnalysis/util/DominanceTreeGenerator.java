package dependenceAnalysis.util;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import dependenceAnalysis.util.cfg.CFGExtractor;
import dependenceAnalysis.util.cfg.Graph;
import dependenceAnalysis.util.cfg.Node;

import java.io.IOException;
import java.util.*;

public class DominanceTreeGenerator {

    Graph cfg;
    protected Map<Node,Collection<Node>> dominate,postDominate;


    /**
     * In this constructor, we obtain the Control Flow Graph, and call the routines necessary
     * to calculate the Map from nodes to their respective dominators and post-dominators.
     *
     * @param methodNode
     */
    public DominanceTreeGenerator(String pathToClass,MethodNode methodNode) throws IOException, AnalyzerException {
        try {
            cfg = CFGExtractor.getCFG(pathToClass,methodNode);
        } catch (AnalyzerException e) {
            e.printStackTrace();
        }

        //The CFG has been extracted. Now we calculate the dominance relations from the CFG.

        dominate = calculateDominance(cfg, new HashMap<Node,Collection<Node>>());

        //To calculate the post-dominance, we simply reverse the control flow graph and repeat the exercise.

        postDominate = calculateDominance(reverseGraph(cfg),new HashMap<Node,Collection<Node>>());
    }
    
    /**
     * In this constructor, we take a given CFG and call the routines necessary
     * to calculate the Map from nodes to their respective dominators and post-dominators.
     *
     * @param methodNode
     */
    public DominanceTreeGenerator(Graph cfg) throws IOException, AnalyzerException {
        this.cfg = cfg;

        //The CFG has been extracted. Now we calculate the dominance relations from the CFG.

        dominate = calculateDominance(cfg, new HashMap<Node,Collection<Node>>());

        //To calculate the post-dominance, we simply reverse the control flow graph and repeat the exercise.

        postDominate = calculateDominance(reverseGraph(cfg),new HashMap<Node,Collection<Node>>());
    }


    /**
     * The dominance computation function.
     *
     * @param cfg
     * @param map
     * @return
     */
    private Map<Node, Collection<Node>> calculateDominance(Graph cfg, Map<Node, Collection<Node>> map){
        Node entry = cfg.getEntry();
        HashSet<Node> entryDom = new HashSet<Node>();
        entryDom.add(entry);
        map.put(entry, entryDom);
        for(Node n: cfg.getNodes()){
            if(n.equals(entry))
                continue;
            HashSet<Node> allNodes = new HashSet<Node>();
            allNodes.addAll(cfg.getNodes());
            map.put(n, allNodes);
        }
        boolean changed = true;
        while(changed){
            changed = false;
            for(Node n: cfg.getNodes()){
                if(n.equals(entry))
                    continue;
                Collection<Node> currentDominators = map.get(n);
                Collection<Node> newDominators = calculateDominators(cfg, map,n);

                if(!currentDominators.equals(newDominators)){
                    changed = true;
                    map.put(n, newDominators);
                    break;
                }
            }
        }
        return map;
    }

    /**
     * Produce a new Graph object, representing the reverse of the
     * Graph given in the cfg parameter.
     * @param cfg
     * @return
     */
    private Graph reverseGraph(Graph cfg){
        Graph reverseCFG = new Graph();
        Iterator<Node> cfgIt = cfg.getNodes().iterator();
        while(cfgIt.hasNext()){
            reverseCFG.addNode(cfgIt.next());
        }
        cfgIt = cfg.getNodes().iterator();
        while(cfgIt.hasNext()){
            Node n = cfgIt.next();
            Set<Node> successors = cfg.getSuccessors(n);
            for (Node succ : successors) {
                reverseCFG.addEdge(succ, n);
            }
        }
        return reverseCFG;
    }


    /**
     * Computes the intersection for a given set of sets of nodes (representing
     * the sets of dominators).
     * @param cfg
     * @param dominate
     * @param n
     * @return
     */
    private static Set<Node> calculateDominators(Graph cfg, Map<Node,Collection<Node>> dominate, Node n) {
        Set<Node> doms = new HashSet<Node>();
        doms.add(n);
        Iterator<Node> predIt = cfg.getPredecessors(n).iterator();
        Set<Node> intersection = new HashSet<Node>();
        if(!predIt.hasNext())
            return new HashSet<Node>();
        boolean firstTime = true;
        while(predIt.hasNext()){
            Node pred = predIt.next();
            Collection<Node> pDoms = dominate.get(pred);
            if(firstTime){
                intersection.addAll(pDoms);
                firstTime = false;
            }
            else{
                intersection.retainAll(pDoms);
            }
        }
        intersection.addAll(doms);
        return intersection;
    }

    private Graph buildDominatorTree(Graph cfg, Map<Node,Collection<Node>> domMap){
        Graph dominanceTree = new Graph();
        dominanceTree.addNode(cfg.getEntry());
        Map<Node,Collection<Node>> mapCopy = new HashMap<Node,Collection<Node>>();
        mapCopy.putAll(domMap);
        Iterator<Node> keyIt = mapCopy.keySet().iterator();
        while(keyIt.hasNext()){
            Node next = keyIt.next();
            mapCopy.get(next).remove(next);
        }
        Queue<Node> nodeQueue = new LinkedList<Node>();
        nodeQueue.add(cfg.getEntry());
        while(!nodeQueue.isEmpty()){
            Node m = nodeQueue.remove();
            Iterator<Node> nodeIterator = mapCopy.keySet().iterator();
            while(nodeIterator.hasNext()){
                Node n = nodeIterator.next();
                Collection<Node> doms = mapCopy.get(n);
                if(doms.contains(m)){
                    doms.remove(m);
                    if(doms.isEmpty()){
                        dominanceTree.addNode(n);
                        dominanceTree.addEdge(m,n);
                        nodeQueue.add(n);
                    }
                }

            }
        }
        return dominanceTree;
    }

    /**
     * Return a Graph representation of the dominator tree.
     * @return
     */
    public Graph dominatorTree() {
        return buildDominatorTree(cfg,dominate);
    }

    /**
     * Return a Graph representation of the post-dominator tree.
     * @return
     */
    public Graph postDominatorTree() {
        return buildDominatorTree(reverseGraph(cfg),postDominate);
    }












}
