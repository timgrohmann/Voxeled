package GUI;

import GL_Math.Vector2;

class GUIButton extends UIBasicTexturedComponent{

    private final GUIText guiText;

    private static Vector2 UV_ORIGIN = new Vector2(0,66);
    private static Vector2 UV_SIZE = new Vector2(200,20);

    public GUIButton(Vector2 pos, float width, String text) {
        super(pos, width, true, UV_ORIGIN, UV_SIZE);

        guiText = new GUIText(text,this.centerPos(),0.08f,true);

        this.textureDescriptor = GUIDrawer.WIDGET_TEXTURE;
    }

    GUIText getText() {
        return guiText;
    }
}
