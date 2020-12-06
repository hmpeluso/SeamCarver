// import com.sun.java.util.jar.pack.Package.File;
// import java.io.File;


/**
 * Example program that uses SeamCarver and RGBFileFormat.
 *
 * @author Chris Mayfield
 * @version 10/26/2020
 */
public class Main {

    /**
     * Read an image from a file and shrink its size.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        
        // save the energy matrix for a provided example
        try {
            Picture p1 = RGBFileFormat.load("6x5.txt");
            SeamCarver s1 = new SeamCarver(p1);
            RGBFileFormat.toCSV("6x5.csv", s1.energyMatrix());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // test finding vertical and/or horizontal seams
        Picture p2 = new Picture("HJoceanSmall.png");
        SeamCarver s2 = new SeamCarver(p2);
        for (int i = 0; i < 150; i++) {
            int[] seam = s2.findVerticalSeam();
            s2.removeVerticalSeam(seam);
        }
        s2.getPicture().show();
    }

}
