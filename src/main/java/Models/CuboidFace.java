package Models;

import GL_Math.Vector2;
import GL_Math.Vector3;
import Textures.Texture;

import java.util.Arrays;
import java.util.List;

public class CuboidFace {
    Vector2 uvOrigin;
    Vector2 uvSize;
    Face face;
    Texture texture;
    boolean culling = true;

    public CuboidFace(Vector2 uvOrigin, Vector2 uvSize, Face face, Texture texture) {
        this.uvOrigin = uvOrigin.multiplied(1f/16);
        this.uvSize = uvSize.multiplied(1f/16);
        this.face = face;
        this.texture = texture;
    }

    public CuboidFace(Face face, Texture texture) {
        this.uvOrigin = Vector2.zero;
        this.uvSize = new Vector2(1,1);
        this.face = face;
        this.texture = texture;
    }

    List<ModelVertex> texturedVertices(Vector3[] untexturedPostions) {
        return Arrays.asList(
                new ModelVertex(untexturedPostions[0], uvOrigin.x, uvOrigin.y, texture),
                new ModelVertex(untexturedPostions[1], uvOrigin.x + uvSize.x, uvOrigin.y, texture),
                new ModelVertex(untexturedPostions[2], uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y, texture),
                new ModelVertex(untexturedPostions[3], uvOrigin.x, uvOrigin.y + uvSize.y, texture)
        );
    }

    public enum Face{
        TOP("top"), BOTTOM("bottom"), LEFT("west"), RIGHT("east"), FRONT("north"), BACK("south");
        String rawValue;

        Face(String rawValue) {
            this.rawValue = rawValue;
        }
    }
}
