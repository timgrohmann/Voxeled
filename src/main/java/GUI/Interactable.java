package GUI;

import GL_Math.Vector2;

interface Interactable {
    void click(Vector2 pos);
    void setHover(boolean hov);

    GUIRectangle getRect();

    boolean currentlyInteractable();
}