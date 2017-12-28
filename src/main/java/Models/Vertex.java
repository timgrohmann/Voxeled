package Models;

import GL_Math.Vector3;
import Textures.Texture;

public class Vertex {
    public final float x;
    public final float y;
    public final float z;

    public final float u;
    public final float v;

    public final Texture texture;

    public Vertex(ModelVertex modelVertex, Vector3 position, Model model, Texture texture) {
        this.x = modelVertex.x + position.x - model.origin.x;
        this.y = modelVertex.y + position.y - model.origin.y;
        this.z = modelVertex.z + position.z - model.origin.z;

        this.u = modelVertex.u;
        this.v = modelVertex.v;

        this.texture = texture;
    }

    public Vertex(Vector3 position, float u, float v, Texture texture) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;

        this.u = u;
        this.v = v;

        this.texture = texture;
    }

    public Vertex(Vector3 modelVertex, Vector3 position, Model model) {
        this.x = modelVertex.x + position.x - model.origin.x;
        this.y = modelVertex.y + position.y - model.origin.y;
        this.z = modelVertex.z + position.z - model.origin.z;

        this.u = 0;
        this.v = 0;

        this.texture = null;
    }
}
