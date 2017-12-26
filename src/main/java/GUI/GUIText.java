package GUI;

import Models.GUITexturedVertex;

import java.util.ArrayList;
import java.util.Arrays;

class GUIText {

    private GUITexturedVertex[] textVertices;
    private float xPos;
    private float yPos;
    private final float size;
    private final ArrayList<GUITexturedVertex> vertices;

    public GUIText(String text, float x, float y, float size, boolean centered) {

        vertices = new ArrayList<>();

        xPos = x;
        yPos = y;
        this.size = size;

        if (centered) {
            xPos -= size / 2 * text.length();
            yPos += size / 2;
        }

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 65 && c <= 90) {
                //Uppercase letter
                int letterIndex = c - 65;
                addChar(i, letterIndex,0);
            }
            if (c >= 97 && c <= 122) {
                //Uppercase letter
                int letterIndex = c - 97;
                addChar(i,letterIndex,1);
            }
            if (c >= 48 && c <= 57) {
                //Numbers
                int letterIndex = c - 48 + 26;
                addChar(i,letterIndex,0);
            }
            if (c == '?') {
                int letterIndex = 27;
                addChar(i,letterIndex,1);
            }
            if (c == ':') {
                int letterIndex = 37;
                addChar(i,letterIndex,1);
            }
            if (c == ',') {
                addChar(i, 30,1);
            }
        }

        textVertices = new GUITexturedVertex[vertices.size()];
        textVertices = vertices.toArray(textVertices);
    }

    GUITexturedVertex[] getVertices() {
        return textVertices;
    }

    private void addChar(int i, int col, int row) {
        vertices.addAll(Arrays.asList(texQuadAspectAndPixel(xPos + i * size, yPos, size, 16 * col, 16 * (col + 1), row * 16,(row + 1) * 16)));
    }

    private static GUITexturedVertex[] texQuadAspectAndPixel(float minX, float minY, float xWidth, float pixUStart, float pixUEnd, float pixVStart, float pixVEnd) {
        int texW = 16 * 38;
        int texH = 32;
        float maxX = minX + xWidth;
        float maxY = minY - xWidth;
        float minU = pixUStart / texW;
        float minV = pixVStart / texH;
        float maxU = pixUEnd / texW;
        float maxV = pixVEnd / texH;

        return GUIDrawer.texQuad(minX,minY,minU,minV,maxX,maxY,maxU,maxV);
    }
}
