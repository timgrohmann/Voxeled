package GUI;

public class GUITextureDescriptor {
    public String texturePath;
    public boolean interpolate;

    public GUITextureDescriptor(String texturePath, boolean interpolate) {
        this.texturePath = texturePath;
        this.interpolate = interpolate;
    }
}
