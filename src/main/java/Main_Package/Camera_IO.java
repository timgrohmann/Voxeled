package Main_Package;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.GLFW.*;

class Camera_IO {
    private Renderer renderer;

    private final float moveSpeed = 0.2f;
    private final float rotationSpeed = 0.01f;

    private double lastMouseX;
    private double lastMouseY;

    private boolean mouseCaptured = true;

    private boolean lastPosIsSet = false;
    Camera_IO(Renderer r){
        this.renderer = r;
        renderer.getWindow().addKeyCallback(cb);

        renderer.getWindow().setMouseMoveCallBack(cursorPosCallback);
    }

    private final GLFWKeyCallbackI cb = (w, key, scanCode, action, mods) -> {
        if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
            glfwSetWindowShouldClose(w, true); // We will detect this in the rendering loop
    };

    void disableMouseTracking() {
        glfwSetInputMode(renderer.getWindow().identifier, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        mouseCaptured = false;
    }
    void enableMouseTracking() {
        glfwSetInputMode(renderer.getWindow().identifier, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        mouseCaptured = true;
    }

    private final GLFWCursorPosCallbackI cursorPosCallback = (w, xpos, ypos) -> {
        if (!mouseCaptured) return;

        if (!lastPosIsSet) {
            lastMouseX = xpos;
            lastMouseY = ypos;
            lastPosIsSet = true;
        }

        double dx = xpos - lastMouseX;
        double dy = ypos - lastMouseY;

        this.renderer.camera.pitch((float) -dy / 500);
        this.renderer.camera.yaw((float) dx / 500);

        lastMouseX = xpos;
        lastMouseY = ypos;

        //this.renderer.getWindow().setCursorPosToCenter();
    };

}
