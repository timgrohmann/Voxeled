package Entities;

import GL_Math.Vector3;

abstract public class Entity {

    protected Vector3 pos = Vector3.zero();
    Vector3 speed = Vector3.zero();


    protected Entity(Vector3 pos) {
        this.pos = pos;
    }

    public Vector3 getPos() {
        return pos;
    }

    public void update() {}
}
