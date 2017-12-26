package Entities;

import GL_Math.Vector3;
import Models.Model;
import Models.Vertex;

abstract public class DrawableEntity extends Entity {
    abstract Vertex[] getVertices();
    abstract int vertexCount();

    protected DrawableEntity(Vector3 pos) {
        super(pos);
    }

    public Model model;
}
