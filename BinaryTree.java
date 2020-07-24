/**
 * BinaryTree.java
 * Version: 1.0
 * Author: Theo Liu
 * Date: 2020-04-30
 * Description: personal implementation of a binary tree (with huffman parsing abilities)
 */

//import statements
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

//main class
class BinaryTree<E extends Comparable<E>>{
    //class variables
    Node<E> root;
    int size;
    
    /*
     * BinaryTree Constructor
     * creates the personal binary tree object
     * @param:null
     * @return:null
     */
    public BinaryTree(){
        this.root = null;
        size = 0;
    }
//METHODS ------------------------------------------------------------------------------  
    /**
     * add
     * Adds the specified element to this tree if it is not already present.
     * @param: the item being added to the tree
     * @return: boolean (true if inserted, or false if item is already present)
     */
    public boolean add(E item){
        if (this.root == null){
            root = new Node<E>(item); size++;
        } else {
            Node<E> currentNode = this.root;
            int compare = item.compareTo(currentNode.getItem());
            //while the item is smaller and left child is not empty or the item is greater and right child is not empty
            while (compare < 0 && currentNode.getLeft() != null || compare > 0 && currentNode.getRight() != null){ 
                //move one level down to left or to right
                if (compare < 0){
                    currentNode = currentNode.getLeft();
                } else {
                    currentNode = currentNode.getRight();
                }
                //compare the item with the current node
                compare = item.compareTo(currentNode.getItem());             
            }
            //insert the item to left or to right
            if (compare < 0){
                currentNode.setLeft(item); 
                size++;
            } else if (compare > 0){
                currentNode.setRight(item); 
                size++;
            } else {
                return false;  //the item is present and cannot be inserted
            } 
        }
        return true;
    }
    
    /**
     * huffmanAdd
     * Adds the specified element to the tree based on a String. 
     * It reads the string character by character, and if it's 0 it moves left to add and 1 moves right.
     * @param: the item being added to the tree and a string of 1s and 0s
     * @return: null
     */
    public void huffmanAdd(E item, String position){
        Node<E> currentNode = this.root;
        for (int i = 0; i < position.length()-1; i++){
            //if character is 0 move to the left, if character is 1 move to the right
            if (position.charAt(i) == '0'){
                currentNode = currentNode.getLeft();
            } else {
                currentNode = currentNode.getRight();
            }
        }
        //final character determines if new node is set left or right
        if (position.charAt(position.length()-1) == '0'){
            currentNode.setLeft(item); 
        } else {
            currentNode.setRight(item);
        }
        size++;
    }
    
    /**
     * huffmanTraverse
     * ONLY MEANT FOR DECODING A HUFFMAN TREE (whose node's also contain string)
     * given a string of 0s and 1s, the method traverses the string and the tree (0 moves left, 1 moves right)
     * when it reaches a leaf, it returns the String of the character for that ascii and recursively calls itself again.
     * @param: the given string of 0s and 1s
     * @return: String of decoded message 
     */
    public String huffmanTraverse(String given){
        Node currentNode = this.root; // start at root
        int count = 0;
        int decodedChars = 64; //decode 64 characters at a time.
        String result = "";
        int i = 0;
        boolean check = false;
        while (count < decodedChars){  
            if (given.charAt(i) == '0'){  //move left
                currentNode = currentNode.getLeft();
            } else if (given.charAt(i) == '1'){   //move right
                currentNode = currentNode.getRight();
            }
            
            if (currentNode.isLeaf()){  //if a leaf is found
                String item = (String)currentNode.getItem();
                char stuff = (char)(Integer.parseInt(item)); //converts string to int, then int to char.
                //appends charcter converted to string with rest of decoded mesage
                result = result + Character.toString(stuff);
                currentNode = this.root; //returns to root
                count++;  
            }
            if (i == (given.length()-1)){
              count = decodedChars;
              check = true;
            } else {
              i++;  //traverse through string
            }
        }
        if (check == false){ //if there is still more to decode
          return result + huffmanTraverse(given.substring(i,given.length()));
        } else {  //base case (reached end)
          return result;
        }
    }
//------------------------------------------------------------------------------  
    /**
     * contains
     * Returns true if this tree contains the specified element.
     * @param: the item to be found
     * @return: boolean T/F
     */
    public boolean contains(E item){
        if (this.root == null){
            return false;
        }
        if (this.root.getItem().equals(item)){
            return true;
        }
        Node<E> currentNode = this.root;
        int comp = item.compareTo(currentNode.getItem());        
        while (comp < 0 && currentNode.getLeft() != null ||  comp > 0 && currentNode.getRight() != null){ 
            if (comp < 0){
                currentNode = currentNode.getLeft();
            } else {
                currentNode = currentNode.getRight();
            }
            if (currentNode.getItem().equals(item)){
                return true;
            }
            comp = item.compareTo(currentNode.getItem());  
        }
        return false;
    }
    
    /**
     * isEmpty()
     * Returns true if the tree is empty
     * @param: null
     * @return: boolean T/F
     */
    public boolean isEmpty(){
        if (root == null){
            return true;
        } else{
            return false;
        }
    }
    /*
     * clear
     * Removes all of the elements from this tree.
     * @param: null
     * @return: null
     */
    public void clear(){
      root.setRight(null);
      root.setLeft(null);
      this.root = null;
    }
    /*
     * size
     * Returns the number of elements in this tree.
     * @param: null
     * @return: the int
     */
    public int size(){
        return size;
    }
    
    /*
     * countLeaves
     * Returns the number of leaves in this tree.
     * @param: null
     * @return: the int
     */
    public int countLeaves(){
      return countLeavesAlgorithm(this.root); //calls the algorithm given the root.
    }
    //recursively counts the leaves.
    public int countLeavesAlgorithm(Node<E> currentNode){
        if (currentNode.getLeft() != null && currentNode.getRight() != null){
            return countLeavesAlgorithm(currentNode.getLeft()) + countLeavesAlgorithm(currentNode.getRight());
        } else if (currentNode.getLeft() == null && currentNode.getRight() != null ){
            return countLeavesAlgorithm(currentNode.getRight());
        } else if (currentNode.getRight() == null && currentNode.getLeft() != null ){
            return countLeavesAlgorithm(currentNode.getLeft());
        } else {
            return 1;
        }
    }
    
    /*
     * countLeavels
     * Returns the number of leavels in this tree.
     * @param: null
     * @return: the int
     */
    public int countLevels(){
        return countLevelsAlgorithm(this.root);
    }
    //recursively counts the levels
    public int countLevelsAlgorithm(Node<E> currentNode){
        if (currentNode.getLeft() != null && currentNode.getRight() != null){
            if (countLevelsAlgorithm(currentNode.getLeft()) > countLevelsAlgorithm(currentNode.getRight())){
                return 1+countLevelsAlgorithm(currentNode.getLeft());
            } else {
                return 1+countLevelsAlgorithm(currentNode.getRight());
            }
        } else if (currentNode.getLeft() == null && currentNode.getRight() != null ){
            return 1+countLevelsAlgorithm(currentNode.getRight());
        } else if (currentNode.getRight() == null && currentNode.getLeft() != null ){
            return 1 + countLevelsAlgorithm(currentNode.getLeft());
        } else {
            return 1;
        }
    }
//------------------------------------------------------------------------------    
    /*
     * toString
     * Returns String representation of the tree. Elements are in natural order.
     * @param:null
     * @return: formatted string of the binary tree
     */
//    @Override
//    public String toString(){
//        if (this.root == null){return "[]";}
//        Stack<Node<E>> stack = new Stack<Node<E>>();
//        Node<E> currentNode = this.root;
//        String s = "";
//        while (!stack.empty() || currentNode != null){
//            if (currentNode != null){
//                stack.push(currentNode);
//                currentNode = currentNode.getLeft();
//            }else {
//                currentNode = stack.pop();
//                s = s + currentNode.getItem().toString() +", ";
//                currentNode = currentNode.getRight();
//            }
//        }
//        return "["+ s.substring(0,s.length()-2) +"]";
//    }   
//    //Returns String representation of the tree. Elements are in order by level.
      //left in case you want to see another representation
    @Override
    public String toString(){
        if (this.root == null){return "[]";}
        Queue<Node<E>> q = new LinkedList<Node<E>>(); //LinkedList class implements Queue interface
        q.add(this.root);
        String s = "";
        while (!q.isEmpty()){
            Node<E> currentNode = q.remove();
            s = s + currentNode.getItem().toString() +", ";
            if (currentNode != null){
                Node<E> leftChild = currentNode.getLeft();
                if (leftChild != null){q.add(leftChild);}
                Node<E> rightChild = currentNode.getRight();
                if (rightChild != null){q.add(rightChild);}
            }
        }
        return "["+ s.substring(0,s.length()-2) +"]";
    } 
  
//------------------------------------------------------------------------------     
//  inner class Node  
//------------------------------------------------------------------------------  
    private class Node<T>{
        private T item;
        private Node<T> left;
        private Node<T> right;
        
        //Node constructor
        public Node(T item){
            this.item = item;
            this.left = null;
            this.right = null;
        } 
        //getters and setters
        public T getItem(){
            return this.item;
        }
        public void setItem(T item){
            this.item = item;
        }
        public Node<T> getLeft(){
            return this.left;
        }        
        public void setLeft(T item){
            this.left = new Node<T>(item);
        }
        public Node<T> getRight(){
            return this.right;
        } 
        public void setRight(T item){
            this.right = new Node<T>(item);
        }  
        //helper methods
        private boolean isLeaf(){
            if (getLeft() == null && getRight() == null){
                return true;
            } else {
                return false;
            }
        }
    } //end of Node class 
} 
//end of BinaryTree class