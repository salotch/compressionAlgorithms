import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LZW implements Algo {

    int[] tags = new int[100];
    int tagsCount = 0; // to know how much tags you have

    @Override
    public void comperssion() {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        int position = 128;
        String lineToCompression = "";
        try {
            System.out.println("what is the file name: ");
            String filename = scanner.nextLine();
            File myObj = new File(filename + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                lineToCompression = myReader.nextLine();
                // System.out.println(lineToCompression);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            return;
            
        }

        int lengthOfLine = lineToCompression.length();
        for (int i = 0; i < lengthOfLine;) {

            if (!dictionary.containsKey(lineToCompression.substring(i, i + 1)))
                dictionary.put(lineToCompression.substring(i, i + 1), (int) lineToCompression.charAt(i));

            int subStringEnd = 1;

            try {
                while (dictionary.containsKey(lineToCompression.substring(i, i + subStringEnd))) {
                    subStringEnd++;
                }
            } catch (Exception e) {
                tags[tagsCount] = dictionary.get(lineToCompression.substring(i, i + subStringEnd - 1));
                tagsCount++;
                position++;
                i += subStringEnd;
                continue;
            }

                dictionary.put(lineToCompression.substring(i, i + subStringEnd), position);
                tags[tagsCount] = dictionary.get(lineToCompression.substring(i, i + subStringEnd - 1));
                tagsCount++;
                position++;
                i += subStringEnd - 1;
        }

        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            for (int i = 0; i < tagsCount; i++) {
                myWriter.write((int)tags[i]+"\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return;
        }
    }

    public void decompresion() {
        Map<Integer, String> dictionary = new HashMap<Integer, String>();
        String decompString = "";
        int position = 128;
        Scanner scanner =new Scanner(System.in);
        try {
            System.out.println("what is the file name you want to read from it the tags: ");
            String filename = scanner.nextLine();
            File myObj = new File(filename + ".txt");
            Scanner myReader = new Scanner(myObj);
            int i=0;
            while (myReader.hasNextLine()) {
                tags[i] = Integer.valueOf(myReader.nextLine());
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            
        }
        for (int i = 0; i < tagsCount; i++) {

            if (tags[i] > 0 && tags[i] < 128) {
                if (!dictionary.containsKey(tags[i]))
                    dictionary.put(tags[i], Character.toString(tags[i]));
                decompString = decompString.concat(Character.toString(tags[i]));

                if (i > 0) {
                    String last = dictionary.get(tags[i - 1]);
                    String firstChar = Character.toString(tags[i]);
                    String concat = last.concat(firstChar);
                    // here we take the last tag string and the first leter of the new tag(its only
                    // one char 0<char<128 ) and put it in the dictionary
                    dictionary.put(position, concat);
                    position++;
                }
            } else {

                if (dictionary.containsKey(tags[i])) {

                    decompString = decompString.concat(dictionary.get(tags[i]));
                    if (i > 0) {
                        String last = dictionary.get(tags[i - 1]);// to get the last add lenght to add it in dictionary
                        String firstChar = dictionary.get(tags[i]).substring(0, 1);
                        String concat = last.concat(firstChar);

                        dictionary.put(position, concat);
                        position++;
                    }

                } else {
                    String Unkown = dictionary.get(tags[i - 1]);

                    Unkown = Unkown.concat(Unkown.substring(0, 1));

                    dictionary.put(position, Unkown);
                    position++;
                    decompString = decompString.concat(Unkown);
                }
            }
            // dictionary.forEach((key, value) -> System.out.println(key + " " + value));

        }
        try {
            FileWriter myWriter = new FileWriter("filename1.txt");
                myWriter.write(decompString);
            
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        

    }
}