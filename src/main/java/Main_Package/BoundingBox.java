package Main_Package;

import GL_Math.Vector3;

class BoundingBox {
    private final Vector3 minCorner;
    private final Vector3 maxCorner;

    private BoundingBox(Vector3 minCorner, Vector3 maxCorner) {
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
    }

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this(new Vector3(minX,maxY,minZ),new Vector3(maxX,maxY,maxZ));
    }
}
