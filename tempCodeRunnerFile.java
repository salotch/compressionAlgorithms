import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // ABAABABBAABAABAAAABABBBBBBBB
        int t;
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter the number of test cases: ");
        t = scanner.nextInt();
        // t=3;
        while (t > 0) {
            byte number;
            System.out.print("enter the number of the number of algorithm you want to try:\n1.LZ77\n2.LZW\n3.Standerd Huffman \n: ");
            number = scanner.nextByte();
            // number=2;
            if (number == 1) {
                LZ77 lz77 = new LZ77();
                lz77.comperssion();
                System.out.println("Do you want to decomprasion it ?!(yes/no)");
                String ans = scanner.next();
                if (ans.equalsIgnoreCase("yes"))
                    lz77.decompresion();
                t--;
            }
            else if (number==2) {
                LZW lzw=new LZW();
                lzw.comperssion();
                System.out.println("Do you want to decomprasion it ?!(yes/no)");
                String ans = scanner.next();
                if (ans.equalsIgnoreCase("yes"))
                    lzw.decompresion();
                t--;
            }
            else if (number==3) {
                standerdHuffman h=new standerdHuffman();
                h.comperssion();
                // System.out.println("Do you want to decomprasion it ?!(yes/no)");
                // String ans = scanner.next();
                // if (ans.equalsIgnoreCase("yes"))
                //     lzw.decompresion();
                t--;
            }
        }
    
    }
}
