import java.util.Scanner;

/*
 A program with a priority queue using an array-based representation of a max-heap. The priority is determined by the 
 number of occurrences of a number, and if the number of occurrences of two numbers is equal, the larger number has 
 a higher priority. The queue has two defined capacities - the maximum number of distinct elements and the maximum 
 number of total elements. The program handles three commands - insert, which inserts elements into the queue, 
 getMax, which removes the specified number of occurrences of an element with the highest priority from the queue, 
 and sort, which sorts the queue in non-decreasing order using the HeapSort method.
 */
public class Source {
    public static Scanner sc = new Scanner(System.in);
    public static int n; //maximum number of distinct elements
    public static int p; //maximum number of total elements
    public static String operation; //curren operation ('i', 'g' or 's')
    public static void main(String[] args) {
        int z = sc.nextInt(); //number of data sets
        for (int i = 0; i < z; i++) {
            //inputing data
            n = sc.nextInt(); 
            p = sc.nextInt();
            /*
             Creating the cueue with given capacities
             */
            Queue q = new Queue(n, p);
            operation = "d";
            /*
            Program inputs operation until it is 's', because 's' is always the last operation in the data set
             */
            while (!operation.equals("s")) { 
                operation = sc.next();
                switch(operation) {
                    case "i":
                        //insert
                        int count = sc.nextInt(); //number of elements to insert
                        for (int j = 0; j < count; j++) {
                            /*
                             Inputing element and inserting it into the queue
                             */
                            q.insert(sc.nextInt()); 
                        }
                        break;
                    case "g":
                        //getMax
                        /*
                        Inputing number of occurencies to delete and executing getMax on queue
                         */
                        q.getMax(sc.nextInt());
                        break;
                    case "s":
                        /*
                         Sorting queue with heapsort
                         */
                        q.HeapSort();
                        break;
                }
            }
        }
    }
}

/*
 A class representing a queue based on an array-based representation of a max-heap. It has fields that 
 store information about the currently occupied capacities and maximum capacities. The elements of the 
 queue are objects of the Element class.
 */
class Queue{
    public Element[] elements; //heap
    public int maxSize; //maximum number of distinct elements
    public int maxTotalSize; //maximum number of total elements
    public int currentSize; //current number of distinct elements
    public int currentTotalSize; //current number of total elements

    public Queue(int n, int p) {
        maxTotalSize = p;
        maxSize = n; 
        currentSize = 0; 
        currentTotalSize = 0; 
        elements = new Element[maxSize];
    }

    /*
     A method that inserts a new element provided as an argument into the queue. First, we use the findIndex method 
     to check if the given value already exists in the queue. If it does, we increment the occurrence count of that 
     value, increase the current total occurrence count, and use the upHeap method on that element. 
     If the given value is not yet present in the queue, we create a new element with the given value and an 
     occurrence count of 1. Then, we insert it at the end of the heap and use the upHeap method. The variables 
     storing information about the currently occupied capacities are incremented. 
     If the insertion of the element is not possible due to capacity limitations, the element is not inserted into 
     the queue.
     */
    public void insert(int value) {
        int index = findIndex(value); //index of the given value
        if (index == -1) {
            /*
             The given value is not yet present in the queue. 
             Now, program checks if it is possible to insert new element into queue due to capacities.
             */
            if (currentSize == maxSize || currentTotalSize == maxTotalSize) {
                //capacities are full, inserting element is impossible
                return;
            }

            //inserting element is possible, so new element is created
            elements[currentSize] = new Element(value); //element is pushed into the end of the heap
            upHeap(currentSize); //now, element is moved into correct place
            currentSize++;
            currentTotalSize++;
        } else {
            /*
            The number already exists in the queue. .
            */
            if (currentTotalSize == maxTotalSize) {
                //inserting element is impossible due to capacity limitation
                return;
            }

            //it is possible to insert element
            elements[index].occurrencies++;
            currentTotalSize++; 
            upHeap(index); //now, element is moved into correct place
        }
    }

    /*
     A function that removes the specified number of occurrences of an element with the highest priority. 
     If the number specified in the argument is greater than the number of occurrences of that element, 
     only the number of occurrences of that element is removed. Then, the function prints out that element 
     and the actual number of removed occurrences. If the queue is empty, the function prints "0 0".
     */
    public void getMax(int count) {
        if (currentSize == 0) {
            //queue is empty
            System.out.println("0 0");
            return;
        }

        /*
         We are checking if the number of occurrences to be removed, specified in the argument, is less than or 
         equal to the number of occurrences of the element with the highest priority, which is located at 
         index 0 in the queue.
         */
        if (elements[0].occurrencies <= count) {
            /*
             The number of occurrences of the element with the highest priority is less than or equal to the 
             number specified in the argument. Therefore, this element and the number of its occurrences are 
             printed, and then the element is removed from the queue.
             */
            System.out.println(elements[0].value + " " + elements[0].occurrencies); //outputing the result
            /*
             Decreasing the currently occupied capacity - the current total number of elements by the number 
             of occurrences of the element being removed and the current number of distinct elements by 1..
             */
            currentTotalSize -= elements[0].occurrencies;
            currentSize--;

            /*
            Inserting the last element at index 0 and using downHeap on it to maintain the heap condition.
             */
            elements[0] = elements[currentSize];
            downHeap(0, currentSize);
        } else {
            /*
             The number of occurrences of the element with the highest priority is greater than the number 
             specified in the argument. Therefore, this element and the number of times its occurrences to 
             remove are printed. After that, the 'downHeap' method is applied to this element to maintain 
             the heap condition.
             */
            System.out.println(elements[0].value + " " + count); //outputing the result
            elements[0].occurrencies -= count; 
            currentTotalSize -= count; 
            downHeap(0, currentSize);
        }
    }

    /*
    Function searches for the element with given value in the queue and returns it's index.
     */
    public int findIndex(int value) {
        int i = 0;
        while (i < currentSize) { //iterating through queue
            if (elements[i].value == value) {
                //element found, returning it's index
                return i;
            }
            i++;
        }
        return -1; //there is no element with such value
    }

    /*
     A function that moves an element at the given index down the heap until the element below it has 
     a lower priority. It utilizes the 'compare' function, which compares two queue elements and returns 
     true if the element specified as the first argument has a higher priority than the element specified 
     as the second argument.
    It takes the index of the element and the last index to which the 'downHeap' function should operate 
    as arguments.
     */
    public void downHeap(int index, int n) {
        int j; //index of the child of the element
        Element temp = elements[index];
        while (index < n / 2) { 
            j = 2 * index + 1; //index of the left child
            /*
            Checking whether the right child exists and if yes then we check if it has higher priority
            than the left child. If yes than the 'j' variable is incremented, so it points to the child
            with higher priority.
             */
            if (j < n - 1 && compare(elements[j], elements[j + 1]) == false) {
                j += 1;
            }
            /*
            Comparing the moving element with the child with higher priority.
            If the child has lower priority than the moving element, then we break the while.
             */
            if (compare(temp, elements[j])) {
                break;
            }
            /*
            The child has higher priority than the moving element so the child is moved up by one place.
             */
            elements[index] = elements[j];
            index = j;
        }
        //inserting the moving element into it's place
        elements[index] = temp;
    }
    
    /*
    Function moves element with index given in the argument up the heap until we meet element with
    higher priority.
     */
    public void upHeap(int index) {
        int j = (index - 1) / 2; //index of the parent node
        Element temp = elements[index]; //auxiliary variable that stores the moving element
        while (index > 0 && compare(temp, elements[j])) { 
            elements[index] = elements[j]; //moving parent by one place down
            index = j; 
            j = (j - 1) / 2; //new index of the parent node
        }
        //inserting the moving element into the correct place
        elements[index] = temp;
    }

    /*
    Function sorts array which is heap in non-descending order by HeapSort. Then
    the elements of the queue are printed.
     */
    public void HeapSort() {
        /*
        The variable 'n' is an auxiliary variable passed as an argument to the 'downHeap' function to indicate 
        which index the function should compare elements to.
         */
        int n = currentSize;
        while (n > 0) {
            /*
            Swaping the first element and element with index n-1.
             */
            Element temp = elements[0];
            elements[0] = elements[n - 1];
            elements[n - 1] = temp;
            n--;
            downHeap(0, n); //executing downHeap on the first element 
        }

        /*
         Outputing values and number of occuriencies of the elements of the queue.
         */
        for (int i = 0; i < currentSize; i++) {
            System.out.print(elements[i].value + " " + elements[i].occurrencies + " ");
        }
        System.out.print("\n");
    }

    /*
    Function compares two elements of the queue given in the arguments.
    If the first element has higher or equal priority then true is returned.
     */
    public Boolean compare(Element first, Element second) {
        if (first.occurrencies > second.occurrencies) { //comparing number of occuriencies of the elements
            //first element has higher priority
            return true;
        }
        if (first.occurrencies < second.occurrencies) {
            //second element has higher priority
            return false;
        }
        if (first.value >= second.value) { //comparing values of the elements
            //first element has higher or equal priority
            return true;
        }
        //second element has higher priority
        return false;
    }
}

/*
Objects of this class are elements of the queue
 */
class Element{
    public int value;
    public int occurrencies;
    public Element(int value) { 
        this.value = value;
        this.occurrencies = 1;
    }
}