import java.util.Scanner;

/*
 A program that, given two lists of nodes - one in inorder and the other in postorder or preorder - 
 reconstructs a binary tree using the Tree class. Then, the program outputs the preorder or postorder 
 traversal as well as the level-order traversal..
 */
public class Source {
    public static Scanner sc = new Scanner(System.in);
    public static int size; //size of the tree
    public static int inorder[]; //array of inorder traversal
    public static int porder[]; //array of preorder or postorder traversal
    public static String order; //type of the traversal
    public static Tree tree;
    public static void main(String[] args) {
        int z = sc.nextInt(); 
        for (int i = 0; i < z; i++) {
            size = sc.nextInt();
            //creating arrays
            porder = new int[size];
            inorder = new int[size];
            order = sc.nextLine();
            order = sc.nextLine();
            //inputing data
            for (int j = 0; j < size; j++) {
                porder[j] = sc.nextInt(); 
            }
            String s = sc.nextLine();
            s = sc.nextLine();
            for (int j = 0; j < size; j++) {
                inorder[j] = sc.nextInt();
            }
            /*
            Creating tree depending on the type of the given traversal and outputing results
             */
            tree = new Tree();
            if (order.equals("POSTORDER")) {
                //tree reconstruction with inorder and postorder traversals
                tree.root = buildTreeWithPostorder(0, size - 1, 0, size - 1);
                
                //outputing results
                System.out.println("PREORDER");
                preorderDisplay(tree.root);
                System.out.print("\n");
            } else {
                //tree reconstruction with inorder and preorder traversals
                tree.root = buildTreeWithPreorder(0, size - 1, 0, size - 1);

                //outputing results
                System.out.println("POSTORDER");
                postorderDisplay(tree.root);
                System.out.print("\n");
            }

            //outputing levelorder traversal
            System.out.println("LEVELORDER");
            levelorderDisplay();
        }
    }

    /*
     A recursive function that, given the inorder and postorder traversals, reconstructs a binary tree.
    It takes the starting and ending indices of the currently processed intervals in the postorder and 
    inorder traversals as arguments.
    First, a new node is created, and the value from the end of the postorder interval is inserted into it. Then, 
    recursively, the left child is created, followed by the right child.
    Finally, the function returns the created node.
    The function utilizes the findIndex(int value) function, which searches for the index of the specified 
    element in the inorder traversal.
     */
    public static Node buildTreeWithPostorder(int inorder_start, int inorder_end, int postorder_start, int postorder_end) {
        if (postorder_start > postorder_end || inorder_start > inorder_end) { 
            return null;
        }

        /*
         The root of the currently processed subtree is the last element of the postorder interval. 
         Therefore, we create a new node and assign this value to it.
         */
        int value = porder[postorder_end]; //value of the root of subtree
        Node root = new Node(value); //creating root of the subtree

        int index = findIndex(value); //searching for index of the value in inorder
        int leftSize = index - inorder_start; //size of the left subtree
        

        root.leftChild = buildTreeWithPostorder(inorder_start, index - 1, postorder_start, postorder_start + leftSize - 1);
        root.rightChild = buildTreeWithPostorder(index + 1, inorder_end, postorder_start + leftSize, postorder_end - 1);

        //returning the created subtree
        return root;
    }


    /*
     A recursive function that, given the inorder and preorder traversals, reconstructs a binary tree.
    It takes the starting and ending indices of the currently processed intervals in the preorder and 
    inorder traversals as arguments.
    First, a new node is created, and the value from the beginning of the preorder interval is inserted into it. 
    Then, recursively, the left child is created, followed by the right child.
    Finally, the function returns the created node.
    The function utilizes the findIndex(int value) function, which searches for the index of the specified element 
    in the inorder traversal.
     */
    public static Node buildTreeWithPreorder(int inorder_start, int inorder_end, int preorder_start, int preorder_end) {
        if (preorder_start > preorder_end || inorder_start > inorder_end) {
            //jeden z przedzialow jest pusty, wiec zwracamy null
            return null;
        }

        /*
         Korzen aktualnie przetwarzanego poddrzewa to pierwszy element przedzialu preorder, dlatego
         tworzymy nowy wezel i wpisujemy do niego ta wartosc
         */
        int value = porder[preorder_start]; //value of the root
        Node root = new Node(value); //creating the root of the subtree

        int index = findIndex(value); //searching for index of the value in inorder
        int leftSize = index - inorder_start; //size of the left subtree

        root.leftChild = buildTreeWithPreorder(inorder_start, index - 1, preorder_start + 1, preorder_start + leftSize);
        root.rightChild = buildTreeWithPreorder(index + 1, inorder_end, preorder_start + leftSize + 1, preorder_end);

        //returing the created subtree
        return root;
    }


    /*
     A function that, given a value as an argument, searches for the index of that value in the inorder traversal.
     */
    public static int findIndex(int value) {
        int i = 0;
        while (i < size && inorder[i] != value) i++;
        
        return i==size ? -1 : i; 
    }

    /*
     An iterative function that prints a binary tree level by level, using a queue. The function saves the 
     children of the current element (if they exist) to the queue, and processes the elements of the queue
    one by one until the queue is empty.
     */
    public static void levelorderDisplay() {
        if (tree.root == null) {
            //tree doesnt exist
            return;
        }

        /*
         Creating the queue and inserting root into it
         */
        Queue q = new Queue();
        q.insert(tree.root);

        while (!q.isEmpty()) {
            Node node = q.remove(); //taking the first element of the queue
            System.out.print(node.value + " "); //outputing element
            if (node.leftChild != null) { 
                //if left child exists, it is inserted into the queue
                q.insert(node.leftChild);
            }
            if (node.rightChild != null) {
                //if right child exists, it is inserted into the queue
                q.insert(node.rightChild);
            }
        }
        System.out.print("\n");
    }

    /*
     A recursive function that prints a binary tree in preorder traversal 
     (meaning the current element is printed first, followed by recursive calls for the left and right subtrees)
     */
    public static void preorderDisplay(Node node) {
        if (node == null) {
            return;
        }
        System.out.print(node.value + " ");
        preorderDisplay(node.leftChild); //left subtree
        preorderDisplay(node.rightChild); //right subtree
    }

    /*
    A recursive function that prints a binary tree in postorder traversal (meaning recursive calls for the left 
    and right subtrees are made first, followed by printing the current element)
     */
    public static void postorderDisplay(Node node) {
        if (node == null) {
            return;
        }
        postorderDisplay(node.leftChild); //left subtree
        postorderDisplay(node.rightChild); //right subtree
        System.out.print(node.value + " ");
    }
}

/*
 A class that represents a queue. It stores the first and last elements and has methods insert, isEmpty, and remove. 
 The values of the queue elements are nodes of a binary tree.
 */
class Queue{
    public QueueNode first; //first element of the queue
    public QueueNode last; //last element of the queue

    public Queue() {
        first = null;
        last = null;
    }

    public void insert(Node node) { //pushes new element into the queue
        QueueNode newQNode = new QueueNode(node); //creating new queue node
        if (last == null) { 
            /*
             Queue is empty
             */
            last = newQNode;
            first = newQNode;
            return;
        }
        last.next = newQNode; 
        last = newQNode;
    }

    /*
     Function removes the first element of the queue and returns it
     */
    public Node remove() {
        if (first == null) {
            //queue empty
            return null;
        }
        Node node = first.node;
        first = first.next;
        if (first == null) {
            //the removed element was the only element of the queue
            last = null;
        }
        return node;
    }

    public Boolean isEmpty() {
        return (first==null && last==null);
    }
}

/*
A class for objects that represent nodes of a queue. The object stores node of a binary tree as its value.
 */
class QueueNode {
    public Node node; //node of the binary tree
    public QueueNode next; //next element of the queue
    public QueueNode(Node node) {
        this.node = node;
    }
}

/*
 Binary tree
 */
class Tree{
    public Node root;
    public Tree() {
        root = null;
    }
}


/*
 Node of the binary tree
 */
class Node{
    public Node leftChild;
    public Node rightChild;
    public int value; //stored value

    public Node(int value) {
        leftChild = null; 
        rightChild = null;
        this.value = value; 
    }
}

/*
Testy:
Wejscie:
4
15
PREORDER
1 2 4 8 9 5 10 11 3 6 12 13 7 14 15
INORDER
8 4 9 2 10 5 11 1 12 6 13 3 14 7 15
15
POSTORDER
8 9 4 10 11 5 2 12 13 6 14 15 7 3 1
INORDER
8 4 9 2 10 5 11 1 12 6 13 3 14 7 15
4
PREORDER
1 2 3 4
INORDER
1 3 4 2
4
POSTORDER
4 3 2 1
INORDER
1 3 4 2

Wyjscie:
ZESTAW 1
POSTORDER
8 9 4 10 11 5 2 12 13 6 14 15 7 3 1
LEVELORDER
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
ZESTAW 2
PREORDER
1 2 4 8 9 5 10 11 3 6 12 13 7 14 15
LEVELORDER
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
ZESTAW 3
POSTORDER
4 3 2 1
LEVELORDER
1 2 3 4
ZESTAW 4
PREORDER
1 2 3 4
LEVELORDER
1 2 3 4
 */