package GUI;

import GL_Math.Vector2;

class ItemBar extends UIBasicTexturedComponent {

    private static Vector2 UV_ORIGIN = new Vector2(0,1);
    private static Vector2 UV_SIZE = new Vector2(182,20);

    public ItemBar(Vector2 pos, float width, boolean center) {
        super(pos, width, center, UV_ORIGIN, UV_SIZE);
        this.textureDescriptor = GUIDrawer.WIDGET_TEXTURE;
    }

    public Vector2 positionForSlot(int i) {
        return this.pos.added(new Vector2(i * this.size.x / 9, 0));
    }
}
