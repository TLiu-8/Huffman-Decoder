/**
 * Decoder.java
 * Version: 1.0
 * Author: Theo Liu
 * Date: 2020-04-23
 * Description: takes a compressed file and decompresses it.
 */

//import statements
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Main Class
class Decoder {
  
  //class variables  
  public static String[] values;
  public static String[] paths;
  public static int size;
  public static int currentElement;
  
  public Decoder(){
    size = 0;
    currentElement = 0;
  }
  
  /**
   * decode
   * takes a filename, reads it, produces a huffman tree, writes decoded message in new file
   * @param: the file name
   * @return: null
   */
  public static void decode (String fileName) throws IOException{
      String givenName = fileName;
      BufferedReader input = new BufferedReader(new FileReader(givenName + ".MZIP")); //entered names exlcude the mzip.
      
      //read the first line to get the original file name, and create the name for the new file
      String name =  "DECODED_"+input.readLine();
      //read the second line to get the huffman tree
      String extractedTree = input.readLine();
      //read the third line to get the padded bits
      String extra = input.readLine();
      int padded = Integer.parseInt(extra);
      input.close(); //close the reader
      
      String binaryCode = ""; //String of 0s and 1s that we'll use to parse the huffman tree
      //create a fileinputstream to read the bytes on the encoded message line
      FileInputStream in = null;
      try {
          in = new FileInputStream(givenName + ".MZIP");
          int c;
          int count = 0;
          boolean check = false;;
          while ((c = in.read()) != -1) {   
              if (count == 3){  //once count has reached 3 (first three lines are passed)
                      String stuff = Integer.toBinaryString(c);
                      while (stuff.length() < 8){
                          stuff = "0"+stuff;  //make the string always have 8 characters
                      }
                      //System.out.println(stuff);
                      binaryCode = binaryCode + stuff; //append binary representation of the character
              } else {
                  if (c == 13 && check == false){ // if a new line is encountered add one to count
                      count++;
                  } else if (c == 10 && check == false){ //sometimes a file has a 10 (\r) after every \n
                      check = true;
                  } else if (c == 10 && check == true){  //if there are 10s then count by them instead.
                      count++;
                  }
              }
          }
      } finally {
          if (in != null) {
              in.close();  //close reader
          }
      }
      countElements(extractedTree.substring(1,extractedTree.length()-1)); //first counts the # of elements in the tree.
      paths = new String[size];  //create two arrays to store the data with the size found from the countElements method.
      values = new String[size]; //would be easier to use an arraylist, but making a custom implementation is too hard.
      
      //takes  the huffman tree string and creates two arrays containing values (at each node of the tree), and paths to those values.
      debracketer(extractedTree.substring(1,extractedTree.length()-1),"");

      //create the huffman tree
      BinaryTree<String> decodedHuffman = new BinaryTree<String>();
      
      //add the root
      decodedHuffman.add(extractedTree.substring(1,extractedTree.length()-1));
      
      //add all other elements
      int count = 1; //it first adds 1 path elements (1st level), and then adds 2-path (2nd level), then 3-path,etc.
      boolean check = true;
      while (check){  //runs until element with n-paths cannot be found
          check = false;
          for (int i = 0; i < paths.length; i++){
              if (paths[i].length() == count){
                  check = true;
                  decodedHuffman.huffmanAdd(values[i],paths[i]); //adds the value based on it's path
              }
          }
          count++;
      }
      //System.out.println(decodedHuffman.toString());
      //traverses the tree without the padded bits
      String decoded = decodedHuffman.huffmanTraverse(binaryCode.substring(0,binaryCode.length()-padded));
      System.out.println(decoded);  //decoded message
      System.out.println(fileName+" has been succesfully decoded");
    
      try {
          //create new file, and write the decoded message into the file.
          File decodedFile = new File(name);
          decodedFile.createNewFile();
          FileWriter output = new FileWriter(name);
          output.write(decoded);
          output.close();
      } catch (IOException e){ //if file cannot be created.
          System.out.println("error");
          e.printStackTrace();
      }
  }
  //end of decode method
  
  /*
   * countElements
   * splits the string into two sections, then adds the # of elements at each level
   * then recursively calls the method again on those two sections.
   * @param: the given huffman tree string and current path taken
   * @return: null
   */
    public static void countElements(String given){
        PersonalStack <Character> chars = new PersonalStack<Character>();
        if (given.length() != 0){
            char first = given.charAt(0);
            if (first == '('){ 
                chars.push(first);
                for (int i = 1; i < given.length(); i++){
                    char symbol = given.charAt(i);
                    if (symbol == '('){
                        chars.push(symbol);
                    } else if (symbol == ')' && chars.peek() == '('){
                        chars.pop();   //removes any '(' once a ')' is found
                    }
                    //once chars is empty, we have closed the outermost bracket
                    if (chars.empty()){
                        size++; //found one item and called countElements on this item.
                        countElements(given.substring(1,i));
              
                        if (given.charAt(given.length()-1) != ')'){  // if the last character is not a close bracket include it, if it is ignore it
                            size++;
                            countElements(given.substring(i+2,given.length()));
                        } else {
                            size++;
                            countElements(given.substring(i+2,given.length()-1));
                        }
                        i = given.length();       
                    }
                }
            } else { // if the first character is not a (
                for (int i = 0; i < given.length(); i++){
                    char symbol = given.charAt(i);
                    if (symbol == ' '){  //find the first space.
                        size++; 
                        if (given.charAt(i+1) == '('){ //if the next character is an open bracket call the method again on the next element
                            size++;
                            countElements(given.substring(i+2,given.length()-1));
                        } else {
                            size++;
                        }
                        i = given.length();
                    }
                }
            }
        }
    }
    //end of countElements method
  
  /*
   * debracketer
   * splits the string into two sections, adds the value and path taken to get there to two arrays
   * then recursively calls the method again on those two sections
   * @param: the given huffman tree string and current path taken
   * @return: null
   */
    public static void debracketer(String given,String code){
        PersonalStack <Character> chars = new PersonalStack<Character>();
        if (given.length() != 0){
            char first = given.charAt(0);
            if (first == '('){ 
                chars.push(first);
                for (int i = 1; i < given.length(); i++){
                    char symbol = given.charAt(i);
                    if (symbol == '('){
                        chars.push(symbol);
                    } else if (symbol == ')' && chars.peek() == '('){
                        chars.pop();   //removes any '(' once a ')' is found
                    }
                    //once chars is empty, we have closed the outermost bracket
                    if (chars.empty()){
                        //adds the string value and path.
                        values[currentElement] = given.substring(1,i);
                        paths[currentElement] = code+"0";
                        currentElement++;  //to iterate through values and paths 
                        debracketer(given.substring(1,i),code+"0");
              
                        if (given.charAt(given.length()-1) != ')'){ // if the last character is not a close bracket include it, if it is ignore it
                            values[currentElement] = given.substring(i+2,given.length());
                            paths[currentElement] = code+"1";
                            currentElement++;
                            debracketer(given.substring(i+2,given.length()),code+"1");
                        } else {
                            values[currentElement] = given.substring(i+2,given.length()-1);
                            paths[currentElement] = code+"1";
                            currentElement++;
                            debracketer(given.substring(i+2,given.length()-1),code+"1");
                        }
                        i = given.length();       
                    }
                }
            } else { //when the first character isn't a (
                for (int i = 0; i < given.length(); i++){
                    char symbol = given.charAt(i);
                    if (symbol == ' '){ //find the first space
                        values[currentElement] = given.substring(0,i);
                        paths[currentElement] = code+"0";
                        currentElement++;
                        if (given.charAt(i+1) == '('){  //if the next character is a (, then there is another bracket to be added and the method is called again.
                            values[currentElement] = given.substring(i+2,given.length()-1);
                            paths[currentElement] = code+"1";
                            currentElement++;
                            debracketer(given.substring(i+2,given.length()-1),code+"1");
                        } else {  //if not, just add the integer into the values, and no need to call the method again.
                            values[currentElement] = given.substring(i+1,given.length());
                            paths[currentElement] = code+"1";
                            currentElement++;
                        }
                        i = given.length();
                    }
                }
            }
        }
    }
    //end of debracketer method
}
//end of decoder class