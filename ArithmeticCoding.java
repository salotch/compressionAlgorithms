import java.io.*;
import java.util.*;

public class ArithmeticCoding {
    static class SymbolRange {
        char symbol;
        double lowRange, highRange;

        SymbolRange(char symbol, double lowRange, double highRange) {
            this.symbol = symbol;
            this.lowRange = lowRange;
            this.highRange = highRange;
        }
    }

    static Map<Character, SymbolRange> calculateSymbolRanges(Map<Character, Double> probabilities) { 
        Map<Character, SymbolRange> ranges = new HashMap<>();
        double low = 0.0;

        for (Map.Entry<Character, Double> entry : probabilities.entrySet()) {
            char symbol = entry.getKey();
            double probability = entry.getValue();
            ranges.put(symbol, new SymbolRange(symbol, low, low + probability));
            low += probability;
        }

        return ranges;
    }

    static double compress(String input, Map<Character, SymbolRange> ranges, File outputFile) throws IOException {
        double lower = 0.0, upper = 1.0, range;

        for (char symbol : input.toCharArray()) {
            SymbolRange s = ranges.get(symbol);
            range = upper - lower;
            upper = lower + range * s.highRange;
            lower = lower + range * s.lowRange;
        }

        double code = (lower + upper) / 2;

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            dos.writeDouble(code);
            dos.writeInt(input.length());
            dos.writeInt(ranges.size());

            for (SymbolRange s : ranges.values()) {
                dos.writeChar(s.symbol);
                dos.writeDouble(s.lowRange);
                dos.writeDouble(s.highRange);
            }
        } catch (Exception e) {
            System.out.println("an Error accoured " + e.getMessage());
        }
        // System.out.println(code);
        return code;
    }

    static String decompress(File inputFile) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile))) {
            double code = dis.readDouble();
            int length = dis.readInt();
            int tableSize = dis.readInt();

            Map<Character, SymbolRange> ranges = new HashMap<>();
            for (int i = 0; i < tableSize; i++) {
                char symbol = dis.readChar();
                double lowRange = dis.readDouble();
                double highRange = dis.readDouble();
                ranges.put(symbol, new SymbolRange(symbol, lowRange, highRange));
            }

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < length; i++) {
                for (SymbolRange sr : ranges.values()) {
                    if (code >= sr.lowRange && code < sr.highRange) {
                        result.append(sr.symbol);
                        double range = sr.highRange - sr.lowRange;
                        code = (code - sr.lowRange) / range;
                        break;
                    }
                }
            }
            return result.toString();

        } catch (Exception e) {
            System.out.println("error occurred while decompression process");
            return "\nthere is no result\n";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Character, Double> probabilities = new LinkedHashMap<>();

        System.out.println("Enter symbol probabilities (format: symbol probability). Enter 'done' to finish:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done"))
                break;
            String[] parts = input.split("\\s+");
            
            probabilities.put(parts[0].charAt(0), Double.parseDouble(parts[1]));
        }

        File file = new File("compressed.bin");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Compress");
            System.out.println("2. Decompress");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                if (choice == 1) {
                    System.out.print("Enter the string to compress: ");
                    String input = scanner.nextLine();
                    Map<Character, SymbolRange> ranges = calculateSymbolRanges(probabilities);
                    compress(input, ranges, file);
                    System.out.println("Compression completed! Data saved to " + file.getName());
                } else if (choice == 2) {
                    String decompressed = decompress(file);
                    System.out.println("Decompressed string: " + decompressed);
                } else if (choice == 3) {
                    break;
                } else {
                    System.out.println("invalid option Please try again");
                }
            } catch (IOException e) {
                System.out.println("error occurred: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
