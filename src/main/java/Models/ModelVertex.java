package Models;

import GL_Math.Vector3;
import Textures.Texture;

public class ModelVertex {
    public float x;
    public float y;
    public float z;

    public float u;
    public float v;

    public Texture texture;

    private ModelVertex(float x, float y, float z, float u, float v, Texture texture) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.u = u;
        this.v = v;

        this.texture = texture;
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

    void rotate(int by, boolean lockUV) {
        this.x -= 0.5f;
        this.z -= 0.5f;

        float oldX = this.x;
        float oldZ = this.z;

        int sin = 0;
        int cos = 0;

        if (by == 1) {
            sin = 1;
        } else if (by == -1) {
            sin = -1;
        } else if (by == 2 || by == -2) {
            cos = -1;
        }

        this.x = oldX * cos + oldZ * -sin;
        this.z = oldX * sin + oldZ * cos;
        //this.x += 1;

        this.x += 0.5f;
        this.z += 0.5f;
    }

    public ModelVertex copy() {
        return new ModelVertex(x,y,z,u,v,texture);
    }
}
