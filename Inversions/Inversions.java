import java.util.Scanner; 

/*
The program counts number of inversions in array by recursion and merge sort.
*/

public class Inversions {
    static long data[];
    public static Scanner sc = new Scanner(System.in);
    public static void main(String args[]){
        //inputing data
        int z = sc.nextInt(); //number of data sets
        for (int i = 0; i < z; i++) {
            int length = sc.nextInt(); //length of array
            data = new long[length]; //array
            for (int j = 0; j < length; j++) {
                data[j] = sc.nextInt();
            }
            System.out.println(Inversion(0, length-1)); //outputing number of inversions
        }
    }
    /*
    The function counts number of inversions by merge sort.
    In the arguments, it accepts the index of the left and right ends of the array range for which it is to count the number of inversions.
     */
    public static int Inversion(int left, int right) {
        //if index of the left end of the array range is greater or equal to index of the right end then number of inversions is 0
        if (left >= right) {
            return 0;
        }

        int result = 0; //stores number of inversions in this range of array
        int mid = (left+right)/2; //the middle of the range
        result += Inversion(left, mid); //recursive call for the left half of this interval
        result += Inversion(mid+1, right); //recursive call for the right half of this interval

        long[] helper = new long[right - left + 1]; //auxiliary array
        for (int i = 0; i < right-left+1; i++) {
            helper[i] = data[left + i]; //coping data to auxiliary array
        }

        /*
        Sorting this range of array by merging left and right halfs of the auxiliary array.
        
        indecies: 
        i - index for the left half of helper[], 
        j - index for the right half of helper[], 
        k - index for this range in data[] (to insert sorted elements)
         */
        int i = 0, j = mid + 1 - left, k = left; 
        while (i < mid + 1 - left && j < right - left + 1) {
            if (helper[i] <= helper[j])
                data[k++] = helper[i++]; //if the number in the left half is smaller, we put it under the current index in data[] and increase the indices
            else {
                data[k++] = helper[j++]; //if the number in the right half is smaller, we put it under the current index in data[] and increase the indices
                /*
                 When the number in the right half is greater than the number in the left half, we have found an inversion, 
                 the amount of which is the index of the beginning of the right half minus the index of the current number 
                 in the left half, i.e. (mid + 1) - (left + i)
                 */
                result += (mid + 1) - (left + i);
            }
        }
        /*
        Inserting to data[] left numbers
         */
        while (i < mid + 1 - left)
            data[k++] = helper[i++];
        while (j < right - left + 1)
            data[k++] = helper[j++];
        return result;
    }
}