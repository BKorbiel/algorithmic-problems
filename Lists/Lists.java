import java.util.Scanner; 
/*
 Program for handling a doubly linked list and a singly linked list.
 */
class Source{  
    static Train TrainsList; //the list of the trains (first element)
    public static Scanner sc = new Scanner(System.in);
    public static void main(String args[]){
        int z = sc.nextInt(); //number of sets
        for (int i = 0; i < z; i++) {
            TrainsList = null;
            int n = sc.nextInt(); //number of operations
            for (int j = 0; j < n; j++) {
                String operation = sc.next();
                if (operation.equals("New")) {
                    /*
                    Creates new train with one carriage and inserts it to the beginning of the list of trains
                    */
                    String trainName = sc.next();
                    String carriageName = sc.next();
                    
                    //a new train named trainName with one carriage named carriageName
                    Train newTrain = new Train(trainName, new Node(carriageName));
                    
                    //inserting to the beginning og the list
                    newTrain.next = TrainsList;
                    TrainsList = newTrain;
                } else if (operation.equals("InsertFirst")) {
                    /*
                    Inserts new carriage as the first carriage of the train
                    */
                    String trainName = sc.next();
                    String carriageName = sc.next();

                    Train walkElem = TrainsList;
                    while (walkElem.name.equals(trainName)==false) { //searching for the train
                        walkElem = walkElem.next;
                    }

                    Node newNode = new Node(carriageName); //creating the carriage

                    //checking the direction of the first carriage (whether it is reversed or not)
                    if (walkElem.firstCarriage.prev == null) {
                        newNode.next = walkElem.firstCarriage; //updating links
                        walkElem.firstCarriage.prev = newNode;
                    } else { //the first carriage is reversed
                        newNode.prev = walkElem.firstCarriage;
                        walkElem.firstCarriage.next = newNode;
                    }
                    walkElem.firstCarriage = newNode; //new carriage is the first carriage
                } else if (operation.equals("InsertLast")) {
                    /*
                    Inserts new carriage at the end of the train
                    */
                    String trainName = sc.next();
                    String carriageName = sc.next();

                    Train walkElem = TrainsList;
                    while (walkElem.name.equals(trainName)==false) { //searching for the train
                        walkElem = walkElem.next;
                    }

                    Node newNode = new Node(carriageName); //creating new carriage

                    //checking the direction of the last carriage (whether it is reversed or not)
                    if (walkElem.lastCarriage.next == null) {
                        newNode.prev = walkElem.lastCarriage;
                        walkElem.lastCarriage.next = newNode;
                    } else {
                        newNode.next = walkElem.lastCarriage;
                        walkElem.lastCarriage.prev = newNode;
                    }
                    walkElem.lastCarriage = newNode;
                } else if (operation.equals("Display")) {
                    /*
                    Display train carriages
                    */
                    String trainName = sc.next();
                    Train walkElem = TrainsList;

                    System.out.print(trainName + ": ");

                    while (walkElem.name.equals(trainName) == false) { //searching for the train
                        walkElem = walkElem.next;
                    }
                    Node walkCarriage = walkElem.firstCarriage; //current carriage
                    Boolean isReversed = walkCarriage.next == null; //this variable of type bool remembers the direction of the carriage
                    System.out.print(walkCarriage.name + " "); //printing the first carriage
                    Node prevCarriage = walkCarriage; //this variable stores the previous carriage (it is needed to check the direction of the current carriage)

                    //updating walkCarriage as the second carriage
                    if (isReversed) {
                        walkCarriage = walkCarriage.prev;
                    } else {
                        walkCarriage = walkCarriage.next;
                    }

                    while (walkCarriage != null) { //iterating through carriages
                        System.out.print(walkCarriage.name + " "); //printing out the current carriage

                        //updating the isReversed variable by checking the direction of current carriage (by checking whether the 'next' or the 'prev' field stores previous carriage)
                        if (isReversed) {
                            if (walkCarriage.prev != null && walkCarriage.prev.name.equals(prevCarriage.name)) {
                                isReversed = false;
                            }
                        } else {
                            if (walkCarriage.next != null && walkCarriage.next.name.equals(prevCarriage.name)) {
                                isReversed = true;
                            }
                        }
                        //updating the prevCarriage
                        prevCarriage = walkCarriage;

                        //updating current carriage (going to the next carriage)
                        if (isReversed) {
                            walkCarriage = walkCarriage.prev;
                        } else {
                            walkCarriage = walkCarriage.next;
                        }
                    }
                    System.out.print("\n");
                } else if (operation.equals("TrainsList")) {
                    /*
                    Prints out every train.
                    */
                    System.out.print("Trains: ");
                    Train walkElem = TrainsList;
                    while (walkElem != null) { //iterating through trains
                        System.out.print(walkElem.name + " ");
                        walkElem = walkElem.next; //going to the next trains
                    }
                    System.out.print("\n");
                } else if (operation.equals("Reverse")) {
                    /*
                    Reverses the order of the carriages in the train in O(1)
                    */
                    String trainName = sc.next();
                    Train walkElem = TrainsList;
                    while (walkElem.name.equals(trainName)==false) { //searching for train
                        walkElem = walkElem.next;
                    }
                    Node temp = walkElem.firstCarriage;
                    //swaping the variables that store the first and the last carriage
                    walkElem.firstCarriage = walkElem.lastCarriage;
                    walkElem.lastCarriage = temp;
                } else if (operation.equals("Union")) {
                    /*
                    Adds the second train to the first and then removes the second one.
                    */
                    String firstTrainName = sc.next();
                    String secondTrainName = sc.next();

                    Train firstTrain = null, secondTrain = null;

                    //if the train to remove is the first train then the list is updated
                    if (TrainsList.name.equals(secondTrainName)) { 
                        secondTrain = TrainsList;
                        TrainsList = TrainsList.next;
                    }
                    Train walkElem = TrainsList;
                    while (firstTrain == null || secondTrain == null) { //searching for the first and the second train
                        if (walkElem.name.equals(firstTrainName)) {
                            firstTrain = walkElem;
                        }
                        if (walkElem.next != null && walkElem.next.name.equals(secondTrainName)) { //removing the second train from the list
                            secondTrain = walkElem.next;
                            walkElem.next = secondTrain.next;
                        }
                        walkElem = walkElem.next;
                    }

                    if (firstTrain.lastCarriage.next == null) { //checking the direction of the last carriages of the first train
                        firstTrain.lastCarriage.next = secondTrain.firstCarriage; //updating the link
                        if (secondTrain.firstCarriage.prev == null) { //checking the direction of the first carriage of the second train
                            secondTrain.firstCarriage.prev = firstTrain.lastCarriage; //updating the link
                        } else {
                            secondTrain.firstCarriage.next = firstTrain.lastCarriage;
                        }
                        firstTrain.lastCarriage = secondTrain.lastCarriage; //updating the variable that stores the last carriage of the first train
                    } else {
                        firstTrain.lastCarriage.prev = secondTrain.firstCarriage;
                        if (secondTrain.firstCarriage.prev == null) { //checking the direction of the first carriage of the second train
                            secondTrain.firstCarriage.prev = firstTrain.lastCarriage; //updating the link
                        } else {
                            secondTrain.firstCarriage.next = firstTrain.lastCarriage;
                        }
                        firstTrain.lastCarriage = secondTrain.lastCarriage; //updating the variable that stores the last carriage of the first train
                    }
                } else if (operation.equals("DelFirst")) {
                    /*
                    Removes the first carriage of the train and creates a new train with this carriage. If the carriage to remove 
                    is the only carriage of the train then the train is removed.
                    */
                    String firstTrainName = sc.next();
                    String newTrainName = sc.next();
                    Train train = null;
                    if (TrainsList.name.equals(firstTrainName)) { 
                        train = TrainsList;
                        //if the train is the first train and it has only one carriage then this train is removed from the list
                        if (train.firstCarriage.name.equals(train.lastCarriage.name)) { 
                            TrainsList = train.next;
                        }
                    }
                    Train walkElem = TrainsList;
                    while (train == null) { //searching for the train
                        if (walkElem.next.name.equals(firstTrainName)) {
                            train = walkElem.next;
                            //if the train is found and it has only one carriage then it is removed from the list
                            if (train.firstCarriage.name.equals(train.lastCarriage.name)) { 
                                walkElem.next = train.next;
                            }
                        }
                        walkElem = walkElem.next;
                    }
                    String carriageName = train.firstCarriage.name; //the name of the carriage to remove
                    Train newTrain = new Train(newTrainName, new Node(carriageName)); //creating the new train
                    if (train.firstCarriage.prev == null) { //checking the direction of the first carriage of the train
                        if (train.firstCarriage.next != null) {
                            //checking the direction of the second carriage (if it exists) and then updating the links
                            if (train.firstCarriage.next.prev != null && train.firstCarriage.next.prev.name.equals(train.firstCarriage.name)) {
                                train.firstCarriage.next.prev = null;
                            } else {
                                train.firstCarriage.next.next = null;
                            }
                        } 
                        train.firstCarriage = train.firstCarriage.next;
                    } else {
                        //checking the direction of the second carriage (if it exists) and then updating the links
                        if (train.firstCarriage.prev != null) {
                            if (train.firstCarriage.prev.prev != null && train.firstCarriage.prev.prev.name.equals(train.firstCarriage.name)) {
                                train.firstCarriage.prev.prev = null;
                            } else {
                                train.firstCarriage.prev.next = null;
                            }
                        }
                        train.firstCarriage = train.firstCarriage.prev;
                    }
                    //inserting the new train into the beginning of the list
                    newTrain.next = TrainsList;
                    TrainsList = newTrain;
                } else if (operation.equals("DelLast")) {
                    /*
                    Removes the last carriage of the train and creates a new train with this carriage. If the carriage to remove 
                    is the only carriage of the train then the train is removed.
                    */
                    String firstTrainName = sc.next();
                    String newTrainName = sc.next();
                    Train train = null;
                    if (TrainsList.name.equals(firstTrainName)) {
                        train = TrainsList;
                        //if the train is the first train and it has only one carriage then this train is removed from the list
                        if (train.firstCarriage.name.equals(train.lastCarriage.name)) { 
                            TrainsList = train.next;
                        }
                    }
                    Train walkElem = TrainsList;
                    while (train == null) { //searching for the train
                        if (walkElem.next.name.equals(firstTrainName)) {
                            train = walkElem.next;
                            //if the train is found and it has only one carriage then it is removed from the list
                            if (train.firstCarriage.name.equals(train.lastCarriage.name)) {
                                walkElem.next = train.next;
                            }
                        }
                        walkElem = walkElem.next;
                    }
                    String carriageName = train.lastCarriage.name;
                    Train newTrain = new Train(newTrainName, new Node(carriageName)); //creating the new train
                    if (train.lastCarriage.next == null) { //checking the direction the last carriage of the train
                        if (train.lastCarriage.prev != null) {
                            //checking the direction of the one before last carriage (if it exists) and then updating the links
                            if (train.lastCarriage.prev.prev != null && train.lastCarriage.prev.prev.name.equals(train.lastCarriage.name)) {
                                train.lastCarriage.prev.prev = null;
                            } else {
                                train.lastCarriage.prev.next = null;
                            }
                        } 
                        train.lastCarriage = train.lastCarriage.prev;
                    } else {
                        if (train.lastCarriage.next != null) {
                            //checking the direction of the one before last carriage (if it exists) and then updating the links
                            if (train.lastCarriage.next.prev != null && train.lastCarriage.next.prev.name.equals(train.lastCarriage.name)) {
                                train.lastCarriage.next.prev = null;
                            } else {
                                train.lastCarriage.next.next = null;
                            }
                        } 
                        train.lastCarriage = train.lastCarriage.next;
                    }
                    //inserting the new train into the beginning of the list
                    newTrain.next = TrainsList;
                    TrainsList = newTrain;
                }
            }
        }
    }
}

/*
 Carriage
 */
class Node{
    Node prev; //previous carriage
    Node next; //next carriage
    String name; //the name of the carriage

    public Node(String name) {
        this.name = name;
        this.prev = null;
        this.next = null;
    }
}

class Train{
    Train next; //next train
    Node firstCarriage; //first carriage
    Node lastCarriage;  //last carriage
    String name; //the name of the train

    public Train(String name, Node firstCarriage) {
        this.name = name;
        this.next = null;
        this.firstCarriage = firstCarriage; //creating the carriage (every train has at least one carriage)
        this.lastCarriage = firstCarriage;
    }
}