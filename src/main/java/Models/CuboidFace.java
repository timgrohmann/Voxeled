package Models;

import GL_Math.Vector2;
import GL_Math.Vector3;
import Textures.Texture;

import java.util.*;

public class CuboidFace extends Model {
    Vector2 uvOrigin;
    Vector2 uvSize;
    public Face face;
    public Texture texture;
    public boolean culling = true;

    private int rotation;

    CuboidModel cuboidModel;


    public CuboidFace(Vector2 uvOrigin, Vector2 uvSize, Face face, Texture texture, CuboidModel model, int rotation) {
        this.uvOrigin = uvOrigin.multiplied(1f/16);
        this.uvSize = uvSize.multiplied(1f/16);
        this.face = face;
        this.texture = texture;
        this.cuboidModel = model;
        this.rotation = rotation;

        generateVertices();
    }

    private void generateVertices() {
        Vector3[] corners = getCorners();

        List<ModelVertex> vertexList = new ArrayList<>();


        shiftByRotation(corners);
        vertexList.addAll(quadFromCorners(texturedVertices(corners)));


        vertices = new ModelVertex[vertexList.size()];
        vertices = vertexList.toArray(vertices);
    }

    private Vector3[] getCorners() {
        switch (this.face) {
            case TOP:
                return new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z)};
            case BOTTOM:
                return new Vector3[]{cuboidModel.origin.added(0,0,0),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0)};
            case LEFT:
                return new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,0),};
            case RIGHT:
                return new Vector3[]{cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z)};
            case FRONT:
                return new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z)};
            case BACK:
                return new Vector3[]{cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,0,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0)};
        }
        return null;
    }

    private void shiftByRotation(Vector3[] in) {
        if (Math.floorMod(rotation, 4) == 1) {
            Vector3 temp = in[3];
            in[3] = in[2];
            in[2] = in[1];
            in[1] = in[0];
            in[0] = temp;
        } else if (Math.floorMod(rotation, 4) == 3) {
            Vector3 temp = in[0];
            in[0] = in[1];
            in[1] = in[2];
            in[2] = in[3];
            in[3] = temp;
        } else if (Math.floorMod(rotation, 4) == 2) {
            Vector3 temp1 = in[0];
            Vector3 temp2 = in[1];
            in[0] = in[2];
            in[1] = in[3];
            in[2] = temp1;
            in[3] = temp2;
        }
    }

    List<ModelVertex> texturedVertices(Vector3[] untexturedPositions) {
        return Arrays.asList(
                new ModelVertex(untexturedPositions[0], uvOrigin.x, uvOrigin.y, texture),
                new ModelVertex(untexturedPositions[1], uvOrigin.x + uvSize.x, uvOrigin.y, texture),
                new ModelVertex(untexturedPositions[2], uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y, texture),
                new ModelVertex(untexturedPositions[3], uvOrigin.x, uvOrigin.y + uvSize.y, texture)
        );
    }

    private List<ModelVertex> quadFromCorners(List<ModelVertex> ms) {
        return Arrays.asList(
                ms.get(0),ms.get(3),ms.get(2),
                ms.get(2).copy(),ms.get(1),ms.get(0).copy()
        );
    }

    public Texture getTexture() {
        return texture;
    }



    @Override
    int getVertexCount(Culling culling) {
        if (this.culling) {
            if (visible(culling)) return 6;
        }else {
            return 6;
        }
        return 0;
    }

    @Override
    ModelVertex[] getModelVertices(Culling culling) {
        return vertices;
    }

    boolean visible(Culling culling) {
        return (face == Face.TOP && culling.top) ||
                (face == Face.BOTTOM && culling.bottom) ||
                (face == Face.LEFT && culling.left) ||
                (face == Face.RIGHT && culling.right) ||
                (face == Face.FRONT && culling.front) ||
                (face == Face.BACK && culling.back) ||
                !this.culling;
    }

    void rotateY(int by, boolean lockUV) {
        // Front -> Right -> Back -> Left

        if (by == 0) return;

        if (face == Face.TOP && lockUV) {
            rotation += by;
            generateVertices();
        }

        for (ModelVertex vertex: vertices) {
            vertex.rotate(by, lockUV);
        }

        if (by == 1) { // RIGHT TURN
            face = face.right().right().right();
        }

        if (by == 2) { // HALF TURN
            face = face.right().right();
        }

        if (by == -1) { // LEFT TURN
            face = face.right();
        }


    }

    public enum Face{
        TOP("up"), BOTTOM("down"), LEFT("west"), RIGHT("east"), FRONT("north"), BACK("south");
        String rawValue;
        static Map<Face, Face> rightTurn = new HashMap<>();
        static {
            rightTurn.put(FRONT, RIGHT);
            rightTurn.put(RIGHT, BACK);
            rightTurn.put(BACK, LEFT);
            rightTurn.put(LEFT, FRONT);
            rightTurn.put(TOP, TOP);
            rightTurn.put(BOTTOM, BOTTOM);
        }

        Face(String rawValue) {
            this.rawValue = rawValue;
        }

        Face right() {
            return rightTurn.get(this);
        }

        public Face opposite() {
            switch (this) {
                case TOP: return BOTTOM;
                case BOTTOM: return TOP;
                case LEFT: return RIGHT;
                case RIGHT: return LEFT;
                case FRONT: return FRONT;
                case BACK: return BACK;
            }
            throw new RuntimeException("[Invalid state] Invalid Face!");
        }
    }
}
