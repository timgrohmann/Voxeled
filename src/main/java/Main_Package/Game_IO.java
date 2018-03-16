package Main_Package;

import Entities.Block;
import Entities.HitBox;
import GL_Math.Vector2;
import GL_Math.Vector3;
import Player.Player;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class Game_IO {
    Block selectedBlock;

    private final Camera_IO cameraIo;

    private final Renderer renderer;


    private double scrollState = 0;

    private boolean mouseLeftJustPressed = false;
    private boolean mouseRightJustPressed = false;

    private boolean menuShown = false;


    Game_IO(Renderer renderer) {
        this.renderer = renderer;
        this.cameraIo = new Camera_IO(renderer);

        glfwSetMouseButtonCallback(renderer.getWindow().identifier, (long window, int button, int action, int mods) -> {
            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                mouseLeftJustPressed = true;
            }
            if (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS) {
                mouseRightJustPressed = true;
            }
        });

        glfwSetScrollCallback(renderer.getWindow().identifier, (long window, double xOff, double yOff) -> {
            scrollState += yOff;
            double scrollDiff = 0;
            if (scrollState > scrollDiff) {
                renderer.player.selectedSlot--;
                scrollState = 0;
            }
            if (scrollState < -scrollDiff){
                renderer.player.selectedSlot++;
                scrollState = 0;
            }

            if (renderer.player.selectedSlot > 8) renderer.player.selectedSlot = 0;
            if (renderer.player.selectedSlot < 0) renderer.player.selectedSlot = 8;
        });

        renderer.getWindow().addKeyCallback(keyInput);
    }

    void update() {
        if (!menuShown) {
            handleBlockInteraction(renderer.player);
        }

        Vector2 mousePos = renderer.getWindow().getMousePos();
        renderer.guiDrawer.mouseControl.process(mousePos, mouseLeftJustPressed);

        mouseLeftJustPressed = false;
        mouseRightJustPressed = false;
    }

    private final GLFWKeyCallbackI keyInput = (long window, int key, int scancode, int action, int mods) -> {
        if (key == GLFW_KEY_M && action == GLFW_PRESS) toggleMenu();
        if (key == GLFW_KEY_E && action == GLFW_PRESS) toggleInventory();
    };

    private void handleBlockInteraction(Player p) {
        Block selected = null;

        int xDif = 0;
        int yDif = 0;
        int zDif = 0;

        Vector3 pos = null;
        for (float s = 0; s < 6; s += 0.1){
            for (float dist = 0; dist < 6; dist += 0.05) {
                pos = renderer.camera.rayAtStep(dist);
                selected = renderer.world.getBlockForCoordinates(pos);
                if (selected == null) continue;

                HitBox h = selected.getHitbox();

                if (h != null && h.doesContainPoint(pos)) {
                    float back = pos.x - h.min().x;
                    float front = h.max().x - pos.x;

                    float bottom = pos.y - h.min().y;
                    float top = h.max().y - pos.y;

                    float right = pos.z - h.min().z;
                    float left = h.max().z - pos.z;

                    if (front < back && front < top && front < bottom && front < left && front < right) {
                        xDif = +1;
                    } else if (back < front && back < top && back < bottom && back < left && back < right) {
                        xDif = -1;
                    } else if (top < back && top < front && top < bottom && top < left && top < right) {
                        yDif = +1;
                    } else if (bottom < back && bottom < top && bottom < front && bottom < left && bottom < right) {
                        yDif = -1;
                    } else if (left < back && left < top && left < bottom && left < front && left < right) {
                        zDif = +1;
                    } else if (right < back && right < top && right < bottom && right < left && right < front) {
                        zDif = -1;
                    }

                    break;
                }
            }
        }
        renderer.guiDrawer.highlightBlock(selected);

        if (selected != null && mouseLeftJustPressed) {
            selected.primaryInteraction();
        }

        if (selected != null && mouseRightJustPressed) {
            boolean shouldPlace = selected.secondaryInteraction(p);

            if (shouldPlace) {
                Block t = renderer.player.getSelectedBlock();
                if (t != null){
                    Block newBlock = renderer.world.setBlockForCoordinates(t.type, selected.getXPos() + xDif, selected.getYPos() + yDif, selected.getZPos() + zDif);
                    newBlock.updateOptionsWithPlacePos(pos);
                }
            }
        }
    }

    private void toggleMenu() {
        if (!renderer.guiDrawer.menuShown) {
            //show
            cameraIo.disableMouseTracking();
            renderer.guiDrawer.menuShown = true;
            menuShown = true;
        } else {
            //hide
            cameraIo.enableMouseTracking();
            renderer.guiDrawer.menuShown = false;
            menuShown = false;
        }

    }

    private void toggleInventory() {
        if (renderer.guiDrawer.inventoryShown) {
            cameraIo.enableMouseTracking();
            renderer.guiDrawer.inventoryShown = false;
            menuShown = false;
        } else {
            cameraIo.disableMouseTracking();
            renderer.guiDrawer.inventoryShown = true;
            menuShown = true;
        }
    }

}
