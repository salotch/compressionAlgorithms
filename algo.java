import java.util.Scanner;

public class algo {
    static void comperssion() {
        System.out.print("Enter the string you want to comperassion :");
        Scanner scanner = new Scanner(System.in);
        String lineToCompression = scanner.nextLine();
        System.out.println(lineToCompression);
        String searchBuffer = "";
        String[][] tags = new String[100][3];

        int tagsNumber = 0;
        int lengthOfLine = lineToCompression.length();

        for (int i = 0; i < lengthOfLine ; i++) {

            int searchLength = searchBuffer.length();

            if (searchLength == 0) {
                tags[tagsNumber][0] = "0";
                tags[tagsNumber][1] = "0";
                tags[tagsNumber][2] = lineToCompression.substring(i, 1);
                searchBuffer = searchBuffer.concat(lineToCompression.substring(i, 1));
                tagsNumber++;

            } else {
                int possibleLength = Math.min(searchLength, (lengthOfLine - searchLength));
                while (possibleLength > 0) {
                    int found = searchBuffer.lastIndexOf(lineToCompression.substring(i, i + possibleLength));

                    if (found != -1 && possibleLength >= 1) {
                        tags[tagsNumber][0] = Integer.toString((searchLength - found));
                        tags[tagsNumber][1] = Integer.toString(possibleLength);
                        System.out.println("iam possiblelength" + possibleLength);
                        
                        try {
                            tags[tagsNumber][2] = lineToCompression.substring(i + possibleLength,
                                    possibleLength + i + 1);
                            searchBuffer = searchBuffer.concat(lineToCompression.substring(i, possibleLength + i + 1));
                        } catch (Exception e) {

                            searchBuffer = searchBuffer.concat(lineToCompression.substring(i, i + 1));
                        }

                        System.out.println("search buffer " + searchBuffer);
                        i += possibleLength;
                        tagsNumber++;
                        possibleLength = 0;

                    } else if (found == -1 && possibleLength == 1) {

                        tags[tagsNumber][0] = "0";
                        tags[tagsNumber][1] = "0";
                        tags[tagsNumber][2] = lineToCompression.substring(i, i + 1);
                        searchBuffer = searchBuffer.concat(lineToCompression.substring(i, i + 1));
                        tagsNumber++;
                    }
                    possibleLength--;
                }
            }
        }

        for (int i = 0; i < tagsNumber; i++) {
            for (int j = 0; j < 3; j++) {

                System.out.print(tags[i][j]);
                if (j < 2)
                    System.out.print(", ");
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
        // String s = "salma";
        // System.out.println(s.substring(4,6));
        int t;
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter the number of test cases: ");
        t = scanner.nextInt();
        while (t > 0) {
            comperssion();
            t--;
        }

    }

}