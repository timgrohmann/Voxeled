package Textures;

public class Texture {
    private final int posX;
    private final int posY;

    private String name = "NO_NAME";

    private static final int horizontalTexCount = 16;
    private static final int verticalTexCount = 16;

    public Texture(int posX, int posY, String name) {
        this.posX = posX;
        this.posY = posY;
        this.name = name;
    }

    public Texture(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Texture(int index) {
        this.posY = index / 16;
        this.posX = index - posY * 16;
    }

    public float convertedU(float u) {
        return ((float) posX + u)/ horizontalTexCount;
    }
    public float convertedV(float v) {
        return ((float) posY + v)/ verticalTexCount;
    }


}
