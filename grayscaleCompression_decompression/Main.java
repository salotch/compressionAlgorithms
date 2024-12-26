package grayscaleCompression_decompression;

import java.io.*;
import java.util.Scanner;

public class Main {
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter path file: ");
        String inputImagePath=scanner.next(); 
        System.out.println("please enter blocksize: ");
        Integer blockSize=scanner.nextInt(); 
        System.out.println("please enter codebooksize: ");
        Integer codebookSize=scanner.nextInt(); 

        String compressedFilePath = "compressed.bin";  // Compressed output file

        ImageCompression.compress("grayscaleCompression_decompression\\"+inputImagePath, compressedFilePath, blockSize, codebookSize);

        System.out.println("Compression complete!");
    }
}