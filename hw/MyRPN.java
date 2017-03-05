package hw;

import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class MyRPN {
	
    private static Scanner input = new Scanner(System.in);
    
    private static boolean isOperator(char c) { // Tell whether c is an operator.

        return c == '+'  ||  c == '-'  ||  c == '*'  ||  c == '/'  ||  c == '^'
              || c=='(' || c==')'|| c == '%';
      
      }
    
    private static boolean isSpace(char c) {  // Tell whether c is a space.

        return (c == ' ');
      
      }
    
    private static boolean lowerPrecedence(char op1, char op2) {
        // Tell whether op1 has lower precedence than op2, where op1 is an
        // operator on the left and op2 is an operator on the right.
        // op1 and op2 are assumed to be operator characters (+,-,*,/,^).
        
        switch (op1) {

           case '+':
           case '-':
              return !(op2=='+' || op2=='-') ;

           case '*':
           case '/':
           case '%':
              return op2=='^' || op2=='(';

           case '^':
              return op2=='(';

           case '(': return true;

           default:  // (shouldn't happen)
              return false;
        }
   
     }
    
    public static String convertToPostfix(String infix) {
        // Return a postfix representation of the expression in infix.

       Stack operatorStack = new Stack();  // the stack of operators

       char c;       // the first character of a token
     
       StringTokenizer parser = new StringTokenizer(infix,"+-*/^%() ",true);
                     // StringTokenizer for the input string

       StringBuffer postfix = new StringBuffer(infix.length());  // result
   
       // Process the tokens.
          while (parser.hasMoreTokens()) {     
                                              
             String token = parser.nextToken();          // get the next token
                                                               // and let c be
             c = token.charAt(0);         // the first character of this token
     
             if ( (token.length() == 1) && isOperator(c) ) {    // if token is
                                                               //  an operator
       
                while (!operatorStack.empty() &&
                    !lowerPrecedence(((String)operatorStack.peek()).charAt(0), c))
                   // (Operator on the stack does not have lower precedence, so
                   //  it goes before this one.)
               
                   postfix.append(" ").append((String)operatorStack.pop());

                if (c==')') {
                   // Output the remaining operators in the parenthesized part.
                      String operator = (String)operatorStack.pop();
                      while (operator.charAt(0)!='(') {
                         postfix.append(" ").append(operator);
                         operator = (String)operatorStack.pop();  
                      }
                }
                else
                   operatorStack.push(token);// Push this operator onto the stack.
     
             }
             else if ( (token.length() == 1) && isSpace(c) ) {    // else if
                                                          // token was a space
               ;                                                  // ignore it
             }
             else {  // (it is an operand)
               postfix.append(" ").append(token);  // output the operand
             }//end if
   
           }// end while for tokens
   
       // Output the remaining operators on the stack.
          while (!operatorStack.empty())
             postfix.append(" ").append((String)operatorStack.pop());
       
       // Return the result.

          return postfix.toString();


     }//end convertToPostfix
    
    
    
    public static void calculate(String expr){
        Stack<Double> stack = new Stack<>();

        System.out.println(expr);
//        System.out.println("Input\tOperation\tStack after");

        for (String token : expr.split("\\s+")) {
//            System.out.print(token + "\t");
            switch (token) {
                case "+":
//                    System.out.print("Operate\t\t");
                    stack.push(stack.pop() + stack.pop());
                    break;
                case "-":
//                    System.out.print("Operate\t\t");
                    stack.push(-stack.pop() + stack.pop());
                    break;
                case "*":
//                    System.out.print("Operate\t\t");
                    stack.push(stack.pop() * stack.pop());
                    break;
                case "/":
//                    System.out.print("Operate\t\t");
                    double divisor = stack.pop();
                    stack.push(stack.pop() / divisor);
                    break;
                case "%":
//                  System.out.print("Operate\t\t");
                  double remainder = stack.pop();
                  stack.push(stack.pop() % remainder);
                  break;
                case "^":
//                    System.out.print("Operate\t\t");
                    double exponent = stack.pop();
                    stack.push(Math.pow(stack.pop(), exponent));
                    break;
                default:
//                    System.out.print("Push\t\t");
                    stack.push(Double.parseDouble(token));
                    break;
            }

//            System.out.println(stack);
        }

        System.out.println("Final Answer: " + stack.pop());
    }
    
    public static void main(String[] args) {
    	@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
    	String numOrOperand = " ";
    	System.out.println("Example input: (1+2)%3*9/3");
        while (true) {
        	System.out.println("Enter your problem and quit to end: ");
            numOrOperand = input.nextLine();
            if(numOrOperand.equals("quit"))
            {
            	break;
            }
            String postfix = convertToPostfix(numOrOperand);
        	if(postfix.charAt(0) == ' '){
        		postfix = postfix.substring(1);
        	}
        	calculate(postfix);
        }
    	
    	
    }
}
