package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {

		String filename = "src/project/infile.dat"; // file location

		ArrayList<Router> routers = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String s;

		while ((s = br.readLine()) != null) { // read first time add all router
			
			if (s.matches(".*\\..*\\..*")) {//has two '.'
				String[] stringarr = s.trim().split(" ");
				Router router = new Router(Integer.valueOf(stringarr[0]), stringarr[1]);
				routers.add(router);
			}
		}
		BufferedReader br2 = new BufferedReader(new FileReader(filename));
		int i = 0;
		while ((s = br2.readLine()) != null) { // read second time add each
												// router's driect edges
			if (s.matches(".*\\..*\\..*")) {
				Graph graph = new Graph(routers);
				routers.get(i).graph = graph;
				i++;
				
			}
			if (!s.matches(".*\\..*\\..*")) {
				String[] stringarr = s.trim().split(" ");
				routers.get(i - 1).graph.addEdge(routers.get(i - 1).id, Integer.valueOf(stringarr[0]),
						Double.valueOf(stringarr[1]));
			}
		}
		
		Scanner sc = new Scanner(System.in);
		outer:
		while (true) {
			System.out.println("----------------------------------------------------------------------------------");
			System.out.println("'C'  Continue");
			System.out.println("'Q'  Quite");
			System.out.println("'P [router-id]'  Print the routing table of a router");
			System.out.println("'S [router-id]'  Shut down a router");
			System.out.println("'T [router-id]'  Start up a router");
			System.out.println("----------------------------------------------------------------------------------");
			String input = sc.nextLine();
			if(input==null||input.equals("")){
				System.out.println("Input error!");
				continue;
			}else if (input.toLowerCase().equals("c")) {
				for (Router r : routers) {
					r.originatePacket();
				}
				continue;
			}else if(input.toLowerCase().equals("q")){
				break;
			}else if(input.toLowerCase().charAt(0)=='p'){
				input = input.replace("p", "").replaceAll(" ","");
				
				for(Router r:routers){
					if(r.id==Integer.valueOf(input)){
						if(!r.start){
							System.out.println("Router is shut down");
							continue outer;
						}
						r.shortestPath();
						r.printRoutingTable();
					}	
				}
				continue;
			}else if(input.toLowerCase().charAt(0)=='s'){
				input = input.replace("s", "").replaceAll(" ","");
				for(Router r:routers){
					if(r.id==Integer.valueOf(input)){
						r.start=false;
					}
				}
				continue;
			}else if(input.toLowerCase().charAt(0)=='t'){
				input = input.replace("t", "").replaceAll(" ","");
				for(Router r:routers){
					if(r.id==Integer.valueOf(input)){
						r.start=true;
					}
				}
				continue;
			}else{
				System.out.println("Input error!");
			}
 		}

	}
}
