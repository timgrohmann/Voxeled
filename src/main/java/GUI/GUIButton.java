package GUI;

import Models.GUITexturedVertex;

import java.util.ArrayList;
import java.util.Arrays;

class GUIButton {
    private final float midX;
    private final float midY;

    ArrayList<GUITexturedVertex> buttonVertices;

    private final GUIText guiText;

    public GUIButton(float midX, float midY, String text) {
        this.midX = midX;
        this.midY = midY;

        guiText = new GUIText(text,midX,midY,0.08f,true);

        generateButtonVertices();
    }

    private void generateButtonVertices() {
        buttonVertices = new ArrayList<>(Arrays.asList(GUIDrawer.texQuadAspectAndPixelCenter(0,0,1.6f,0,200,46,66)));
    }

    GUITexturedVertex[] getTextVertices() {
        return guiText.getVertices();
    }
}
