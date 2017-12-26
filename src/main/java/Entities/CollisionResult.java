package Entities;

public class CollisionResult{
    public float xCorr;
    public float yCorr;
    public float zCorr;

    public static CollisionResult none = new CollisionResult(0, 0,0);

    public CollisionResult(float xCorr, float yCorr, float zCorr) {
        this.xCorr = xCorr;
        this.yCorr = yCorr;
        this.zCorr = zCorr;
    }
}