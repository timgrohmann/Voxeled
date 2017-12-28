package Models;

import GL_Math.Vector3;

public abstract class Model {
    public ModelVertex[] vertices;

    public Vector3 origin;

    abstract int getVertexCount(Culling culling);
    abstract ModelVertex[] getModelVertices(Culling culling);
}
