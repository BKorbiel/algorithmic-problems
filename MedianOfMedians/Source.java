import java.util.Scanner;

/*
 A program that uses the median of medians algorithm to find the k-th largest number in a dataset. 
 The algorithm has a constant memory complexity and linear time complexity and operates recursively. 
 I used the ShellSort algorithm for sorting.
 */
public class Source {
    public static Scanner sc = new Scanner(System.in);
    public static int dataSize;
    public static int data[];
    public static int queriesCount;
    public static int query;
    public static void main(String[] args) {
        int z = sc.nextInt();
        for (int i = 0; i < z; i++) {
            dataSize = sc.nextInt();
            data = new int[dataSize];
            for (int j = 0; j < dataSize; j++) {
                data[j] = sc.nextInt();
            }

            //inputing queries
            queriesCount = sc.nextInt();
            for (int j = 0; j < queriesCount; j++) {
                query = sc.nextInt();
                if ( query > 0 && query <= dataSize) { //checking if the query is correct
                    System.out.println(query + " " + Select(query, dataSize)); //query is correct, outputing answer
                } else {
                    //query incorrect
                    System.out.println(query + " none");
                }
            }
        }
    }

    /*
    The median of medians algorithm finds the k-th largest number in the data[] array.
    The elements that need to be processed are stored at the beginning of the array, which gives the 
    algorithm constant memory complexity as it doesn't use additional arrays.
    It takes the value of k as an argument, indicating the element to be found, and the length of the 
    currently processed part of the array.
    The recursive equation for time complexity is as follows:
    T(n) = c1 for n = 1;
    T(n) <= T(n/5) + T(3n/4) + c2*n for n > 1;
    (one subproblem of size n/5, one subproblem of size up to 3n/4, and sorting of five-element groups)
    Therefore, the complexity is linear because 1/5 + 3/4 < 1
     */
    public static int Select(int k, int length) {
        if (length == 1) { //this part of the array has only one element, so it is returned
            return data[0];
        }

        /*
         Below is the division of the currently processed part of the array into groups of five elements, 
         followed by sorting those groups and taking their medians (which correspond to the element at 
         index size/2 in the sorted group).
         Then, the medians are stored at the beginning of the data[] array to avoid creating additional memory.
         */
        for (int i = 0; i < (length + 4) / 5; i++) {
            int start = i * 5;
            int end = Math.min(start + 4, length - 1);
            int size = end - start + 1;

            sort(start, start + size);
            swap(i, start + size / 2);
        }
        
        /* 
        Recursive call, calculating the median of medians.
        The number of elements in the median group is (length + 4) / 5,
        and their median will be the middle element (i.e., the one with the index (length + 4) / 10 in order).
        */
        int medianOfMedians = Select((length + 4) / 10, (length + 4) / 5);

        int smaller = 0; //number of elements smaller than median of medians
        int greater = 0; //number of elements greater than median of medians
        int equal = 0; //number of elements equal to median of medians
        for (int i = 0; i < length; i++) {
            /*
             The loop counts the number of elements that are smaller, larger, and equal to the median of medians.
             */
            int num = data[i];
            if (num < medianOfMedians) {
                smaller++;
            } else if (num > medianOfMedians) {
                greater++;
            } else {
                equal++;
            }
        }

        if (smaller >= k) {
            //The desired element is located in the group of elements that are smaller than the median of medians.
            int idx = 0; //current index
            for (int i = 0; i < length; i++) {
                if (data[i] < medianOfMedians) { 
                    //The element is smaller than the median of medians, so we insert it at index idx (the beginning of the array).
                    swap(i, idx);
                    idx++;
                }
            }
            /*
            Recursive call, the number of elements in the interval is equal to the variable 'smaller', 
            and the index of the element remains the same
            */
            return Select(k, smaller);
        } else if (smaller + equal >= k) {
            //The desired element is located in the group of elements that are equal to the median of medians, so the median is returned
            return medianOfMedians;
        } else {
            //The desired element is located in the group of elements that are greater than the median of medians.
            int idx = 0; //current index
            for (int i = 0; i < length; i++) { 
                if (data[i] > medianOfMedians) {
                    //The element is greater than the median of medians, so we insert it at index idx (the beginning of the array).
                    swap(i, idx);
                    idx++;
                }
            }
            /*
            Recursive call, the number of elements in the interval is equal to the variable 'greater', 
            and the index of the element should be reduced by the values of 'smaller' and 'equal'
             */
            return Select(k - smaller - equal, greater);
        }
    } 

    public static void swap(int first, int second) {
        int temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    /*
     Sorting a portion of the data[] array using the ShellSort algorithm.
    It takes the start and end indexes of the interval in the array to be sorted as arguments.
    The complexity ranges from O(n^(7/6)) to O(n^(3/2)).
     */
    public static void sort(int begin, int end) {
        int j, k; //indiecies
        int n = end - begin; //number of elements
        int tmp;
        int h = 1; //the starting value of h
        while(h <= n/3)
            h = h*3 + 1;
        while(h>0) {
            for(k = h; k < n; k++){ 
                tmp = data[begin + k]; 
                j = k;
                while(j > h - 1 && data[begin + j - h] >= tmp) {
                    data[begin + j] = data[begin + j - h];
                    j=j-h;
                }
                data[begin + j] = tmp;
            }
            h = (h-1) / 3;
        } 
    }
}