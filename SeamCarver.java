import java.awt.Color;

/**
 * Seam carving implementation based on the algorithm discovered by Shai Avidan
 * and Ariel Shamir (SIGGRAPH 2007).
 * 
 * @author Hannah Peluso
 * @version - 10-20-20
 */
public class SeamCarver extends ProvidedCode {

    /**
     * Construct a SeamCarver object.
     * 
     * @param picture initial picture
     */
    public SeamCarver(Picture picture) {
        super(picture);
    }

    /**
     * Computes the energy of a pixel.
     * 
     * @param x column index
     * @param y row index
     * @return energy value
     */
    public int energy(int x, int y) {
        // redid this method with the algorithm from dr. mayfield.
        int eValue;
        if (x < 0 || x >= picture.width()) {
            throw new IndexOutOfBoundsException(
                    "x = " + x + ", width = " + picture.width());
        }
        if (y < 0 || y >= picture.height()) {
            throw new IndexOutOfBoundsException(
                    "y = " + y + ", height = " + picture.height());
        }

        // checks for border pixels and alters x and y values for them
        // accordingly.
        int right = x + 1;
        int left = x - 1;
        int top = y + 1;
        int bottom = y - 1;

        if (right >= width) {
            right = 0;
        }
        if (left < 0) {
            left = width - 1;
        }
        if (top >= height) {
            top = 0;
        }
        if (bottom < 0) {
            bottom = height - 1;
        }  

        // math is same for every case now.
        int xGradient;
        int yGradient;
        Color c1 = picture.get(right, y);
        Color c2 = picture.get(left, y);
        int rDiffX = c1.getRed() - c2.getRed();
        rDiffX = rDiffX * rDiffX;
        int gDiffX = c1.getGreen() - c2.getGreen();
        gDiffX = gDiffX * gDiffX;
        int bDiffX = c1.getBlue() - c2.getBlue();
        bDiffX = bDiffX * bDiffX;
        xGradient = rDiffX + gDiffX + bDiffX;

        Color c3 = picture.get(x, top);
        Color c4 = picture.get(x, bottom);
        int rDiffY = c3.getRed() - c4.getRed();
        rDiffY = rDiffY * rDiffY;
        int gDiffY = c3.getGreen() - c4.getGreen();
        gDiffY = gDiffY * gDiffY;
        int bDiffY = c3.getBlue() - c4.getBlue();
        bDiffY = bDiffY * bDiffY;
        yGradient = rDiffY + gDiffY + bDiffY;

        eValue = xGradient + yGradient;

        return eValue;

    }

    /**
     * @return the energy matrix for the picture
     */
    @Override
    public int[][] energyMatrix() {
        int[][] eMatrix = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                eMatrix[x][y] += energy(x, y);
            }
        }

        return eMatrix;
    }

    /**
     * Removes a horizontal seam from current picture.
     * 
     * @param seam sequence of row indices
     */
    public void removeHorizontalSeam(int[] seam) {
        // tried flipping this algorithm to opposite of remove horizontal ( like
        // i initially had used both methods) and it didn't work so came back to
        // original form.
        validateSeam(seam, width, height);
        Picture copy = new Picture(width, height - 1);
        int k = 0;
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                if (y != seam[x]) {
                    copy.set(x, k, picture.get(x, y));
                    k++;
                }
            }
            // this is where I realized counter was not being reset and was only
            // going to continue increasing the new x value when it needed to
            // only increase up to a max of he width value.
            k = 0;
        }

        this.height = copy.height();
        this.picture = copy;
    } 

    /**
     * Removes a vertical seam from the current picture.
     * 
     * @param seam sequence of column indices
     */
    public void removeVerticalSeam(int[] seam) {
        // tried switching this method to a new algorithm as well and it worked.
        validateSeam(seam, height, width);
        Picture copy = new Picture(width - 1, height);

        // instead of skipping a y value as i did in remove horizontal, i copied
        // up to a seam value, then made a new loop to copy past that seam
        // value.
        for (int y = 0; y < copy.height(); y++) {
            for (int x = 0; x < seam[y]; x++) {
                copy.set(x, y, this.picture.get(x, y));
            }
            for (int x = seam[y]; x < copy.width(); x++) {

                copy.set(x, y, this.picture.get(x + 1, y));
            }
        }

        this.width = copy.width();
        this.picture = copy;
    }

    /**
     * Validates the seam array prior to removing.
     * 
     * @param seam sequence of row/column indices
     * @param length expected length of the array
     * @param pixels current size of the dimension
     */
    private static void validateSeam(int[] seam, int length, int pixels) {
        if (seam == null) {
            throw new NullPointerException();
        }
        if (seam.length != length) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (pixels == 1) {
            throw new IllegalArgumentException();
        }
    }

}
