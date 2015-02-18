package measure.graph.criteria;


import java.util.Vector;

import measure.graph.RelativeCommunityCriteria;
import edu.uci.ics.jung.graph.Graph;

/**
 * <pre> Q(undirected)= 1/2m Sum(i,j){Aij - (K(i)-K(j))/2m } Delta(i,j) </pre> 
 * where m is number of links, K(i) is degree of node i: # of links for node i  
 * <p> Delta(i,j) is one if i and j are in the same cluster, otherwise 0
 * <p> Aij is 1 for un-weighted networks: can be any number for weighted networks
 */

public class AltModularity<V, E> extends RelativeCommunityCriteria<V,E> {

	public AltModularity(Graph<V, E> graph) {
		super(graph);
	}
	public double evaluateCommunities(Vector<Graph<V,E>> communities){
		
		double modularity = 0;
		double m; // edges_within_cluster, edges_between_cluster

		m = graph.getEdgeCount();

		for (Graph<V,E> cluster : communities) {
			for (V v1 : cluster.getVertices()) {
				for (V v2 : cluster.getVertices()) if(v1!=v2){
					if (cluster.getNeighbors(v1).contains(v2))
						modularity += 1;
					modularity -= graph.getNeighbors(v1).size()	* graph.getNeighbors(v2).size() / (2 * m);
				}
			}
		}

		modularity /= (2 * m);
		return modularity;
	}
	

	//TODO: make it weighted !!!! edge count = sum W, size = sum W, 1 = W 
	public double evaluateAltCommunities(Vector<Graph<V,E>> communities){
		
		double modularity = 0;
		double Max , E=0; 
		
		Max = 2*  graph.getEdgeCount(); //Original formula

		for (Graph<V,E> cluster : communities) {
		
			for (V v1 : cluster.getVertices()) {
				for (V v2 : cluster.getVertices()) if(v1!=v2){
					double pij = 0, pi=0,pj=0;
					
					for (V k : graph.getNeighbors(v1)) pi++;	pi/=Max;
					for (V k : graph.getNeighbors(v2)) pj++;	pj/=Max;
				
					if (cluster.getNeighbors(v1).contains(v2))
						pij = 1/(Max);
						
					modularity += pij;
					E += pi*pj ;				
				}
			}
			
		}
		modularity  = (modularity - E ) ;// (modularity - E );  Original formula
		return modularity;
	}
	
	public String toString(){
		return "AQ";
	}

	public String getName() {
		return "AQ" ;
	}

}
