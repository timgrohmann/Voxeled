package GUI;

import GL_Math.Vector2;
import Models.GUITexturedVertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GUIText extends UIComponent {

    private String text;

    public GUIText(String text, Vector2 pos, float letterHeight, boolean centered) {
        super(pos,new Vector2(letterHeight * text.length(), letterHeight), centered);

        this.text = text;
        this.textureDescriptor = GUIDrawer.TEXT_TEXTURE;
    }

    @Override
    void generateVertices() {
        ArrayList<GUITexturedVertex> vertexArrayList = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 65 && c <= 90) {
                //Uppercase letter
                int letterIndex = c - 65;
                addChar(i, letterIndex,0, vertexArrayList);
            }
            if (c >= 97 && c <= 122) {
                //Uppercase letter
                int letterIndex = c - 97;
                addChar(i,letterIndex,1, vertexArrayList);
            }
            if (c >= 48 && c <= 57) {
                //Numbers
                int letterIndex = c - 48 + 26;
                addChar(i,letterIndex,0, vertexArrayList);
            }
            if (c == '?') {
                int letterIndex = 27;
                addChar(i,letterIndex,1, vertexArrayList);
            }
            if (c == ':') {
                int letterIndex = 37;
                addChar(i,letterIndex,1, vertexArrayList);
            }
            if (c == ',') {
                addChar(i, 30,1, vertexArrayList);
            }
        }

        vertices = new GUITexturedVertex[vertexArrayList.size()];
        vertices = vertexArrayList.toArray(vertices);
    }

    private void addChar(int i, int col, int row, List<GUITexturedVertex> list) {
        //noinspection SuspiciousNameCombination
        list.addAll(Arrays.asList(texQuadAspectAndPixel(pos.x + i * this.size.y, pos.y, this.size.y, 16 * col, 16 * (col + 1), row * 16,(row + 1) * 16)));
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

        return UIComponent.texQuadNormalized(minX,minY,minU,minV,maxX,maxY,maxU,maxV);
    }
}
