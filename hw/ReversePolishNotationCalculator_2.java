package hw;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class ReversePolishNotationCalculator_2 {
	// stroe char of postfix
	Queue<Character> postQ = new LinkedList<>();
	// stor num of postfix
	Queue<Integer> postQnum = new LinkedList<>();

	public long caculator() {
		Stack<Integer> eval = new Stack<>();
		char t;
		int topNum, nextNum, answer = 0;
		while (!postQ.isEmpty()) {
			t = postQ.poll();
			// is number
			if (t == 'n') {
				eval.push(postQnum.poll());
			} else {
				topNum = eval.pop();
				nextNum = eval.pop();
				switch (t) {
				case '+':
					answer = nextNum + topNum;
					break;
				case '-':
					answer = nextNum - topNum;
					break;
				case '*':
					answer = nextNum * topNum;
					break;
				case '/':
					answer = nextNum / topNum;
					break;
				case '%':
					answer = nextNum % topNum;
					break;
				}
				eval.push(answer);
			}
		}
		return eval.pop();
	}

	public String translator(String infix) {
		Stack<Character> opStack = new Stack<>();
		// store char of infix
		Queue<Character> infixQ = new LinkedList<>();
		// store number of infix
		Queue<Integer> infixQnum = new LinkedList<>();
		// push infix into infixQ
		// if is number add 'n' to infixQ, add val to infixQNum
		int num = 0;
		for (int i = 0; i < infix.length(); i++) {
			char a = infix.charAt(i);
			if (a <= 57 && a >= 48) { // is number
				num += a - 48; // char to int
				if (i != infix.length() - 1) {
					char b = infix.charAt(i + 1);
					if (b <= 57 && b >= 48) { // next char is number
						num *= 10;
						continue;
					}
				}
				infixQ.add('n');
				infixQnum.add(num);
				num = 0;
			} else {
				infixQ.add(infix.charAt(i));
			}
		}

		char t;
		while (!infixQ.isEmpty()) {
			t = infixQ.poll();
			if (t == 'n') { // is number
				postQ.add('n');
				postQnum.add(infixQnum.poll());
			} else if (opStack.isEmpty()) {
				opStack.push(t);
			} else if (t == '(') {
				opStack.push(t);
			} else if (t == ')') {
				while (opStack.peek() != '(') {
					postQ.add(opStack.pop());
				}
				opStack.pop(); // discard a '(' from stack
			} else {
				String pre = "+-       %*/";
				// if sub<3 then precedence of t <= predence of opStack.peek()
				int sub = pre.indexOf(t) - pre.indexOf(opStack.peek());
				while (!opStack.isEmpty() && opStack.peek() != '(' && (sub < 3)) {
					postQ.add(opStack.pop());
				}
				opStack.push(t);
			}
		}
		StringBuilder postfix = new StringBuilder();
		while (!opStack.isEmpty()) {
			postQ.add(opStack.pop());
		}
		int count = postQ.size();
		for (int i = 0; i < count; i++) {
			char a = postQ.poll();
			if (a == 'n') {
				postQ.add(a);
				postQnum.add(postQnum.peek());
				postfix.append(postQnum.poll().toString() + " ");
			} else {
				postQ.add(a);
				postfix.append(a + " ");
			}

		}
		return postfix.toString();
	}

	public static void main(String[] args) {
		while (true) {
			System.out.println("Please input infix math problem or input quit");
			Scanner sc = new Scanner(System.in);
			String infix = sc.nextLine();
			if ("quit".equals(infix.toLowerCase()))
				break;
			ReversePolishNotationCalculator_2 rc = new ReversePolishNotationCalculator_2();
			String postfix = rc.translator(infix.replaceAll(" ", ""));
			System.out.println("The problem in postfix is:" + postfix);
			System.out.println("Result:" + rc.caculator());
		}
	}
}
