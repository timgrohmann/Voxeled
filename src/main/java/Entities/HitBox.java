package Entities;

import GL_Math.Vector3;

public class HitBox {
    Vector3 size;
    Vector3 centerOffset;
    Entity linkedEntity;

    public HitBox(Vector3 size, Entity linkedEntity) {
        this(size,linkedEntity,Vector3.zero());
    }

    public HitBox(Vector3 size, Entity linkedEntity, Vector3 centerOffset) {
        this.size = size;
        this.linkedEntity = linkedEntity;
        this.centerOffset = centerOffset;
    }

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

    private Vector3 min() {
        return linkedEntity.pos.added(centerOffset).added(size.multiplied(-0.5f));
    }
    private Vector3 max() {
        return linkedEntity.pos.added(centerOffset).added(size.multiplied(0.5f));
    }


}
