package GUI;

import GL_Math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GUIMouseControl {
    private GUIDrawer guiDrawer;

    private List<Interactable> components = new ArrayList<>();

    public GUIMouseControl(GUIDrawer guiDrawer) {
        this.guiDrawer = guiDrawer;
    }

    void registerComponent (Interactable component) {
        components.add(component);
    }

    public void process(Vector2 mousePos, boolean clicked) {
        if (clicked) click(mousePos);
        hover(mousePos);
    }

    private void click(Vector2 mousePos) {
        float width = guiDrawer.renderer.getWindow().getWidth();
        float height = guiDrawer.renderer.getWindow().getHeight();

        Vector2 screenPos = new Vector2(mousePos.x / width * 2 - 1, -mousePos.y / height * 2 + 1);
        screenPos.x *= guiDrawer.renderer.getWindow().getAspectRatio();

        components.forEach(k -> {
            if (k.currentlyInteractable() && GUIMouseControl.isInside(screenPos, k.getRect())) k.click(screenPos);
        });
    }

    private void hover(Vector2 mousePos) {
        float width = guiDrawer.renderer.getWindow().getWidth();
        float height = guiDrawer.renderer.getWindow().getHeight();

        Vector2 screenPos = new Vector2(mousePos.x / width * 2 - 1, -mousePos.y / height * 2 + 1);
        screenPos.x *= guiDrawer.renderer.getWindow().getAspectRatio();

        components.forEach(k -> k.setHover(k.currentlyInteractable() && GUIMouseControl.isInside(screenPos, k.getRect())));
    }

    private static boolean isInside(Vector2 pos, GUIRectangle rec) {
        return GUIMouseControl.isInside(pos, rec.origin, rec.size);
    }

    public static boolean isInside(Vector2 pos, Vector2 origin, Vector2 size) {
        return pos.x > origin.x && pos.x < origin.x + size.x &&
                pos.y < origin.y && pos.y > origin.y - size.y;
    }
}
