package Main_Package;

import Entities.Block;
import GL_Math.Vector3;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class Game_IO {
    Block selectedBlock;

    private final Camera_IO cameraIo;

    private final Renderer renderer;


    public int selectedSlot = 0;
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
                selectedSlot--;
                scrollState = 0;
            }
            if (scrollState < -scrollDiff){
                selectedSlot++;
                scrollState = 0;
            }

            if (selectedSlot > 8) selectedSlot = 0;
            if (selectedSlot < 0) selectedSlot = 8;
        });

        renderer.getWindow().addKeyCallback(keyInput);
    }

    void update() {
        if (!menuShown) handleBlockInteraction();

        mouseLeftJustPressed = false;
        mouseRightJustPressed = false;
    }

    private final GLFWKeyCallbackI keyInput = (long window, int key, int scancode, int action, int mods) -> {
        if (key == GLFW_KEY_M && action == GLFW_PRESS) toggleMenu();
    };

    private void handleBlockInteraction() {
        Block selected = null;

        int xDif = 0;
        int yDif = 0;
        int zDif = 0;

        for (float s = 0; s < 6; s += 0.1){
            for (float dist = 0; dist < 6; dist += 0.05) {
                Vector3 pos = renderer.camera.rayAtStep(dist);
                selected = renderer.world.getBlockForCoordinates(pos);
                if (selected != null) {
                    float back = pos.x - selected.getXPos();
                    float front = 1 - back;

                    float bottom = pos.y - selected.getYPos();
                    float top = 1 - bottom;

                    float right = pos.z - selected.getZPos();
                    float left = 1 - right;

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
            Block.Type t = renderer.player.inventory()[selectedSlot].type;
            if (t != null){
                renderer.world.setBlockForCoordinates(t, selected.getXPos() + xDif, selected.getYPos() + yDif, selected.getZPos() + zDif);
            }
        }
    }

    private void toggleMenu() {
        if (!menuShown) {
            //show
            cameraIo.disableMouseTracking();
            renderer.guiDrawer.menuShown = true;
        } else {
            //hide
            cameraIo.enableMouseTracking();
            renderer.guiDrawer.menuShown = false;
        }

        menuShown = !menuShown;
    }

}
