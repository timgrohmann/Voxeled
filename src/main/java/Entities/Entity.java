package Entities;

import GL_Math.Vector3;
import Models.Model;
import Models.Vertex;

abstract public class Entity {

    protected Vector3 pos = new Vector3(0,0,0);


    protected Entity(Vector3 pos) {
        this.pos = pos;
    }

    public Vector3 getPos() {
        return pos;
    }
}
