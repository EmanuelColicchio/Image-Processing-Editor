import java.awt.*;
import java.awt.image.BufferedImage;

class ImageOperations {

    /**
     * turns each pixel's red value to 0 in img
     * @param img a given BufferedImage
     * @return a new BufferedImage with no
     */
    static BufferedImage zeroRed(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int rgb = img.getRGB(x, y);
                int b = ColorOperations.getBlue(rgb);
                int g = ColorOperations.getGreen(rgb);
                newImg.setRGB(x, y, new Color(0, g, b).getRGB());
            }
        }
        return newImg;
    }

    /**
     * turns the entire image into a greyscale version of itself
     * @param img a given BufferedImage
     * @return a new BufferedImage with each pixel rgb value being converted
     * to the following greyscale formula (0.299 * red + 0.587 * green + 0.114 * blue)
     */
    static BufferedImage grayscale(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int rgb = img.getRGB(x,y);
                int r = ColorOperations.getRed(rgb);
                int g = ColorOperations.getGreen(rgb);
                int b = ColorOperations.getBlue(rgb);
                int avg = (int) ((r + g + b) / 3.0);
                Color grey = new Color(avg, avg, avg);
                newImg.setRGB(x, y, grey.getRGB());
            }
        }
        return newImg;
    }

    /**
     * inverts the color of the image to subtract each RGB value by the RGB maximum
     * @param img a given image to invert
     * @return a new BufferedImage with the properties of an inverted image
     */
    static BufferedImage invert(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int rgb = img.getRGB(x, y);
                int r = ColorOperations.getRed(rgb);
                int b = ColorOperations.getBlue(rgb);
                int g = ColorOperations.getGreen(rgb);
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                newImg.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        return newImg;
    }

    /**
     * Mirrors the image on either its vertical or horizontal axis
     * @param img a given BufferedImage
     * @param dir a inputted direction (either horizontal or vertical)
     * @return a new BufferedImage that is mirrored in the direction dir
     */
    static BufferedImage mirror(BufferedImage img, MirrorMenuItem.MirrorDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        if (dir == MirrorMenuItem.MirrorDirection.VERTICAL) {
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    if(x < width / 2) newImg.setRGB(x, y, img.getRGB(x, y));
                    else newImg.setRGB(x, y, img.getRGB(width - x, y));
                }
            }
        } else {
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    if(y < height / 2) newImg.setRGB(x, y, img.getRGB(x, y));
                    else newImg.setRGB(x, y, img.getRGB(x, height - y));
                }
            }
        }
        return newImg;
    }

    /**
     * rotates the image img either clockwise or counter-clockwise indicated by dir
     * @param img a given image to be rotated
     * @param dir an inputted direction (either clockwise or counter-clockwise)
     * @return a new BufferedImage of the newly rotated image
     */
    static BufferedImage rotate(BufferedImage img, RotateMenuItem.RotateDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        if (dir == RotateMenuItem.RotateDirection.CLOCKWISE) {
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    newImg.setRGB(x, y, ColorOperations.getColorAtPos(img, y, (width - 1) - x).getRGB());
                }
            }
        } else {
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    newImg.setRGB(x, y, ColorOperations.getColorAtPos(img, (height - 1) - y ,x).getRGB());
                }
            }
        }
        return newImg;
    }

    /**
     * repeats an image n amount of times in the indicated direction dir
     * @param img a given img to be repeated
     * @param n an integer to indicate the amount of times an image should be repeated
     * @param dir an indicator for the direction to repeat the image in (either vertical or horizontal)
     * @return a new BufferedImage that repeats n times in direction dir
     */
    static BufferedImage repeat(BufferedImage img, int n, RepeatMenuItem.RepeatDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg;
        // newImg must be instantiated in both branches with the correct dimensions.
        if (dir == RepeatMenuItem.RepeatDirection.HORIZONTAL) {
            newImg = new BufferedImage(width * n, height, BufferedImage.TYPE_INT_RGB);
            for(int i = 0; i < n; i++) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        newImg.setRGB((width * i) + x, y, ColorOperations.getColorAtPos(img, x, y).getRGB());
                    }
                }
            }
        } else {
            newImg = new BufferedImage(width, height * n, BufferedImage.TYPE_INT_RGB);
            for(int i = 0; i < n; i++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        newImg.setRGB(x, (height * i) + y, ColorOperations.getColorAtPos(img, x, y).getRGB());
                    }
                }
            }
        }
        return newImg;
    }

    /**
     * Zooms in on the image. The zoom factor increases in multiplicatives of 10% and
     * decreases in multiplicatives of 10%.
     *
     * @param img        the original image to zoom in on. The image cannot be already zoomed in
     *                   or out because then the image will be distorted.
     * @param zoomFactor The factor to zoom in by.
     * @return the zoomed in image.
     */
    static BufferedImage zoom(BufferedImage img, double zoomFactor) {
        int newImageWidth = (int) (img.getWidth() * zoomFactor);
        int newImageHeight = (int) (img.getHeight() * zoomFactor);
        BufferedImage newImg = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newImageWidth, newImageHeight, null);
        g2d.dispose();
        return newImg;
    }
}
