package GUI;

import GL_Math.Vector2;

abstract public class UIBasicTexturedComponent extends UIComponent {
    Vector2 uvOrigin;
    Vector2 uvSize;

    public UIBasicTexturedComponent(Vector2 pos, float width, boolean center, Vector2 uvOrigin, Vector2 uvSize) {
        super(pos, new Vector2(width, width * uvSize.y / uvSize.x), center);
        this.uvOrigin = uvOrigin;
        this.uvSize = uvSize;
    }

    @Override
    void generateVertices() {
        vertices = UIComponent.texQuad(pos.x, pos.y, uvOrigin.x, uvOrigin.y,
                pos.x + size.x, pos.y - size.y, uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y);
    }
}
