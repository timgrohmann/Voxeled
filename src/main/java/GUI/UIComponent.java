package GUI;

import GL_Math.Vector2;
import Models.GUITexturedVertex;

import java.util.Arrays;
import java.util.List;

abstract public class UIComponent {
    protected final Vector2 pos;
    protected final Vector2 size;
    protected GUITexturedVertex[] vertices;

    private boolean visible = true;

    private static float TEXTURE_SIZE = 256;

    public GUITextureDescriptor textureDescriptor;


    public UIComponent(Vector2 pos, Vector2 size, boolean centered) {
        this.pos = pos;
        this.size = size;

        if (centered) {
            this.pos.x -= size.x / 2;
            this.pos.y += size.y / 2;
        }
    }

    abstract void generateVertices();

    protected Vector2 centerPos() {
        return new Vector2(pos.x + size.x / 2, pos.y - size.y / 2);
    }

    GUITexturedVertex[] getVertices() {
        if (vertices == null || vertices.length == 0) generateVertices();
        return vertices;
    }

    List<GUITexturedVertex> getVerticesList() {
        if (vertices == null || vertices.length == 0) generateVertices();
        return Arrays.asList(vertices);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    static GUITexturedVertex[] texQuad(float minX, float minY, float minU, float minV,
                                       float maxX, float maxY, float maxU, float maxV) {
        minU /= TEXTURE_SIZE; minV /= TEXTURE_SIZE; maxU /= TEXTURE_SIZE; maxV /= TEXTURE_SIZE;
        return texQuadNormalized(minX,minY,minU,minV,maxX,maxY,maxU,maxV);

    }

    static GUITexturedVertex[] texQuadNormalized(float minX, float minY, float minU, float minV,
                                       float maxX, float maxY, float maxU, float maxV) {
        return new GUITexturedVertex[]{
                new GUITexturedVertex(minX,minY,minU,minV), new GUITexturedVertex(maxX,minY,maxU,minV), new GUITexturedVertex(minX,maxY,minU,maxV),
                new GUITexturedVertex(maxX,minY,maxU,minV), new GUITexturedVertex(maxX,maxY,maxU,maxV), new GUITexturedVertex(minX,maxY,minU,maxV),
        };

    }
}
