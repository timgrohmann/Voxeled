package GUI;

import GL_Math.Vector2;


public class GUIHoverButton extends GUIButton {

    private static Vector2 HOVER_ORIGIN = new Vector2(0,86);

    private boolean hover = false;

    GUIHoverButton(Vector2 pos, float width, String text) {
        super(pos, width, text);
    }

    public void setHover(boolean hov) {
        if (hov == hover) return;
        hover = hov;

        if (hover) {
            this.uvOrigin = HOVER_ORIGIN;
        } else {
            this.uvOrigin = UV_ORIGIN;
        }
        this.generateVertices();
    }

}
