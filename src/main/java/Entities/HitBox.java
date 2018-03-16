package Entities;

import GL_Math.Vector3;

public class HitBox {
    private Vector3 minCorner;
    private Vector3 maxCorner;
    private Entity linkedEntity;


    public HitBox(Vector3 minCorner, Vector3 maxCorner, Entity linkedEntity) {
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
        this.linkedEntity = linkedEntity;
    }


    /**
     * Computes the minimal displacement necessary to stop two hitboxes from overlapping.
     * @param otherHitBox The hitbox to be checked against.
     */
    public CollisionResult checkCollision(HitBox otherHitBox) {
        if (otherHitBox == null) return CollisionResult.none;

        Vector3 minA = this.min();
        Vector3 maxA = this.max();

        Vector3 minB = otherHitBox.min();
        Vector3 maxB = otherHitBox.max();

        float penRight = maxA.x - minB.x;   //+x
        float penLeft = maxB.x - minA.x;    //-x

        float penTop = maxA.y - minB.y; //+y
        float penBottom = maxB.y - minA.y; //-y

        float penFront = maxA.z - minB.z;
        float penBack = maxB.z - minA.z;

        //Check for collision
        if ((penRight > 0 && penLeft > 0) &&
                (penTop > 0 && penBottom > 0) &&
                (penFront > 0 && penBack > 0)) {
            float xCor = correctionForPosAndNegValues(penRight,penLeft);
            float yCor = correctionForPosAndNegValues(penTop,penBottom);
            float zCor = correctionForPosAndNegValues(penFront,penBack);

            if (yCor > 0f && yCor < 0.55f) return new CollisionResult(0,yCor * 0.4f,0);

            if (Math.abs(xCor) < Math.abs(yCor) && Math.abs(xCor) < Math.abs(zCor)) {
                return new CollisionResult(xCor,0,0);
            }
            if (Math.abs(yCor) < Math.abs(xCor) && Math.abs(yCor) < Math.abs(zCor)) {
                return new CollisionResult(0,yCor,0);
            }
            if (Math.abs(zCor) < Math.abs(xCor) && Math.abs(zCor) < Math.abs(yCor)) {
                return new CollisionResult(0,0,zCor);
            }

            return new CollisionResult(xCor, yCor, zCor);
        } else {
            return CollisionResult.none;
        }


    }


    /**
     * @return The value that the entity linked to this HitBox should be translated by in a single axis given the overlapping values.
     */
    private float correctionForPosAndNegValues(float valPos, float valNeg) {
        if (valPos < valNeg) {
            return -valPos;
        } else {
            return valNeg;
        }
    }

    public Vector3 min() { return linkedEntity.pos.added(minCorner);
    }
    public Vector3 max() {
        return linkedEntity.pos.added(maxCorner);
    }


    /**
     * @return The vertices of the 12 lines surrounding the hitbox.
     * */
    Vector3[] getEdgeVertices() {
        Vector3 min = min();
        Vector3 max = max();

        return new Vector3[] {
                new Vector3(min.x,min.y,min.z), new Vector3(min.x,max.y,min.z),
                new Vector3(max.x,min.y,min.z), new Vector3(max.x,max.y,min.z),
                new Vector3(max.x,min.y,max.z), new Vector3(max.x,max.y,max.z),
                new Vector3(min.x,min.y,max.z), new Vector3(min.x,max.y,max.z),

                new Vector3(min.x,min.y,min.z), new Vector3(max.x,min.y,min.z),
                new Vector3(min.x,min.y,max.z), new Vector3(max.x,min.y,max.z),
                new Vector3(min.x,max.y,min.z), new Vector3(max.x,max.y,min.z),
                new Vector3(min.x,max.y,max.z), new Vector3(max.x,max.y,max.z),

                new Vector3(min.x,min.y,min.z), new Vector3(min.x,min.y,max.z),
                new Vector3(min.x,max.y,min.z), new Vector3(min.x,max.y,max.z),
                new Vector3(max.x,min.y,min.z), new Vector3(max.x,min.y,max.z),
                new Vector3(max.x,max.y,min.z), new Vector3(max.x,max.y,max.z),
        };
    }


    /**
     * Checks if point lies within hitbox
     * @param pos The point to check.
     * @return True if point lies inside.
     */
    public boolean doesContainPoint(Vector3 pos) {
        Vector3 min = min();
        Vector3 max = max();

        return pos.x > min.x && pos.y > min.y && pos.z > min.z &&
                pos.x < max.x && pos.y < max.y && pos.z < max.z;
    }
}
