/**
 * DecoderLauncher.java
 * Version 1.0
 * Author: Theo Liu
 * Date: 2020-04-30
 * Description: launches the decoder
 */

//import statements
import java.util.Scanner;
import java.io.IOException;

//Main class
class DecoderLauncher{
    public static void main(String[] args){
        System.out.println("Welcome to the .MZIP decoder!");
        
        Scanner input = new Scanner(System.in);
        String fileName;
        do {
            System.out.println("To decode a file, place it in the encoded folder, and type the name in the prompt below (excluding the .mzip):");
            System.out.println("To stop decoding files, enter nothing.");
            fileName = input.nextLine();
            try{
                Decoder a = new Decoder();
                a.decode(fileName);
            } catch (IOException e){}
        } while (!fileName.equals(""));//continue to decode until the person enters nothing
        System.out.println("thank you for using the decoder!");
    }
}