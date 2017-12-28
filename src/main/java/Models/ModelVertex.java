package Models;

import GL_Math.Vector3;
import Textures.Texture;

public class ModelVertex {
    public final float x;
    public final float y;
    public final float z;

    public final float u;
    public final float v;

    public Texture texture;

    public ModelVertex(float x, float y, float z, float u, float v) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.u = u;
        this.v = v;
    }
    public ModelVertex(Vector3 vec, float u, float v) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;

        this.u = u;
        this.v = v;
    }
    public ModelVertex(Vector3 vec, float u, float v, Texture texture) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;

        this.u = u;
        this.v = v;
        this.texture = texture;
    }

}
