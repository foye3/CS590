package hw;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class TrieArticles_backup {

	public class Node {
		Node[] nextL;
		boolean isword;

		public Node() {
			this.nextL = null;
			this.isword = false;
		}
	}

	Node root; // Trie just have letters
	HashMap<String, String> nameMap; // key just have letters, value is fullname
	HashMap<String, Integer> countMap;
	int totalwords;

	public TrieArticles_backup() {
		nameMap = new HashMap<>();
		countMap = new HashMap<>();
		totalwords = 0;
	}

	public void add(String s) {
		if (root == null) {
			root = new Node();
		}
		Node p = root;
		for (int i = 0; i < s.length(); i++) {
			char a = s.charAt(i);
			int index = a > 90 ? a - 81 : a - 65;
			if (p.nextL == null) {
				p.nextL = new Node[52];
			}
			if (p.nextL[index] == null) {
				p.nextL[index] = new Node();
			}
			p = p.nextL[index];
		}
		p.isword = true;
	}

	public boolean contains(String s) {
		Node p = root;

		for (int i = 0; i < s.length(); i++) {
			char a = s.charAt(i);
			int index = a > 90 ? a - 81 : a - 65;
			if (p.nextL[index] == null) {
				// if(p.nextL[index]==null){
				return false;
			} else {
				p = p.nextL[index];
			}
		}
		if (p.isword) {
			return true;
		}
		return false;
	}

	public boolean containsPrefix(String s) {
		
		if (s == null || s.equals(""))
			return false;
		Node p = root;
		for (int i = 0; i < s.length(); i++) {
			char a = s.charAt(i);
			int index = a > 90 ? a - 81 : a - 65;
			if (p.nextL == null || p.nextL[index] == null) {
				return false;
			} else {
				p = p.nextL[index];
			}
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader brComp = new BufferedReader(new FileReader("src/companies.dat"));	//compaines.dat
		TrieArticles_backup ta = new TrieArticles_backup();
		String s;
		while ((s = brComp.readLine()) != null) {
			String[] compArr = s.split("	");
			for (int i = 0; i < compArr.length; i++) {
				String removeSym = compArr[i].replaceAll("[^a-zA-Z]", "");
				ta.nameMap.put(removeSym, compArr[0]);
				ta.add(removeSym);
			}
		}
		String input = "";
		Scanner sc = new Scanner(System.in);
		StringBuilder sb = new StringBuilder();
		while(!input.matches("\\.")){
			System.out.println("Please input article");
			input = sc.nextLine();
			sb.append(input+" ");
		}

		
		String[] artiArr = sb.toString().replaceAll("[^a-zA-Z ]", "").split(" ");
		for (int i = 0; i < artiArr.length; i++) {
//			boolean containNotcount = false;
			String search = artiArr[i];
			// a, an, the, and, or, but
			if ((!search.equals("")) && (!search.equals(" ")) && search != null && !search.equals("a") 
					&& !search.equals("an")&& !search.equals("the") && !search.equals("or") 
					&& !search.equals("and") && !search.equals("but") ){
				ta.totalwords++;
			}
			for (int j = i+1; j <= artiArr.length; j++) {
				if (ta.containsPrefix(search)) {
					if (ta.contains(search)) {
						if (ta.countMap.containsKey(search)) {
							ta.countMap.put(search, ta.countMap.get(search) + 1);

						} else {
							ta.countMap.put(search, 1);
						}
//						if (containNotcount) {
//							ta.totalwords++;
//						}
						//ta.totalwords -= j - i-1;
						break;
					}
					
//					if (artiArr[j].equals("a") && artiArr[j].equals("an") && artiArr[j].equals("the")
//							&& artiArr[j].equals("or") && artiArr[j].equals("and") && artiArr[j].equals("but")) {
//						containNotcount = true;
//					}
					//combine next
					search = search + artiArr[j];
				} else {
					break;

				}
			}
		}

		System.out.println(String.format("%-25s%-20s%-10s","Company","Hit Count","Relevance" ));
		int totalhit=0;
		float totalpercent=0;
		for (Entry<String, Integer> entry : ta.countMap.entrySet()) {
			float num = entry.getValue();
			System.out.println(String.format("%-25s%-20d%.5f%%",
					ta.nameMap.get(entry.getKey()),entry.getValue(),num/ta.totalwords*100 ));
			totalhit+=entry.getValue();
			totalpercent+=num/ta.totalwords*100;
		}
		System.out.println(String.format("%-25s%-20d%.5f%%","Total:",totalhit,totalpercent));
		System.out.println("Total Words:             "+ta.totalwords);
	}
}

