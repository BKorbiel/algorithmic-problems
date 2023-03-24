import java.util.Scanner; 

/*
A program that finds the maximum 2D subarray. 
The idea is to apply the Kadane's algorithm to every possible range of rows and return the highest result.
 */
class Source{  
    public static Scanner sc = new Scanner(System.in);
    public static void main(String args[]){ 
        int z = sc.nextInt();
        for (int h=0; h<z; h++) {

            //loading input
            int nr = sc.nextInt();
            String d = sc.next(); //colon
            int rows = sc.nextInt();
            int columns = sc.nextInt();

            /*
            The values of the elements in the following array will be equal to the 
            sum of the values of the elements (from the array in the task) located 
            in the same column, starting from this element upwards.
             */
            int [][]a = new int [rows][columns]; //creating the 2D array
            for (int i=0; i<rows; i++) {
                for (int j=0; j<columns; j++) {
                    a[i][j] = sc.nextInt(); //loading data to the array

                    if (i!=0) {
                        a[i][j]+=a[i-1][j]; 
                        //If this is not the first row, we add the value of the cell from the same column in the row above to this cell.
                    }
                }
            }

            //Setting the initial values of variables.
            int result = 0;
            int res_i=-1, res_j=-10,  res_k=-1,  res_l=-10; 
            //Negative initial indexes allow us to determine later whether the maximum subarray does even exist.

            for (int bottom = 0; bottom<rows; bottom++) {
                for (int top = bottom; top<rows; top++) { //Iterating through all possible ranges of rows.

                    int[] helper = new int[columns];
                    /*
                    Creating an auxiliary table
                    
                    In the "helper" table, the cell at index "i" represents the sum of the elements in column number "i" 
                    from the row with index "bottom" to the row with index "top", inclusive.
                    */
                    for (int col=0; col<columns; col++) {
                        helper[col] = a[top][col];
                        if (bottom!=0) {
                            helper[col] -= a[bottom-1][col];
                        }
                    }

                    //We set the value of 'max' and the index 'k' to 0
                    int currentMax = 0;
                    int currentK = 0;

                    //Kadane's algorithm
                    for (int i=0; i<columns; i++) {
                        if (currentMax<=0) {
                            currentMax=0;
                            currentK = i; 
                            /*
                            If 'max' is less than or equal to 0, we set the value of index 'k' to be the same as the 
                            column number we are currently in (since the subarray should have the least number of elements possible)
                            */
                        }
                        currentMax+=helper[i];
                        if (currentMax>=result) {

                            //Checking which subarray has more elements.
                            int numberOfElements = (top-bottom+1)*(i-currentK+1);
                            int prevNumberOfElements = (res_j-res_i+1)*(res_l-res_k+1);

                            /*
                             If the currently computed sum of elements is greater than the previous max, 
                             or if they are equal but this sum has fewer elements, we set this sum as the new max.
                             */
                            if (currentMax>result || (numberOfElements<prevNumberOfElements)) { 
                                result = currentMax;
                                res_i = bottom;
                                res_j = top;
                                res_k = currentK;
                                res_l = i;
                            }
                        }
                    }
                }
            }
            //outputting the result
            if (res_i==-1) { //maximum subarray doesn't exist, because the index is negative
                System.out.println(nr+": n = "+rows+" m = "+columns+", ms = 0, mst is empty");
            }
            else {
                System.out.println(nr+": n = "+rows+" m = "+columns+", ms = " + result + ", mst = a["+res_i+".."+res_j+"]["+res_k+".."+res_l+"]");
            }
        }
    }  
}  

/*
Tests:

Input:
 7
1 : 3 3
-1 -1 -1
-1 -1 -1
-1 -1 0
2 : 1 1
-1
3 : 1 1
1
4 : 1 1
0
5 : 3 3
-1 -1 -1
1 -1 2
-1 -1 -1
6 : 3 3
-1 2 -1
-1 -1 -1
-1 2 -1
7 : 2 2
0 0
-1 1

Output:
1: n = 3 m = 3, ms = 0, mst = a[2..2][2..2]
2: n = 1 m = 1, ms = 0, mst is empty
3: n = 1 m = 1, ms = 1, mst = a[0..0][0..0]
4: n = 1 m = 1, ms = 0, mst = a[0..0][0..0]
5: n = 3 m = 3, ms = 2, mst = a[1..1][2..2]
6: n = 3 m = 3, ms = 3, mst = a[0..2][1..1]
7: n = 2 m = 2, ms = 1, mst = a[1..1][1..1]



Input:
6
1 : 1 6
-2 7 -4 8 -5 4
2 : 2 5
1 1 -1 -1 0
1 1 -1 -1 4
3 : 2 5
0 -1 -1 1 1
4 -2 -2 1 1
4 : 2 5
0 -1 -1 4 0
4 -2 -2 0 0
5 : 2 5
-1 -2 -3 -1 -2
-1 -1 -1 -1 -5
6 : 2 5
0 0 0 0 0
0 0 0 0 0

Output:
1: n = 1 m = 6, ms = 11, mst = a[0..0][1..3]
2: n = 2 m = 5, ms = 4, mst = a[1..1][4..4]
3: n = 2 m = 5, ms = 4, mst = a[1..1][0..0]
4: n = 2 m = 5, ms = 4, mst = a[0..0][3..3]
5: n = 2 m = 5, ms = 0, mst is empty
6: n = 2 m = 5, ms = 0, mst = a[0..0][0..0]
 */