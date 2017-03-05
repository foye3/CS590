package hw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class HuffmanCodes {

	public class Node implements Comparable<Node> {
		private String chars;
		private int freq;
		private Node left, right, parent;

		@Override
		public int compareTo(Node node) {
			return freq - node.freq;
		}
	}
	
	public void huffmanCodes(String s) throws IOException {
		
		int total = s.length();
		TreeMap<Character, Integer> tm = new TreeMap<>();
		for (int i = 0; i < total; i++) {
			char a = s.charAt(i);
			if (tm.containsKey(a))
				tm.put(a, tm.get(a) + 1);
			else
				tm.put(a, 1);
		}
		List<Map.Entry<Character, Integer>> mlist = new ArrayList<>(tm.entrySet());
		Comparator<Map.Entry<Character, Integer>> compare = new Comparator<Map.Entry<Character, Integer>>() {
			public int compare(Entry<Character, Integer> o1, Entry<Character, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		};
		Collections.sort(mlist, compare);
		PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
		List<Node> leafs = new LinkedList<>();
		for (Map.Entry<Character, Integer> entry : mlist) {
			Node node = new Node();
			node.chars = entry.getKey().toString();
			node.freq = entry.getValue();
			priorityQueue.add(node);
			leafs.add(node);
		}

		int size = priorityQueue.size();
		for (int i = 0; i < size - 1; i++) {
			Node node1 = priorityQueue.poll();
			Node node2 = priorityQueue.poll();
			Node parentNode = new Node();
			parentNode.chars = node1.chars + node2.chars;
			parentNode.freq = node1.freq + node2.freq;
			parentNode.left = node1;
			parentNode.right = node2;
			node1.parent = parentNode;
			node2.parent = parentNode;
			priorityQueue.add(parentNode);

		}

		File file = new File("src/hw/outfile.dat");
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write("Letter    Frequency    HuffmanCode\n");

		int totallength = 0;
		for (Node leafnode : leafs) {
			char a = leafnode.chars.charAt(0);
			String code = "";
			Node currentNode = leafnode;
			while (currentNode.parent != null) {
				if (currentNode.parent != null && currentNode == currentNode.parent.left) {
					code = "0" + code;
				} else {
					code = "1" + code;
				}
				currentNode = currentNode.parent;
			}
			float f = tm.get(a);
			String output = "  " + a + "        " + String.format("%-5.2f", f / total * 100) + "%        "
					+ String.format("%-15s", code) + "\n";
			bw.write(output);

			totallength += code.length() * tm.get(a);
		}

		bw.write("\nTotal Length: " + totallength);
		bw.close();
	}

	public static void main(String[] args) throws IOException {
		HuffmanCodes hc = new HuffmanCodes();
		BufferedReader br = new BufferedReader(new FileReader("src/hw/infile.dat"));
		String s;
		StringBuffer sb = new StringBuffer();
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		br.close();
		hc.huffmanCodes(sb.toString().replaceAll("[^a-zA-Z0-9]", ""));
		
	}
}
