import java.util.Scanner;

public class algo {
    static int tagsNumber=0;
    static String[][] tags = new String[100][3];
    public void comperssion() {
        System.out.print("Enter the string you want to comperassion :");
        Scanner scanner = new Scanner(System.in);
        String lineToCompression = scanner.nextLine();
        String searchBuffer = "";
        

        int lengthOfLine = lineToCompression.length();
        for (int i = 0; i < lengthOfLine ; i++) {

            int searchLength = searchBuffer.length();
            if (searchLength == 0) {
                tags[tagsNumber][0] = "0";
                tags[tagsNumber][1] = "0";
                tags[tagsNumber][2] = lineToCompression.substring(i, 1);
                searchBuffer = searchBuffer.concat(lineToCompression.substring(i, 1));
                tagsNumber++;

            }else {
                int possibleLength = Math.min(searchLength, (lengthOfLine - searchLength));
                while (possibleLength > 0) {
                    int found = searchBuffer.lastIndexOf(lineToCompression.substring(i, i + possibleLength));

                    if (found != -1 && possibleLength >= 1) {
                        
                        tags[tagsNumber][0] = Integer.toString((searchLength - found));
                        tags[tagsNumber][1] = Integer.toString(possibleLength);
                        try {
                            tags[tagsNumber][2] = lineToCompression.substring(i + possibleLength,possibleLength + i + 1);
                            searchBuffer = searchBuffer.concat(lineToCompression.substring(i, possibleLength + i + 1));
                        } catch (Exception e) {
                            searchBuffer = searchBuffer.concat(lineToCompression.substring(i, i + 1));
                        }
                        i += possibleLength;
                        tagsNumber++;
                        possibleLength = 0;
                    }
                    else if (found == -1 && possibleLength == 1) {

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
    void decompresion(){
        String decompline ="";
        for(int i =0;i<tagsNumber;i++){
            if(tags[i][0]==tags[i][1] && tags[i][0]=="0"){
                decompline+=tags[i][2];
            }
            else{
                char[] result=decompline.toCharArray();
                int index=decompline.length()-Integer.parseInt(tags[i][0]);
                for(int k=0;k<Integer.parseInt(tags[i][1]);k++){
                    decompline+=result[index];
                    ++index;
                }
                if(tags[i][2]!=null)
                decompline+=tags[i][2];
            }

        }
        System.out.println(decompline);

    }

    public static void main(String[] args) {
        int t;
        algo lz77 =new algo();
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter the number of test cases: ");
        t = scanner.nextInt();
        while (t > 0) {
            lz77.comperssion();
            System.out.println("Do you want to decomprasion it ?!(yes/no)");
            String ans=scanner.next();
            if(ans.equalsIgnoreCase("yes"))
                lz77.decompresion();
            t--;
        }
    }

}