package io.github.aspwil.pixelperfectupscale;
/*
the code for this class was provided by Lê Hoàng Dững on my stack exchange question
@ https://stackoverflow.com/questions/62506024/how-do-i-do-pixel-perfect-upscaling-in-java/62507011#62507011
many thanks to him, I had no idea how to pull this off.
*/



import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageResize {
    //black magic, I don't know how this works
    private static int[][] convertToPixels(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
        return result;
    }

    public static BufferedImage resize(BufferedImage image, int range) {
        int[][] pixels = convertToPixels(image);
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage imageResult = new BufferedImage(width * range, height * range, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width * range; x++) {
            for (int y = 0; y < height * range; y++) {
                imageResult.setRGB(x, y, pixels[y / range][x / range]);
            }
        }
        return imageResult;
    }

}
