package grayscaleCompression_decompression;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

public class ImageDecompression {

    public static void decompress(String compressedFilePath, String outputFilePath) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(compressedFilePath))) {
            // Read metadata
            int blockSize = dis.readInt();
            int width = dis.readInt();
            int height = dis.readInt();
            int codebookSize = dis.readInt();

            // Read codebook
            List<int[][]> codebook = new ArrayList<>();
            for (int i = 0; i < codebookSize; i++) {
                int[][] block = new int[blockSize][blockSize];
                for (int row = 0; row < blockSize; row++) {
                    for (int col = 0; col < blockSize; col++) {
                        block[row][col] = dis.readInt();
                    }
                }
                codebook.add(block);
            }

            // Read compressed indices
            int totalBlocks = (int) Math.ceil((double) height / blockSize) * (int) Math.ceil((double) width / blockSize);
            int[] compressedIndices = new int[totalBlocks];
            for (int i = 0; i < totalBlocks; i++) {
                compressedIndices[i] = dis.readInt();
            }

            // Reconstruct the image from blocks
            int[][] decompressedImage = reconstructImage(compressedIndices, codebook, blockSize, width, height);

            // Save the decompressed image as a grayscale image
            saveImage(decompressedImage, outputFilePath);
        }
    }

    private static int[][] reconstructImage(int[] compressedIndices, List<int[][]> codebook, int blockSize, int width, int height) {
        int[][] image = new int[height][width];
        int blockIndex = 0;

        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                int[][] block = codebook.get(compressedIndices[blockIndex++]);

                for (int i = 0; i < blockSize; i++) {
                    for (int j = 0; j < blockSize; j++) {
                        if (y + i < height && x + j < width) {
                            image[y + i][x + j] = block[i][j];
                        }
                    }
                }
            }
        }

        return image;
    }

    private static void saveImage(int[][] image, String outputFilePath) throws IOException {
        int height = image.length;
        int width = image[0].length;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grayValue = image[y][x];
                int rgb = (grayValue << 16) | (grayValue << 8) | grayValue;
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        ImageIO.write(bufferedImage, "jpg", new File(outputFilePath));
    }
   
}
