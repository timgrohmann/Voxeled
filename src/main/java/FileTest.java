import Main_Package.FileManager;

public class FileTest {
    public static void main(String[] args) {
        new FileTest().runTest();
    }

    private void runTest() {
        byte[] bs = FileManager.getFromFile("0.player");
    }
}
