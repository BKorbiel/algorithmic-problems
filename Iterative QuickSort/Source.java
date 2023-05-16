import java.util.Scanner;

/*
The program sorts rows of strings separated by commas by selected column using the QuickSort method without using a stack, recursion, 
and with a constant memory complexity. When the size of the data in subtask is less than 5, the program sorts the data using the 
SelectionSort method.
To achieve a constant memory complexity of QuickSort, the right endpoints of the intervals are marked by appending a '?' 
character to the end of the element.
 */
public class Source {
    public static Scanner sc = new Scanner(System.in);
    public static int dataSize; //number of rows
    public static int column; //The column by which the program will sort the data
    public static Boolean ascending; //stores info whether the sorting should be ascending or descending
    public static String[][] data;
    public static void main(String[] args) {
        int z = sc.nextInt(); //number of data sets
        String d = sc.nextLine();
        for (int i = 0; i < z; i++) {
            String input = sc.nextLine(); 
            String[] inputArr = input.split(",");
            dataSize = Integer.parseInt(inputArr[0]) + 1;
            column = Integer.parseInt(inputArr[1]) - 1;
            ascending = Integer.parseInt(inputArr[2]) == 1;
            /*
             Inputing data
             */
            data = new String[dataSize][];
            for (int j = 0; j < dataSize; j++) {
                data[j] = sc.nextLine().split(",");
            }
            
            QuickSort();

            //outputing the sorted data
            for (int j = 0; j < dataSize; j++) {
                /*
                 This loop changes the order of columns in the row so that the column by which the program sorted
                 the data is first
                 */
                String firstElem = data[j][column];
                for (int k = column - 1; k>=0; k--) {
                    data[j][k + 1] = data[j][k];
                }
                data[j][0] = firstElem;
                System.out.print(data[j][0]);
                for (int k = 1; k < data[j].length; k++) {
                    System.out.print("," + data[j][k]);
                }
                System.out.print("\n");
            }
            System.out.print("\n");
        }
    }

    /*iterative Quicksort without recursion, stack and with constant memory complexity
    It uses the findIndex() function to find the index of the right endpoint of the interval.
    It uses also the Partition() function to divide the interval into two subtasks.
    */
    public static void QuickSort() {
        /*
         The average time complexity is O(n*log(n)) because both findIndex() and Partition() have linear time complexity, 
         and in the average case scenario, Partition() will be called log(n) times. findIndex() will be called fewer 
         times than Partition() because it is in the outer loop.
         */
        int l = 1;
        while (l < dataSize) {
            int r = findIndex(l) - 1; //right endpoint of the interval
            while (l < r) {  
                if (r - l < 5) { //when the number of elements in the interval is less than 6 then it is sorted by SelectionSort
                    SelectionSort(l, r);
                    l = r;
                    r -= 1;
                } else {
                    int q = Partition(l, r); //Partition return index of the pivot
                    r = q - 1; //new right endpoint of the interval
                    if (l < r) {
                        //If the new interval has at least two elements, we mark the right endpoint of the interval by appending a '?' symbol.
                        data[q][column] += "?";
                    }
                }
            }
            l = r + 2; //new left endpoint of the interval
        }
    }

    /*
     A function that partitions an array and places one element in its correct position, returning its index.
    The last element of the interval is chosen as the pivot.
    It takes the indices of the interval boundaries as arguments.
    It utilizes the Compare(String, String) function to compare two elements.
     */
    public static int Partition(int l, int r) {
        /*The complexity of this function is indeed O(n) because in each step of the loop, the variable 'j' is 
        incremented, meaning the loop will execute at most n times.
        */
        int i = l - 1;
        
        String pivot = data[r][column]; //the last element as the pivot

        int j;
        for ( j = l; j < r; j++) { //iterating through interval
            if (Compare(data[j][column], pivot)) { //comparing pivot with element
                /*Inside that 'if' statement, the element should be placed before the pivot in the array, 
                so there is an increment of 'i' and a swap of elements at indices 'i' and 'j'.
                */
                i = i + 1; 
                swap (i, j); 
            }
        }
        
        swap (i + 1, r); //inserting the pivot into correct place

        return i + 1; //returning index of the pivot
    }

    /*
    The function takes two indicies as arguments and swaps elements with given indicies.
     */
    public static void swap(int i, int j) {
        String temp[] = data[i];
        data[i] = data[j];
        data[j] = temp;
    } 
    
    /*
     A function that searches for the right endpoint of an interval, which is the first element with an index 
     greater than 'l' and whose last character is '?'. If such an element doesn't exist, it returns the size 
     of the data (number of rows). If such an element exists, the '?' character is removed from its end.
     */
    public static int findIndex(int l) {
        /*
         The time complexity is O(n) because at most n comparisons will be performed.
         */
        //Iterating over the array until we encounter an element whose last character is '?'
        while (l<dataSize && data[l][column].charAt( data[l][column].length() - 1) != '?') {
            l++;
        }
        if (l==dataSize) { //there is no such element, so datasize is returned
            return dataSize;
        }

        //removing the '?' character
        data[l][column] = data[l][column].substring(0, data[l][column].length() - 1);
        return l;
    }

    public static void SelectionSort(int l, int r) {
        /*
        The time complexity is the sum of the numbers from 0 to n, which is n^2/2, therefore O(n^2)
         */
        int index;
        
        for (int i = l; i < r; i++) {//index of the correct place for the next element
            index = i;
            for (int j = i + 1; j <= r; j++) {
                /*Comparing the element at the 'index' position with the element at index 'j', and if the second 
                element should come before the first, assigning the second index to the 'index' variable.
                */
                if (!Compare(data[index][column], data[j][column])) {  
                    index = j;
                }
            }
            swap(index, i); //Inserting an element at the 'index' position into the correct place.
        }
    }

    /*
    A function that compares two elements given in the arguments.
    If the first element should come before the second element in the sorted array, the function returns true, otherwise false.
    The function checks whether the string is a number, and if it is, it compares the numeric values, 
    otherwise it compares strings using the 'compareTo' method.'
     */
    public static Boolean Compare(String first, String second) {
        //checking with regex if the element is a number
        if (first.matches("-?\\d+")) { 
            /*If the element is a number, the relationship between numbers is checked depending on the 
            'ascending' variable, which stores information whether the sorting is to be in ascending order 
            (true) or descending order (false)
            */
            if (ascending && Integer.parseInt(first) <= Integer.parseInt(second)) {
                return true; //in sorted data the first element should be before the second element
            }
            if (!ascending && Integer.parseInt(first) >= Integer.parseInt(second)) {
                return true; //in sorted data the first element should be before the second element
            }
        } else {
            /*
            If the element isn't a number 
            If the element is not a number, the relationship between strings is checked using the 'compareTo' 
            method depending on the 'ascending' variable, which stores information whether the sorting is to 
            be in ascending order (true) or descending order (false)
             */
            if (ascending && first.compareTo(second) < 1) {
                return true; //in sorted data the first element should be before the second element
            }
            if (!ascending && first.compareTo(second) > -1) {
                return true; //in sorted data the first element should be before the second element
            }
        }
        return false; //in sorted data the second element should be before the first element
    }
}