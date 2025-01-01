package grayscaleCompression_decompression;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class ImageCompression {
    static class CodebookEntry {
        int[][] block;

        public CodebookEntry(int[][] block) {
            this.block = block;
        }
    }

    public static void compress(String inputFilePath, String outputFilePath, int blockSize, int codebookSize) throws IOException {
        // ديه ال هتجيب المعلومات من الصوره 
        BufferedImage image = ImageIO.read(new File(inputFilePath));
        int width = image.getWidth();
        int height = image.getHeight();

        //بحول الصوره لي ماتريكس 
        int[][] pixels = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = image.getRGB(x, y) & 0xFF; //بعمل ال بيت تويس عشان يبقي ماسك عشان اخد اخر قيمه  لان الاحمر والاخضر والازرق قد بعض فهاخد قيمه الازرق بس
                pixels[y][x] = gray;
            }
        }

        //بعد ما حولنا الصوره لي ماتريكس هنقسمها بلوك  روحي شوفي الفانكشن 
        List<int[][]> blocks = divideIntoBlocks(pixels, blockSize);

        //  هنا هطلع كودبوك هستخدم فانكشن بتاخد ال بلوكس  وكام كود محتاجين نطلعه 
        List<CodebookEntry> codebook = generateCodebook(blocks, codebookSize);

        // Replace each block with its nearest codeword index
        int[] compressedIndices = compressBlocks(blocks, codebook);

        // Save compressed file
        saveCompressedFile(outputFilePath, codebook, compressedIndices, blockSize, width, height);
    }
    // الفانكشن ديه بتاخد منك بيكسل ال هي ماتريكس الصوره وعايزه تقسميه لكام بلوك
    private static List<int[][]> divideIntoBlocks(int[][] pixels, int blockSize) {
        //هنا هعمل ليست من ال اري هخزن فيها البلوك لما اراي تتقسم
        List<int[][]> blocks = new ArrayList<>();
        // معلومه لذوذه لما تجيبي طول تو دي اري بتجيب هي كام سطر
        //ولما تجيبي لينس لاول سطر بتيجي في كام رقم وده يبقي العرض 
        int height = pixels.length, width = pixels[0].length;
        //    اول اتنين فور لوب ديه هتمش بلوك يعني مثلا هنبتدي بالسطر الصفر ولفه ال بعده من اتنين لو سيزس 2*2
        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                //  ديه ال هخزن فيها ال بلوك نفسها 
                int[][] block = new int[blockSize][blockSize];
                for (int i = 0; i < blockSize; i++) {
                    for (int j = 0; j < blockSize; j++) {
                        if (y + i < height && x + j < width) {
                            block[i][j] = pixels[y + i][x + j];
                        } else {
                            block[i][j] = 0; // ديه عشان لو كانت ال اري 5*5 وال سيز بلوك 2*2  هتخلي الرقم الناقص بصفر عشان اهندل ال بفر  
                        }
                    }
                }
                blocks.add(block);
            }
        }
        return blocks;
    }

    private static List<CodebookEntry> generateCodebook(List<int[][]> blocks, int codebookSize) {
        // ونبدا نسبلت average هنعمل بلوك عشان نحط في ال 
        int[][] avgBlock = calculateAverageBlock(blocks);
        List<CodebookEntry> codebook = new ArrayList<>();
        codebook.add(new CodebookEntry(avgBlock));
        //انا عايزه اتاكد ان codebooksize
        while (codebook.size() < codebookSize) {
            // Split codebook entries
            List<CodebookEntry> newCodebook = new ArrayList<>();
            for (CodebookEntry entry : codebook) {
                newCodebook.add(new CodebookEntry(addEpsilon(entry.block, -1)));
                newCodebook.add(new CodebookEntry(addEpsilon(entry.block, 1)));
            }

            codebook = newCodebook;

            // Reassign blocks and recalculate centroids
            boolean changed;
            do {
                changed = false;
                List<List<int[][]>> clusters = new ArrayList<>();
                for (int i = 0; i < codebook.size(); i++) {
                    clusters.add(new ArrayList<>());
                }

                for (int[][] block : blocks) {
                    int closestIndex = findClosestBlock(block, codebook);
                    clusters.get(closestIndex).add(block);
                }

                for (int i = 0; i < codebook.size(); i++) {
                    int[][] newCentroid = calculateAverageBlock(clusters.get(i));
                    if (!Arrays.deepEquals(codebook.get(i).block, newCentroid)) {
                        changed = true;
                        codebook.get(i).block = newCentroid;
                    }
                }
            } while (changed);
        }

        return codebook;
    }
    
    private static int[][] calculateAverageBlock(List<int[][]> blocks) {
        if (blocks.isEmpty()) return new int[0][0];
        int blockSize = blocks.get(0).length;
        // بصي علي مثال ال في المحاضره انا كده عملت اري من بلوك وبقي 3*3
        int[][] avgBlock = new int[blockSize][blockSize];

        for (int[][] block : blocks) {
            for (int i = 0; i < blockSize; i++) {
                for (int j = 0; j < blockSize; j++) {
                    avgBlock[i][j] += block[i][j];
                }
            }
        }

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                avgBlock[i][j] /= blocks.size();
            }
        }

        return avgBlock;
    }
    private static int[][] addEpsilon(int[][] block, int epsilon) {
        int[][] newBlock = new int[block.length][block[0].length];
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                newBlock[i][j] = block[i][j] + epsilon;
            }
        }
        return newBlock;
    }
    
    private static int findClosestBlock(int[][] block, List<CodebookEntry> codebook) {
        int closestIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < codebook.size(); i++) {
            double distance = calculateDistance(block, codebook.get(i).block);
            if (distance < minDistance) {
                minDistance = distance;
                closestIndex = i;
            }
        }

        return closestIndex;
    }
    private static double calculateDistance(int[][] block1, int[][] block2) {
        double distance = 0.0;
        for (int i = 0; i < block1.length; i++) {
            for (int j = 0; j < block1[i].length; j++) {
                distance += Math.pow(block1[i][j] - block2[i][j], 2);
            }
        }
        return Math.sqrt(distance);
    }
    private static int[] compressBlocks(List<int[][]> blocks, List<CodebookEntry> codebook) {
        int[] indices = new int[blocks.size()];
        for (int i = 0; i < blocks.size(); i++) {
            indices[i] = findClosestBlock(blocks.get(i), codebook);
        }
        return indices;
    }

    private static void saveCompressedFile(String outputFilePath, List<CodebookEntry> codebook, int[] compressedIndices, int blockSize, int width, int height) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFilePath))) {
            dos.writeInt(blockSize);
            dos.writeInt(width);
            dos.writeInt(height);
            dos.writeInt(codebook.size());

            // Save codebook
            for (CodebookEntry entry : codebook) {
                for (int[] row : entry.block) {
                    for (int value : row) {
                        dos.writeInt(value);
                    }
                }
            }

            // Save compressed indices
            for (int index : compressedIndices) {
                dos.writeInt(index);
            }
        }
    }



}
