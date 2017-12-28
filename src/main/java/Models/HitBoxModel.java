package Models;

import Entities.Entity;
import Entities.HitBox;
import GL_Math.Vector3;

public class HitBoxModel {
    private Vector3 minCorner;
    private Vector3 maxCorner;

    public HitBoxModel(Vector3 minCorner, Vector3 maxCorner) {
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
    }

    public HitBox linkedWith(Entity e) {
        return new HitBox(minCorner,maxCorner,e);
    }
}
