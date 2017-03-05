package project;

import java.util.ArrayList;

public class Graph {
	public double[][] edges;
	public ArrayList<Router> routers;
	public Graph(ArrayList<Router> routers){
		this.routers = routers;
		int v = routers.size();
		edges = new double[v][v];
		for(int i=0;i<v;i++){
			for(int j=0;j<v;j++){
				edges[i][j] = Double.MAX_VALUE;
			}
			edges[i][i]=0;
		}
	}
	
	public void addEdge(int r1, int r2,double w){
		int v1=0;
		int v2=0;
		for(int i=0;i<routers.size();i++){
			if(routers.get(i).id==r1){
				v1 =i;
			}
			if(routers.get(i).id==r2){
				v2 =i;
			}
		}
		edges[v1][v2] = w;
		edges[v2][v1] = w;
		
	}
	public void removeEdge(int r1,int r2){
		if(r1==r2) return;
		int v1=0;
		int v2=0;
		for(int i=0;i<routers.size();i++){
			if(routers.get(i).id==r1){
				v1 =i;
			}
			if(routers.get(i).id==r2){
				v2 =i;
			}
		}
		edges[v1][v2] = Double.MAX_VALUE;
		edges[v2][v1] = Double.MAX_VALUE;
	}
	public boolean isAdjacent(int v1, int v2){
		return edges[v1][v2] != Double.MAX_VALUE;
	}
	
	public double[] neighbors(int v){
		double[] neighbors = new double[edges.length];
		for(int i=0;i<edges.length;i++){
			neighbors[i] =edges[v][i];
		}
		return neighbors;
	}
	
}
