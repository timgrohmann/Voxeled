package Textures;

import GL_Math.Vector3;

public class Texture{
    public int layer = -1;

    public final String name;
    public final Vector3 blendColor;

    public Texture(String s, boolean foliage) {
        name = s;
        if (foliage) {
            blendColor = new Vector3(0.3f,0.7f,0f);
        } else {
            blendColor = new Vector3(1,1,1);
        }
    }

    public Texture(String name) {
        this(name,false);
    }
}
