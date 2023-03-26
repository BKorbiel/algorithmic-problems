import java.util.Scanner; 

/*
 * Program that uses binary search to answer the question of how many numbers from a given 
 * ordered set of data are contained in a certain range.
 */

class Source{
    public static Scanner sc = new Scanner(System.in);
    static int count; 
    static long data[];
    public static void main(String args[]){ 
        int z = sc.nextInt();
        for (int h = 0; h < z; h++) {

            //input
            count = sc.nextInt(); //size of the data set
            data = new long[count];

            for (int i = 0; i < count; i++) {
                data[i] = sc.nextLong(); //loading numbers into a table
            }

            int queryCount = sc.nextInt();
            for (int i = 0; i < queryCount; i++) {
                long min = sc.nextLong();
                long max = sc.nextLong();

                /*
                    The program finds the first position of the number one greater than the upper end of the range, 
                    and then subtracts the first position of the number from the lower end of the range. The result 
                    of this operation is count of the numbers in the range.
                */
                int result = SearchBinFirst(max+1) - SearchBinFirst(min);

                if (result < 0) { //The result can't be less than 0.
                    result = 0;
                }
                System.out.println(result);
            }
            //outputing the number of diffrent numbers in data set
            System.out.println(Count());
        }
    } 
    
    /*
     Function that uses binary seatch to find the position of the first apperearance of the given number.
     If the number is not in the data set then the function returns position where the number would be if it were 
     inserted into the table.
    */
    static int SearchBinFirst(long x) {
        //Initial values.
        int bottom = 0;
        int top = count-1;
        int mid = 0;

        //Binary search.
        while (bottom < top) {
            mid = (top + bottom) / 2;
            if (data[mid] >= x) {
                top = mid; //if data[mid]>=x then the result is for sure less or equal to "mid"
            } else {
                bottom = mid + 1; //if data[mid]<x then the result is for sure greater than "mid"
            }
        }

        /*
        If data[bottom]<x then x is greater than every number in the table, so function 
        has to return last index + 1, which is equal to the size of the table.
        */
        if (data[bottom] < x) {
            return count;
        }

        //returning index 'bottom' as a result
        return bottom;
    }

    /*
    Function that return the number of diffrent numbers in the data set.
    */
    static int Count() {
        int result = 1; //the result is for sure greater than 0, because the size of the data set is greater than 0
        for (int i = 1; i < count; i++) {
            if (data[i] != data[i-1]) { //if two consecutive numbers are different then the result is increased by 1
                result+=1;
            }
        }
        return result;
    }
}  