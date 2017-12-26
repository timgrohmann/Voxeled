package Models;

import GL_Math.Vector3;
import Textures.Texture;

public class Vertex {
    public final float x;
    public final float y;
    public final float z;

    public final float u;
    public final float v;

    public Vertex(ModelVertex modelVertex, Vector3 position, Model model, Texture texture) {
        this.x = modelVertex.x + position.x - model.center.x;
        this.y = modelVertex.y + position.y - model.center.y;
        this.z = modelVertex.z + position.z - model.center.z;

        this.u = texture.convertedU(modelVertex.u);
        this.v = texture.convertedV(modelVertex.v);
    }

    public Vertex(Vector3 modelVertex, Vector3 position, Model model) {
        this.x = modelVertex.x + position.x - model.center.x;
        this.y = modelVertex.y + position.y - model.center.y;
        this.z = modelVertex.z + position.z - model.center.z;

        this.u = 0;
        this.v = 0;
    }
}
