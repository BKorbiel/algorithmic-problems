import java.util.Scanner;
 
/*
Program which solves the Subset Sum Problem with two methods - recursive function and iterative function which is 
a simulation of the recursive function using stack.
 */

class Source{  
    public static Scanner sc = new Scanner(System.in);
    public static int k; //number of elements
    public static int n; //target sum
    public static int elements[]; //elements
    public static int result[]; //array of elements as result
    public static int resultIndex; //index to invers elements to result[]

    //variables necessary for iterative simulation of recursive function
    public static int label; //step of the program
    public static Stack S; //parameters stack
    public static Params Par; //current parameters
    public static Boolean solutionFound; //variable stores info wether the solution is already found
    public static void main(String args[]){
        int z = sc.nextInt(); //number of data sets
        for (int i = 0; i < z; i++) {
            //inputing data
            n = sc.nextInt();
            k = sc.nextInt();
            elements = new int[k];
            for (int j = 0; j < k; j++) {
                elements[j] = sc.nextInt();
            }
            resultIndex = 0;

            if (rec_find(n, 0, 0) == true) { //recursive method
                System.out.print("REC: " + n + " ="); //outputing result
                for (int j = resultIndex - 1; j >= 0; j--) {
                    System.out.print(" " + result[j]); //outputing elements in the correct order
                }
                System.out.print("\n");
                resultIndex = 0;
                iter_find(); //iterative simulation of recursive function
                System.out.print("ITER: " + n + " ="); //wypisywanie wyniku
                for (int j = resultIndex - 1; j >= 0; j--) {
                    System.out.print(" " + result[j]); //outputing elements in the correct order
                }
                System.out.print("\n");
            } else {
                System.out.println("NONE"); //there is no solution
            }
        }
    }

    /*
    Recursive method
    There are two recursive calls in it - one for the case when current element is taken to the sum, 
    and the other for the case when we skip the element. 
    Arguments: leftSum - remaining sum (i.e., initial sum minus the values of taken elements), 
    elemIndex - the index of the currently examined element in the array of elements, 
    elementsCount - the number of taken elements
     */
    public static Boolean rec_find(int leftSum, int elemIndex, int elementsCount) {
        if (leftSum == 0) { //solution is found, return true
            result = new int[elementsCount]; //creating the result table
            return true; 
        }
        if (elemIndex >= k || leftSum < 0) { //incorrect solution, return false
            return false; 
        }

        /*
         Below is a recursive call where the current element is being taken. 
         The value of current element is substracted from the remaining sum, the index of current element is 
         increased by 1, and the number of taken elements is also increased by 1.
         */
        if ( rec_find(leftSum - elements[elemIndex], elemIndex + 1, elementsCount + 1) == true) {
            //solution is found so we insert the current element into the result table and return true
            result[resultIndex++] = elements[elemIndex]; 
            return true;
        }
        /*
        Below is a recursive call where the current element is not being taken.
         */
        if ( rec_find(leftSum, elemIndex + 1, elementsCount) == true) {
            return true; //solution is found, so return true;
        }

        return false; //if both cases are incorrect then return false
    }

    /*
    Iterative method that simulates the above recursive method.
     */
    public static void iter_find() {
        S = new Stack(); //creating the stack of parameters
        solutionFound = false; //solution is not found yet
        label = 1; //first step
        while (step() == false);
    }

    public static Boolean step() {
        switch(label) {
            case 1:
                Par = new Params(n, 0, 0, 6); //inserting the first Parameters element into the stack
                S.push(Par);
                label = 2; //go to step 2
                break;
            case 2:
                Par = S.top(); //getting current parameters

                //checking the boundary conditions
                if (Par.leftSum == 0) { //correct solution is found
                    result = new int[Par.elementsCount]; //creating the result table
                    solutionFound = true;
                    label = 5; //go to step 5
                } else if (Par.elemIndex >= k || Par.leftSum < 0) { 
                    //incorrect solution
                    label = 5; //go to step 5
                }  else { 
                    // boundary conditions do not hold - go to step 3
                    label = 3;
                }
                break;
            case 3:
                //simulation of the first recursive call where we take the current element
                Params nextParams = new Params(Par.leftSum - elements[Par.elemIndex], Par.elemIndex + 1, Par.elementsCount + 1, 4);
                S.push(nextParams); //pushing new params to the stack
                label = 2; //go to step 2
                break;
            case 4:
                Par = S.top(); //getting current params
                if (solutionFound) { //rozwiazanie jest juz znalezione i zaklada ono ze wzielismy aktualny przedmiot, zatem dopisujemy go do tablicy wynikow
                    //solution is already found and it assumes that the current element is taken, so it is inserted into the result table.
                    result[resultIndex++] = elements[Par.elemIndex];
                    label = 5; //go to step 5
                } else {
                    //simulation of the second recursive call where we skip the current element and it is not being taken.
                    //return address is 5, because it doesn't matter whether we find a solution or not - this item will not be inserted into the result table anyway
                    Params newParams = new Params(Par.leftSum, Par.elemIndex + 1, Par.elementsCount, 5);
                    S.push(newParams);
                    label = 2; //go to step 2
                }
                break;
            case 5:
                Par = S.pop();
                label = Par.addr; //go to step 4, 5 or 6
                break;
            case 6:
                return true;
                
        }
        return false;
    }
}

/*
Stack that stores the parameters of the simulated function calls
 */
class Stack{ 
    public Params first; //first element

    public Stack() {
        first = null; //0 elements
    }

    public void push(Params p) { //function pushes element into the stack
        p.next = first;
        first = p;
    }

    public Params top() { //function returns the first element
        return first;
    }

    public Params pop() { //function removes the first element from the stack
        Params p = first; 
        first = first.next; 
        return p;
    }
}

/*
Class for the parameters of the function calls.
 */
class Params{
    public Params next; //next element
    public int leftSum;
    public int elemIndex;
    public int elementsCount; 
    public int addr; //return address

    public Params(int leftSum, int elemIndex, int elementsCount, int addr) {
        this.leftSum = leftSum;
        this.elemIndex = elemIndex;
        this.elementsCount = elementsCount;
        this.addr = addr;
    }
}