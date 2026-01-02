package View;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class Imageloader {

    public static BufferedImage loadImage(String path) {
        // Normalize path to just the filename for searching
        String filename = new File(path).getName();

        // List of potential paths to check
        String[] possiblePaths = {
                path, // exact path requested
                "resources/image/" + filename, // standard resources path
                "src/View/resources/image/" + filename, // inside src
                "image/" + filename, // top level image folder
                "images/" + filename, // top level images folder
                filename, // project root
                "src/" + filename, // inside src root
                "/resources/" + filename // absolute resources
        };

        for (String p : possiblePaths) {
            BufferedImage img = tryLoad(p);
            if (img != null) {
                System.out.println("Loaded image automatically from: " + p);
                return img;
            }
        }

        // Comprehensive search up the directory tree
        try {
            File current = new File(".").getCanonicalFile();
            // Check up to 3 levels up
            for (int i = 0; i < 3; i++) {
                File potential = new File(current, "resources/image/" + filename);
                if (potential.exists()) {
                    System.out.println("Found image via tree search: " + potential.getAbsolutePath());
                    return ImageIO.read(potential);
                }
                // Also try seeing if we are inside specific subdir
                potential = new File(current, "untitled9/resources/image/" + filename);
                if (potential.exists()) {
                    System.out.println("Found image via tree search: " + potential.getAbsolutePath());
                    return ImageIO.read(potential);
                }

                current = current.getParentFile();
                if (current == null)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.err.println("Could not find image: " + filename + " (Checked common locations)");
        return null;
    }

    private static BufferedImage tryLoad(String path) {
        try {
            // 1. Try as local file
            File file = new File(path);
            if (file.exists()) {
                return ImageIO.read(file);
            }

            // 2. Try as classpath resource
            URL url = Imageloader.class.getClassLoader().getResource(path);
            if (url != null) {
                return ImageIO.read(url);
            }

            // 3. Try with leading slash for classpath
            if (!path.startsWith("/")) {
                url = Imageloader.class.getClassLoader().getResource("/" + path);
                if (url != null)
                    return ImageIO.read(url);
            }

        } catch (Exception e) {
            // Ignore errors during search
        }
        return null;
    }
}
