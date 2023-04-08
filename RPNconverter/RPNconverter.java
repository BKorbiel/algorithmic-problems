import java.util.Scanner; 

/*
The program that converts RPN expresions to INF and vice versa.
*/

class Source{  
    public static Scanner sc = new Scanner(System.in);
    public static void main(String args[]){
        int z = sc.nextInt(); //number of expressions
        for (int i=0; i<z; i++) {
            String format = sc.next(); //reading the form of expression (inf or rpn)
            String expression = sc.nextLine();

            //INF:
            if (format.charAt(0)=='I') {
                System.out.print("RPN:");
                String correctedExpression = CorrectINF(expression); //deleting spaces, commas, etc.

                /*
                 Checking whether the expression is correct
                 */
                if (!isCorrectINF(correctedExpression)) {
                    System.out.print(" error\n"); 
                    continue;
                }

                //if the expression is correct it is converted to RPN
                ConvertToRPN(correctedExpression);
            } 
            //RPN:
            else {
                System.out.print("INF:");
                String correctedExpression = CorrectRPN(expression); //deleting spaces, commas, etc.

                //converting the expression to INF
                ConvertToINF(correctedExpression);
            }
        }
    }

    /*
     The function convers the expression from INF to RPN
     */
    static void ConvertToRPN(String expr) {
        OperatosStack stack = new OperatosStack(); //creating the stack for operators
        for (int i = 0; i < expr.length(); i++) { //iterating through expression characters
            char c = expr.charAt(i);
            if (Character.isLowerCase(c)) { //if the char is a variable it is outputed
                System.out.print(" " + c);
            } else if (c=='(') { //if the char is '(' it is pushed into the stack
                stack.push(c);
            } else if (c==')') {
                /*
                If the char is ')' then every operator from the stack is outputed until the operator is '('
                 */
                while ( stack.top() != '(') {
                    System.out.print(" " + stack.pop());
                }
                stack.pop();
            } else if (c=='!' || c=='~' || c=='^' || c=='=') { //the char is right-associative operator
                int priority = Priority(c);
                while (!stack.isEmpty() && Priority(stack.top())>priority) { //every operator with higher priority from the stack is outputed
                    System.out.print(" " + stack.pop());
                }
                stack.push(c); //pushing the operator into the stack
            } else { //the char is left-associative operator
                int priority = Priority(c);
                while (!stack.isEmpty() && Priority(stack.top())>=priority) { //every operator with higher or equal priority from the stack is outputed
                    System.out.print(" " + stack.pop());
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            System.out.print(" " + stack.pop()); //outputing the remaining operators
        }
        System.out.print("\n");
    }

    /*
     The function removes every incorrect character from the INF expression
     */
    static String CorrectINF(String expr) {
        String correctedExpression = "";
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (Character.isLowerCase(c)) { //variable
                correctedExpression+=c;
            }

            //operator
            if (c=='(' || c==')' || c=='!' || c=='~' || c=='^' || c=='*' || c=='/' || c=='%' || c=='+' || c=='-' || c=='<' || c=='>' || c=='?' || c=='&' || c=='|' || c=='=') {
                correctedExpression+=c;
            }
        }

        return correctedExpression;
    }

    /*
     The function checks whether the INF expression is correct
     */

    static Boolean isCorrectINF(String expr) {
        int state = 0; //initial state
        int brackets = 0; //brackets count (if we meet left bracket then it is incremented, if we meet right then it is decremented )
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (state == 0) {
                if (Character.isLowerCase(c)) { //transition to state 1
                    state = 1;
                } else if (c == '~' || c == '!') { //transition to state 2
                    state = 2;
                } else if (c != '(') { //incorrect char, return false
                    return false;
                } else { //left bracket
                    brackets += 1;
                }
            } else if (state == 1) {
                if (Character.isLowerCase(c) || c=='~' || c=='!' || c=='(') { //incorrect chars, return false
                    return false;
                } else if ( c != ')') { //transition to state 0
                    state = 0;
                } else {
                    brackets-=1; //right bracket
                    if (brackets<0) { //if at some point there is more right brackets than left brackets, then the expression is incorrect - return false
                        return false;
                    }
                }
            } else { //stan 2
                if (Character.isLowerCase(c)) { //transition to state 1
                    state = 1;
                } else if (c == '(') { //transition to state 0
                    state = 0;
                    brackets+=1; //left bracket
                } else if (c != '!' && c != '~') { //incorrect chars
                    return false;
                }

            }
        }

        return (brackets==0 && state==1);
    }

    /*
     The function converts RPN expression to INF
     */
    static void ConvertToINF(String expr) {
        ExpressionsStack stack = new ExpressionsStack(); //creating the stack of expressions
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (Character.isLowerCase(c)) {
                stack.push(" "+c, ' '); //pushing to the stack expression which is only one variable without operator (char ' ' as operator to know that there is no operator)
            } else if (c!='!' && c!='~') { //binary operator
                if (stack.size()<2) {
                    System.out.println(" error"); //if the stack size is less than 2 then the expression is incorrect
                    return;
                }
                int priority = Priority(c);
                String rightExpression = stack.topExpression(); //expression on the right side
                int rightPriority = Priority(stack.topOperator()); //priority of the right expression
                stack.pop();
                String leftExpression = stack.topExpression(); //expression on the left side
                int leftPriority = Priority(stack.topOperator()); //priority of the left expression
                stack.pop();
                String expression = ""; //creating new expression
                if (leftPriority < priority) { //if the left expression has lower priority then we add brackets to it
                    expression = " (" + leftExpression + " ) " + c;
                } else {
                    expression = leftExpression + " " + c;
                }
                
                if (rightPriority <= priority) {
                    /*
                    If the right expression has lower priority or if the priorities are equal and the operator is NOT right-associative then we add brackets to the right expression
                     */
                    if (rightPriority<priority || c!='^' && c!='=') {
                        expression += " (" + rightExpression + " )";
                    } else {
                        expression += rightExpression;
                    }
                } else {
                    expression += rightExpression;
                }

                stack.push(expression, c); //pushing the expression into the stack
            } else { //unary operator
                if (stack.size()<1) {
                    System.out.println(" error"); //if the stack size is less than 2 the expression is incorrect
                    return;
                }
                int priority = Priority(c);
                String rightExpression = stack.topExpression();
                int rightPriority = Priority(stack.topOperator());
                stack.pop();
                String expression = "";
                if (rightPriority<priority) { //if the expression on the right side has lower priority then we add brackets to it
                    expression = " " + c + " (" + rightExpression + " )";
                } else {
                    expression = " " + c + rightExpression;
                }
                stack.push(expression, c); //pushing the expression into the stack
            }
        }
        if (stack.size()!=1) { //if at this point the size of the stack is not 1 then the expression is incorrect
            System.out.println(" error");
            return;
        }
        System.out.println(stack.topExpression());
    }

    /*
     The function removes incorrect characters from the RPN expression
     */
    static String CorrectRPN(String expr) {
        String correctedExpression = "";
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (Character.isLowerCase(c)) {
                correctedExpression+=c;
            }
            if (c=='!' || c=='~' || c=='^' || c=='*' || c=='/' || c=='%' || c=='+' || c=='-' || c=='<' || c=='>' || c=='?' || c=='&' || c=='|' || c=='=') {
                correctedExpression+=c;
            }
        }

        return correctedExpression;
    }
    /*
     The function returns the priority of the operator
     */
    static int Priority(char operator) {
        if (operator == '=') {
            return 0;
        }
        if (operator == '|') {
            return 1;
        }

        if (operator == '&') {
            return 2;
        }

        if (operator == '?') {
            return 3;
        }

        if (operator == '<' || operator == '>') {
            return 4;
        }

        if (operator == '+' || operator=='-') {
            return 5;
        }

        if (operator == '*' || operator=='/' || operator == '%') {
            return 6;
        }

        if (operator == '^') {
            return 7;
        }

        if (operator == '!' || operator == '~') {
            return 8;
        }

        if (operator == '(' || operator==')') { //for the INF->RPN convertion
            return -1;
        }

        if (operator == ' ') { //for expressions without operator (in the RPN->INF convertion)
            return 10;
        }

        return -2;
    }
}

/*
 A "double" stack that stores expressions as strings and their main operator.
 Used for conversion from RPN to INF.
 */
class ExpressionsStack{
    private String[] Expressions; // table that contains expressions
    private char[] Operators; // table that contains operators
    private int t; // index of the top of the stack

    public ExpressionsStack() { // constructor
        //creating tables
        Expressions = new String[256];
        Operators = new char[256]; 
        t = 256; // initial index - the stack is empty
    }

    public void push(String expression, char operator) {
        t-=1;
        Operators[t] = operator;
        Expressions[t] = expression;
    }

    public void pop(){ // removes element
        t++;
    }

    public String topExpression(){ // returns the top expression
        return Expressions[t];
    }
    public char topOperator(){ // return the priority of the top expression
        return Operators[t];
    }

    public boolean isEmpty(){
        return (t == 256);
    }

    public int size() {
        return 256-t;
    }
 }

 /*
  The stack that stores operators. 
  Used for INF->RPN conversion
  */
 class OperatosStack{
    private char[] Operators; // table of the operators
    private int t; // index of the top of the stack

    public OperatosStack() { // constructor
        Operators = new char[256]; // creating the table
        t = 256; // initial index
    }

    public void push(char operator) {
        t-=1;
        Operators[t] = operator;
    }


    public char pop(){ // removes the top of the stack and returns it
        return Operators[t ++];
    }

    public char top(){ // return the operator on the top of the stack 
        return Operators[t];
    }

    public boolean isEmpty(){
        return (t == 256);
    }

 }