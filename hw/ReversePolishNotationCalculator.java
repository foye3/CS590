package hw;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class ReversePolishNotationCalculator {
	private Queue<String> infixQ = new LinkedList<>();
	private Queue<String> postfixQ = new LinkedList<>();
	private Stack<String> opStack = new Stack<>();
	private Stack<Double> eval = new Stack<>();
	private boolean inputcheck=true;
	
	
	public String translateToPostfix(String input){
		if(input.contains("p")){
			inputcheck = false;
			return "Input error: p";
		}
		input=input.replaceAll("POW", "p");		//use "p" for "POW"
		input=input.replaceAll(" ", "");
		StringTokenizer st = new StringTokenizer(input, "()+-*/%p",true);
		while(st.hasMoreTokens()){
			infixQ.add(st.nextToken());
		}
		while(!infixQ.isEmpty()){
			String t = infixQ.poll();
			if(isNumber(t)){
				postfixQ.add(t);
			}else if(isOperate(t)){
				if(opStack.isEmpty()){
					opStack.push(t);
				}else if(t.equals("(")){
					opStack.push(t);
				}else if(t.equals(")")){
					while(!opStack.peek().equals("(")){
						postfixQ.add(opStack.pop());
					}
					opStack.pop();	//discard a "(" from stack
				}else{
					while(!opStack.isEmpty()&&!opStack.peek().equals("(")
							&&comparePrecedence(t, opStack.peek())){
						postfixQ.add(opStack.pop());
					}
					opStack.push(t);
				}
			}else{
				inputcheck=false;
				return "Input error: "+t;
			}
				
		}
		while(!opStack.isEmpty()){
			postfixQ.add(opStack.pop());
		}
		
		StringBuilder sb = new StringBuilder();
		for(String s:postfixQ){
			sb.append(s+" ");
		}
		return sb.toString().replaceAll("p", "POW");
	}
	
	public double calculate(){
		double topnum,nextnum,answer = 0;
		while(!postfixQ.isEmpty()){
			String t = postfixQ.poll();
			if(isNumber(t)){
				eval.push(Double.valueOf(t));
			}else{
				topnum = eval.pop();
				nextnum = eval.pop();
				//System.out.println(topnum+","+nextnum);
				if(t.equals("+"))
					answer = nextnum + topnum;
				if(t.equals("-"))
					answer = nextnum - topnum;
				if(t.equals("*"))
					answer = nextnum * topnum;
				if(t.equals("/"))
					answer = nextnum / topnum;
				if(t.equals("%"))
					answer = nextnum % topnum;
				if(t.equals("p"))
					answer = Math.pow(nextnum, topnum);
				eval.push(answer);
			}
			
		}
		return answer;
	}
	
	//if op1's precedence <= op2 return true
	public boolean comparePrecedence(String op1,String op2){
		return getPrecedenceLevel(op1) <= getPrecedenceLevel(op2);
	}
	
	public int getPrecedenceLevel(String op){
		if(op.equals("+")||op.equals("-"))
			return 1;
		if(op.equals("*")||op.equals("/")||op.equals("%"))
			return 2;
		if(op.equals("p"))
			return 3;
		return -1;
	}
	
	private boolean isNumber(String num) {
		return num.matches("[\\d.]+");
	}
	
	private boolean isOperate(String op){
		return op.matches("[\\+\\-\\*\\/\\%\\(\\)p]");
	}
	
	public static void main(String[] args) {
		String input;
		while(true){
			System.out.println("Please input infix math problem or input quit.");
			Scanner sc = new Scanner(System.in);
			input = sc.nextLine();
			if(input.toLowerCase().equals("quit"))
				break;
			ReversePolishNotationCalculator rp = new ReversePolishNotationCalculator();
			System.out.println("postfix: "+rp.translateToPostfix(input));
			if(!rp.inputcheck) continue;
			double result = rp.calculate();
			DecimalFormat df = new DecimalFormat("#.########");
			System.out.println("result: "+Double.parseDouble(df.format(result)));
		}
	}
}
