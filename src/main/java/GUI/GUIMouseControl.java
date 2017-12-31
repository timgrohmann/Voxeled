package GUI;

import GL_Math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GUIMouseControl {
    GUIDrawer guiDrawer;

    private List<Interactable> components = new ArrayList<>();

    public GUIMouseControl(GUIDrawer guiDrawer) {
        this.guiDrawer = guiDrawer;
    }

    void registerComponent (Interactable component) {
        components.add(component);
    }

    public void click (Vector2 mousePos) {
        float width = guiDrawer.renderer.getWindow().getWidth();
        float height = guiDrawer.renderer.getWindow().getHeight();

        Vector2 screenPos = new Vector2(mousePos.x / width * 2 - 1, -mousePos.y / height * 2 + 1);
        screenPos.x *= guiDrawer.renderer.getWindow().getAspectRatio();

        components.forEach(k -> k.click(screenPos));
    }

    public static boolean isInside(Vector2 pos, Vector2 origin, Vector2 size) {
        return pos.x > origin.x && pos.x < origin.x + size.x &&
                pos.y < origin.y && pos.y > origin.y - size.y;
    }
}
