package Models;

import GL_Math.Vector3;

public class BlockFaceModel {

    private final Side side;

    public BlockFaceModel(Side side) {
        this.side = side;
    }

    static public ModelVertex[] getVertices(Side side) {
        ModelVertex[] vertices = new ModelVertex[6];
        switch (side) {
            case TOP:
                vertices = quadFrom(topLeftBack,topRightBack,topRightFront,topLeftFront);
                break;
            case BOTTOM:
                vertices = quadFrom(bottomLeftBack, bottomLeftFront, bottomRightFront, bottomRightBack);
                break;
            case LEFT:
                vertices = quadFrom(bottomLeftBack,topLeftBack,topLeftFront,bottomLeftFront);
                break;
            case RIGHT:
                vertices = quadFrom(bottomRightFront, topRightFront, topRightBack, bottomRightBack);
                break;
            case FRONT:
                vertices = quadFrom(bottomLeftFront, topLeftFront, topRightFront, bottomRightFront);
                break;
            case BACK:
                vertices = quadFrom(bottomRightBack, topRightBack, topLeftBack, bottomLeftBack);
                break;
        }
        return vertices;
    }

    static public Vector3[] getEdgeVertices(Side side) {
        Vector3[] vertices = new Vector3[8];
        switch (side) {
            case TOP:
                vertices = new Vector3[]{topLeftBack, topRightBack,
                        topRightBack, topRightFront,
                        topRightFront,topLeftFront,
                        topLeftFront,topLeftBack};
                break;
            case BOTTOM:
                vertices = new Vector3[]{bottomLeftBack, bottomLeftFront, bottomLeftFront, bottomRightFront, bottomRightFront, bottomRightBack, bottomRightBack, bottomLeftBack};
                break;
            case LEFT:
                vertices = new Vector3[]{bottomLeftBack, topLeftBack, topLeftBack, topLeftFront, topLeftFront, bottomLeftFront, bottomLeftFront, bottomLeftBack};
                break;
            case RIGHT:
                vertices = new Vector3[]{bottomRightFront, topRightFront, topRightFront, topRightBack, topRightBack, bottomRightBack, bottomRightBack, bottomRightFront};
                break;
            case FRONT:
                vertices = new Vector3[]{bottomLeftFront, topLeftFront, topLeftFront, topRightFront, topRightFront, bottomRightFront, bottomRightFront, bottomLeftFront};
                break;
            case BACK:
                vertices = new Vector3[]{bottomRightBack, topRightBack, topRightBack, topLeftBack, topLeftBack, bottomLeftBack, bottomLeftBack, bottomRightBack};
                break;
        }
        return vertices;
    }

    private static final Vector3 bottomLeftBack = new Vector3(0,0,0);
    private static final Vector3 bottomRightBack = new Vector3(1,0,0);
    private static final Vector3 bottomRightFront = new Vector3(1,0,1);
    private static final Vector3 bottomLeftFront = new Vector3(0,0,1);

    private static final Vector3 topLeftBack = new Vector3(0,1,0);
    private static final Vector3 topRightBack = new Vector3(1,1,0);
    private static final Vector3 topRightFront = new Vector3(1,1,1);
    private static final Vector3 topLeftFront = new Vector3(0,1,1);

    private static ModelVertex[] quadFrom(Vector3 c1, Vector3 c2, Vector3 c3, Vector3 c4) {
        return new ModelVertex[] {
                new ModelVertex(c4,1,1),
                new ModelVertex(c3,1,0),
                new ModelVertex(c2,0,0),
                new ModelVertex(c2,0,0),
                new ModelVertex(c1,0,1),
                new ModelVertex(c4,1,1),
        };
    }


    public enum Side {
        TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK
    }
}
