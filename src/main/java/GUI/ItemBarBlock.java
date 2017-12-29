package GUI;

import GL_Math.Vector2;

public class ItemBarBlock extends UIBasicTexturedComponent {

    private static Vector2 UV_ORIGIN = new Vector2(0,0);
    private static Vector2 UV_SIZE = new Vector2(256,256);

    private final Vector2 initialPos;

    public ItemBarBlock(Vector2 pos, float width, boolean center) {
        super(pos, width, center, UV_ORIGIN, UV_SIZE);
        initialPos = pos.copy();
    }

    public void setScrollState(int slot /*0-8*/) {
        pos.x = initialPos.x + slot * size.x;
        generateVertices();
    }


}
