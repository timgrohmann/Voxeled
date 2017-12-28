package Models;

import GL_Math.Vector2;
import GL_Math.Vector3;
import Textures.Texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CuboidFace extends Model {
    Vector2 uvOrigin;
    Vector2 uvSize;
    public Face face;
    public Texture texture;
    public boolean culling = true;

    private final int rotation;

    CuboidModel cuboidModel;


    public CuboidFace(Vector2 uvOrigin, Vector2 uvSize, Face face, Texture texture, CuboidModel model, int rotation) {
        this.uvOrigin = uvOrigin.multiplied(1f/16);
        this.uvSize = uvSize.multiplied(1f/16);
        this.face = face;
        this.texture = texture;
        this.cuboidModel = model;
        this.rotation = rotation;

        Vector3[] corners = null;

        List<ModelVertex> vertexList = new ArrayList<>();

        switch (face) {
            case TOP:
                corners = new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z)};
                break;
            case BOTTOM:
                corners = new Vector3[]{cuboidModel.origin.added(0,0,0),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0)};
                break;
            case LEFT:
                corners = new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,0),};
                break;
            case RIGHT:
                corners = new Vector3[]{cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z)};
                break;
            case FRONT:
                corners = new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z)};
                break;
            case BACK:
                corners = new Vector3[]{cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,0,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0)};
                break;
        }

        shiftByRotation(corners);
        vertexList.addAll(quadFromCorners(texturedVertices(corners)));


        vertices = new ModelVertex[vertexList.size()];
        vertices = vertexList.toArray(vertices);
    }

    private void shiftByRotation(Vector3[] in) {
        if (rotation == 1) {
            Vector3 temp = in[3];
            in[3] = in[2];
            in[2] = in[1];
            in[1] = in[0];
            in[0] = temp;
        } else if (rotation == -1) {
            Vector3 temp = in[0];
            in[0] = in[1];
            in[1] = in[2];
            in[2] = in[3];
            in[3] = temp;
        } else if (rotation == 2 || rotation == -2) {
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
                ms.get(2),ms.get(1),ms.get(0)
        );
    }

    public Texture getTexture() {
        return texture;
    }

    public enum Face{
        TOP("up"), BOTTOM("down"), LEFT("west"), RIGHT("east"), FRONT("north"), BACK("south");
        String rawValue;

        Face(String rawValue) {
            this.rawValue = rawValue;
        }
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
                (face == Face.BACK && culling.back);
    }
}
