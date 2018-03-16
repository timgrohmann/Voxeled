package Main_Package;


import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {
    public static byte[] getFromFile(String relativePath) {
        try {
            File wd = workingDir();
            File thisPath = new File(wd, relativePath);
            return Files.readAllBytes(thisPath.toPath());
        } catch (Exception ignored) {

        }
        return new byte[0];
    }

    public static void writeToFile(byte[] bs, String relativePath) {
        try {
            File wd = workingDir();
            File thisPath = new File(wd, relativePath);
            FileOutputStream out = new FileOutputStream(thisPath);
            out.write(bs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.format("It didnt work!");
        }
    }

    private static File workingDir() {
        URL mainURL = FileManager.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            File jarPath = new File(mainURL.toURI()).getParentFile();
            return new File(jarPath, "data");
        } catch (Exception ignored) {
            ignored.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
