package project;

import java.util.ArrayList;
import java.util.LinkedList;

public class ListGraph {
	private class Edge{
		int to;
		double weight;
		
		public Edge(int to, double w){
			this.to = to;
			this.weight = w;
		}
		
	}
	ArrayList<LinkedList<Edge>> edges;
	
	public ListGraph(int n){
		edges = new ArrayList<>();
		for(int i=0;i<n;i++){
			edges.add(new LinkedList<>());
		}
	}
	
	public void addEdge(int v1,int v2,double w){
		Edge edge1 = new Edge(v1,w);
		edges.get(v2).add(edge1);
		Edge edge2 = new Edge(v2,w);
		edges.get(v1).add(edge2);
	}
	
	public void addVertix(){
		edges.add(new LinkedList<>());
	}
	
}
