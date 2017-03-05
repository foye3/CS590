package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class Router {

	int id;
	boolean start; // router start up or shut down
	// int[] tick; // tick counter
	String name;
	HashMap<Router, LinkStatePacket> recivedLSP; // recived heighest LSP
	Graph graph;
	double dist[]; // shortest path
	Router next[]; // out link
	int lSPNumber;

	public Router(int id, String name) {
		this.id = id;
		this.start = true;
		this.name = name;
		this.lSPNumber = 0;
		this.recivedLSP = new HashMap<Router, LinkStatePacket>();
	}

	public Router() {

	}

	public boolean receivePacket(LinkStatePacket lsp, Router from, int[] tick) { // return get lsp successfuly
		if (!this.start)
			return false;
		if (lsp.getTtl() >= 0) { // decrement the LSP's TTL
			lsp.setTtl(lsp.getTtl() - 1);
		}
		if (lsp.getTtl() < 0)
			return true; // TTL equals 0 recive successfuly but not use
		Router originRouter = lsp.getOriginate();
		if (recivedLSP.containsKey(originRouter) && recivedLSP.get(originRouter).getLSPNumber() >= lsp.getLSPNumber()) {
			// already get a heigher LSP recive successfuly but not use
			return true;
		}
		if (recivedLSP.containsKey(originRouter)) {
			recivedLSP.remove(originRouter);
		}
		recivedLSP.put(originRouter, lsp); // store the new lsp

		// compare with self graph
		for (int i = 0; i < this.graph.edges.length; i++) {
			for (int j = 0; j < this.graph.edges.length; j++) {
				if (this.graph.edges[i][j] == Double.MAX_VALUE && this.graph.edges[i][j] != lsp.graph.edges[i][j]) {
					this.graph.edges[i][j] = lsp.graph.edges[i][j];
				}
			}
		}

		//change edge if router is shut down
		for (int i = 0; i < tick.length; i++) {

			if (tick[i] != 0) {
				for (int j = 0; j < tick.length; j++) {
					this.graph.removeEdge(this.graph.routers.get(i).id, this.graph.routers.get(j).id);
				}
			}
		}

		// shortestPath();//Dijkstra’s
		//forward to neighbors
		ArrayList<Router> neighbors = new ArrayList<>();
		double[] edges = this.graph.neighbors(this.graph.routers.indexOf(this));
		for (int i = 0; i < edges.length; i++) {
			if (edges[i] != Double.MAX_VALUE) {
				if (i != this.graph.routers.indexOf(from) && i != this.graph.routers.indexOf(this)) {
					neighbors.add(this.graph.routers.get(i));
				}
			}
		}
		for (int i = 0; i < neighbors.size(); i++) {
			neighbors.get(i).receivePacket(lsp, this, tick);
		}
		return true;
	}

	public void shortestPath() { // Dijkstra’s

		ArrayList<Router> routers = this.graph.routers;
		LinkedList<Router> Q = new LinkedList<>();
		for (int i = 0; i < routers.size(); i++) {
			Q.add(routers.get(i));
		}
		double[] dist = new double[routers.size()];
		Router[] pre = new Router[routers.size()];
		for (int i = 0; i < routers.size(); i++) {
			dist[i] = Double.MAX_VALUE;
		}
		dist[routers.indexOf(this)] = 0;

		while (!Q.isEmpty()) {// main loop
			Router u = Q.get(0);
			// find vertex in Q with min dist[u]
			for (Router r : Q) {
				if (dist[routers.indexOf(r)] < dist[routers.indexOf(u)]) {
					u = r;
				}
			}
			Q.remove(u);
			// for each neighbor v of u(minindex):
			double[] neighbors = this.graph.neighbors(routers.indexOf(u));
			for (int v = 0; v < neighbors.length; v++) {
				if (neighbors[v] != Double.MAX_VALUE) {
					double alt = dist[routers.indexOf(u)] + neighbors[v];
					if (alt < dist[v]) {
						dist[v] = alt;
						pre[v] = u;
					}
				}
			}
		}

		pre[routers.indexOf(this)] = this;
		this.dist = dist;
		Router[] next = pre;
		// change print order
		for (int i = 0; i < next.length; i++) {
			if (pre[i] == this) {
				pre[i] = this.graph.routers.get(i);
			}
		}
		for (int i = 0; i < next.length; i++) {
			if (pre[i] != null) {
				int j = i;
				Router r = pre[i];
				while (this.graph.routers.indexOf(r) != j) {

					r = pre[this.graph.routers.indexOf(r)];
					j = this.graph.routers.indexOf(r);
				}

				next[i] = r;
			}

		}
		this.next = next;
	}

	public void originatePacket() {
		if (!start) {
			return;
		}
		int[] tick = new int[this.graph.edges.length];
		ArrayList<Router> neighbors = new ArrayList<>();
		double[] edges = this.graph.neighbors(this.graph.routers.indexOf(this));
		for (int i = 0; i < edges.length; i++) {
			if (edges[i] != Double.MAX_VALUE) {
				if (i != this.graph.routers.indexOf(this)) {
					neighbors.add(this.graph.routers.get(i));
				}
			}
		}
		boolean recive = true;
		for (int i = 0; i < neighbors.size(); i++) {
			LinkStatePacket lsp = new LinkStatePacket(this, lSPNumber, this.graph);
			recive = neighbors.get(i).receivePacket(lsp, this, tick);//not recive return false
			lSPNumber++;
			if (!recive) {
				tick[this.graph.routers.indexOf(neighbors.get(i))]++;
				for (int j = 0; j < this.graph.routers.size(); j++) {
					this.graph.removeEdge(neighbors.get(i).id, this.graph.routers.get(j).id);
				}
			}
		}
		for (int i = 0; i < tick.length; i++) {
			if (tick[i] > 0) {
				recive = false;
			}
		}
		if (!recive) {// if any router not recive, send new LSP to neighbors
			neighbors = new ArrayList<>();
			edges = this.graph.neighbors(this.graph.routers.indexOf(this));
			for (int i = 0; i < edges.length; i++) {
				if (edges[i] != Double.MAX_VALUE && edges[i] != 0) {
					if (i != this.graph.routers.indexOf(this)) {
						neighbors.add(this.graph.routers.get(i));
					}
				}
			}
			for (int i = 0; i < neighbors.size(); i++) {
				LinkStatePacket lsp = new LinkStatePacket(this, lSPNumber, this.graph);
				recive = neighbors.get(i).receivePacket(lsp, this, tick);
				lSPNumber++;
			}
		}

	}

	public void printRoutingTable() {
		System.out.println(String.format("%-20s%-10s%-20s", "Network", "Cost", "Outgoing link"));
		for (int i = 0; i < dist.length; i++) {
			if (next[i] == null)
				continue;
			System.out.println(String.format("%-20s%-10s%-20s", this.graph.routers.get(i).name, dist[i], this.next[i].name));
		}

	}

}
