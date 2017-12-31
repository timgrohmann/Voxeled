package Entities;

import GL_Math.Vector3;
import Models.EntityModel;
import Models.Vertex;

abstract public class DrawableEntity extends Entity {
    public abstract Vertex[] getVertices();
    abstract int vertexCount();

    protected DrawableEntity(Vector3 pos) {
        super(pos);
    }

    public EntityModel model;
}
