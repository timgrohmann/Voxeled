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

    CuboidModel cuboidModel;


    public CuboidFace(Vector2 uvOrigin, Vector2 uvSize, Face face, Texture texture, CuboidModel model) {
        this.uvOrigin = uvOrigin.multiplied(1f/16);
        this.uvSize = uvSize.multiplied(1f/16);
        this.face = face;
        this.texture = texture;
        this.cuboidModel = model;

        Vector3[] corners;

        List<ModelVertex> vertexList = new ArrayList<>();

        switch (face) {
            case TOP:
                corners = new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z)};
                vertexList.addAll(quadFromCorners(texturedVertices(corners)));
                break;
            case BOTTOM:
                corners = new Vector3[]{cuboidModel.origin.added(0,0,0),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0)};
                vertexList.addAll(quadFromCorners(texturedVertices(corners)));
                break;
            case LEFT:
                corners = new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,0),};
                vertexList.addAll(quadFromCorners(texturedVertices(corners)));
                break;
            case RIGHT:
                corners = new Vector3[]{cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z)};
                vertexList.addAll(quadFromCorners(texturedVertices(corners)));
                break;
            case FRONT:
                corners = new Vector3[]{cuboidModel.origin.added(0,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,cuboidModel.size.z),
                        cuboidModel.origin.added(cuboidModel.size.x,0,cuboidModel.size.z),
                        cuboidModel.origin.added(0,0,cuboidModel.size.z)};
                vertexList.addAll(quadFromCorners(texturedVertices(corners)));
                break;
            case BACK:
                corners = new Vector3[]{cuboidModel.origin.added(cuboidModel.size.x,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,cuboidModel.size.y,0),
                        cuboidModel.origin.added(0,0,0),
                        cuboidModel.origin.added(cuboidModel.size.x,0,0)};
                vertexList.addAll(quadFromCorners(texturedVertices(corners)));
                break;
        }

        vertices = new ModelVertex[vertexList.size()];
        vertices = vertexList.toArray(vertices);
    }

    public CuboidFace(Face face, Texture texture, CuboidModel model) {
        this(Vector2.zero, new Vector2(16,16), face, texture, model);
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
