import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class LZW implements Algo {

    int[] tags = new int[100];
    int tagsCount = 0; // to know how much tags you have
    // Map<String, Integer> leterDictionary = new HashMap<String, Integer>();
    // LZW() {
    // leterDictionary.put("A", 65);
    // leterDictionary.put("B", 66);
    // leterDictionary.put("C", 67);
    // leterDictionary.put("D", 68);
    // leterDictionary.put("E", 69);
    // leterDictionary.put("F", 70);
    // leterDictionary.put("G", 71);
    // leterDictionary.put("H", 72);
    // leterDictionary.put("I", 73);
    // leterDictionary.put("J", 74);
    // leterDictionary.put("K", 75);
    // leterDictionary.put("L", 76);
    // leterDictionary.put("M", 77);
    // leterDictionary.put("N", 78);
    // leterDictionary.put("O", 79);
    // leterDictionary.put("P", 80);
    // leterDictionary.put("Q", 81);
    // leterDictionary.put("R", 82);
    // leterDictionary.put("S", 83);
    // leterDictionary.put("T", 84);
    // leterDictionary.put("U", 85);
    // leterDictionary.put("V", 86);
    // leterDictionary.put("W", 87);
    // leterDictionary.put("X", 88);
    // leterDictionary.put("Y", 89);
    // leterDictionary.put("Z", 90);
    // leterDictionary.put("a", 97);
    // leterDictionary.put("b", 98);
    // leterDictionary.put("c", 99);
    // leterDictionary.put("d", 100);
    // leterDictionary.put("e", 101);
    // leterDictionary.put("f", 102);
    // leterDictionary.put("g", 103);
    // leterDictionary.put("h", 104);
    // leterDictionary.put("i", 105);
    // leterDictionary.put("j", 106);
    // leterDictionary.put("k", 107);
    // leterDictionary.put("l", 108);
    // leterDictionary.put("m", 109);
    // leterDictionary.put("n", 110);
    // leterDictionary.put("o", 111);
    // leterDictionary.put("p", 112);
    // leterDictionary.put("q", 113);
    // leterDictionary.put("r", 114);
    // leterDictionary.put("s", 115);
    // leterDictionary.put("t", 116);
    // leterDictionary.put("u", 117);
    // leterDictionary.put("v", 118);
    // leterDictionary.put("w", 119);
    // leterDictionary.put("x", 120);
    // leterDictionary.put("y", 121);
    // leterDictionary.put("z", 122);
    // leterDictionary.put(" ", 32);
    // }

    @Override
    public void comperssion() {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        int position = 128;
        
        System.out.print("Enter the string you want to comperassion :");
        Scanner scanner = new Scanner(System.in);
        String lineToCompression = scanner.nextLine();

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
            if (subStringEnd >= 2) {

                dictionary.put(lineToCompression.substring(i, i + subStringEnd), position);
                tags[tagsCount] = dictionary.get(lineToCompression.substring(i, i + subStringEnd - 1));
                tagsCount++;
                position++;
                i += subStringEnd - 1;

            } else {
                int asciiValue = (int) lineToCompression.charAt(i);
                tags[tagsCount] = asciiValue;
                tagsCount++;
                i++;
            }

        }
        for (int i = 0; i < tagsCount; i++) {
            System.out.println(tags[i]);
        }
        //
    }

    public void decompresion() {
        Map<Integer, String> dictionary = new HashMap<Integer, String>();
        String decompString = "";
        int position = 128;

        for (int i = 0; i < tagsCount; i++) {

            if (tags[i] > 0 && tags[i] < 128) {
                if (!dictionary.containsKey(tags[i]))
                    dictionary.put(tags[i], Character.toString(tags[i]));
                decompString = decompString.concat(Character.toString(tags[i]));

                if (i > 0) {
                    String last=dictionary.get(tags[i-1]);
                    String firstChar=Character.toString(tags[i]);
                    String concat =last.concat(firstChar);
                    //here we take the last tag string and the first leter of the new tag(its only one char 0<char<128 ) and put it in the dictionary
                    dictionary.put(position, concat);
                    position++;
                }
            } else {

                if (dictionary.containsKey(tags[i])) {

                    decompString = decompString.concat(dictionary.get(tags[i]));
                    if (i > 0) {
                        String last = dictionary.get(tags[i - 1]);// to get the last add lenght to add it in dictionary
                        String firstChar = dictionary.get(tags[i]).substring(0, 1);
                        String concat =last.concat(firstChar);
                        
                        dictionary.put(position,concat);
                        position++;
                    }

                } else {
                    String Unkown = dictionary.get(tags[i-1]);

                    Unkown = Unkown.concat(Unkown.substring(0, 1));

                    dictionary.put(position, Unkown);
                    position++;
                    decompString = decompString.concat(Unkown);
                }

            }
            // dictionary.forEach((key, value) -> System.out.println(key + " " + value));


        }
        System.out.println(decompString);

    }
}