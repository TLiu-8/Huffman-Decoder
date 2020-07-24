/**
 * PersonalStack.java
 * Version 1.0
 * Author: Theo Liu
 * Date: 2020-04-30
 * Description: personal implementation of a stack data structure with peek, pop, and push methods.
 */

public class PersonalStack<E extends Comparable<E>>{
    //class variables
    private int size;
    private Node<E> head;
    private Node<E> tail;
    
    /**
     * PersonalStack Constructor
     * creates a stack with a head and tail
     * @param: null
     * @return: null
     */
    PersonalStack(){
        this.head = head;
        this.tail = tail;
    }
    
    /**
     * empty 
     * checks if the stack is empty or not
     * @param: null
     * @return: null
     */
    public boolean empty(){
        if (this.head == null || this.tail == null){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * peek
     * returns the first element of the stack (element at the head) without removing it from the stack
     * @param: null
     * @return: item of whatever type the stack holds
     */
    public E peek(){
        return this.head.getItem();
    }
    
    /**
     * pop
     * returns the first element of the stack (element at the head) while removing it from the stack
     * @param: null
     * @return: item of whatever type the stack holds
     */
    public E pop() {
        if (this.head == null){
            return null; //if the first node was null the stack was empty
        } else {
            Node<E> first = this.head;
            Node<E> second = first.getNext();
            if (second == null){             //if the second node was null the stack only had one element
                this.head = null;
                this.tail = null;
                this.size = 0;
                return first.getItem();
            } else {
                second.setPrev(null);
                this.head = second;
                this.size = this.size - 1;
                return first.getItem();
            }
        }
    }
   
    /**
     * push
     * adds an item to the front of the stack
     * @param: the item of type E (whatever type the stack holds)
     * @return: null
     */
    public void push(E item){
        Node<E> first = this.head;
        Node<E> newFirst = new Node<E>(null,item,first);  //create new node holding the item.
        this.head = newFirst;
        if (first == null){             //if the first node was null the list was empty
            this.tail = newFirst;
        } else {
            first.setPrev(newFirst);
        }
        this.size = this.size + 1;
    }
  
  //------------------------------------------------------------------------------     
  //  inner class Node  
  //------------------------------------------------------------------------------         
    private class Node<T>{ 
        private Node<T> prev;        
        private T item;
        private Node<T> next;
        
        //Node constructor
        public Node(Node<T> prev, T item, Node<T> next){
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
        //getters and setters
        public Node<T> getNext(){
            return this.next;
        }
        public void setNext(Node<T> next){
            this.next = next;
        }
        public Node<T> getPrev(){
            return this.prev;
        }
        public void setPrev(Node<T> prev){
            this.prev = prev;
        }
        public T getItem(){
            return this.item;
        }
        public void setItem(T item){
            this.item = item;
        }
    } //end of Node class  
}
//end of PersonalStack class