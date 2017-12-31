package Main_Package;

import GL_Math.Vector2;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GL_Window {
    int height;
    int width;

    public final long identifier;

    private ArrayList<GLFWKeyCallbackI> callbacks;

    public GL_Window(int height, int width) {
        this.height = height;
        this.width = width;

        //glfwWindowHint(GLFW_DECORATED, GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        identifier = glfwCreateWindow(width, height, "Totally unsuspicious copy of a famous game.", NULL, NULL);
        if ( identifier == NULL )
            throw new RuntimeException("Failed to create the GLFW window");


        //Hides and grabs the cursor, providing virtual and unlimited cursor movement.
        glfwSetInputMode(identifier, GLFW_CURSOR, GLFW_CURSOR_DISABLED);


        // Center Window
        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(identifier, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    identifier,
                    (videoMode.width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        callbacks = new ArrayList<>();
        registerMainKeyCallback();
    }

    private void registerMainKeyCallback() {
        glfwSetKeyCallback(identifier, (long window, int key, int scancode, int action, int mods) -> {
            for (GLFWKeyCallbackI callback: callbacks) {
                callback.invoke(window,key,scancode,action,mods);
            }
        });
    }

    void addKeyCallback(GLFWKeyCallbackI cb) {
        callbacks.add(cb);
    }



    void setResizeCallBack(GLFWWindowSizeCallbackI cb) {
        glfwSetWindowSizeCallback(identifier, cb);
    }

    void setMouseMoveCallBack(GLFWCursorPosCallbackI cb) {
        glfwSetCursorPosCallback(identifier,cb);
    }

    public void destroy() {
        glfwFreeCallbacks(identifier);
        glfwDestroyWindow(identifier);
    }

    public void show() {
        // Make the OpenGL context current
        glfwMakeContextCurrent(identifier);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(identifier);

    }

    void swapBuffers(){
        glfwSwapBuffers(this.identifier);
    }

    boolean shouldClose() {
        return glfwWindowShouldClose(identifier);
    }

    void setCursorPosToCenter() {
        glfwSetCursorPos(identifier,width/2,height/2);
    }

    Vector2 getMousePos() {
        double[] x = new double[1];
        double[] y = new double[1];

        glfwGetCursorPos(identifier,x,y);

        return new Vector2((float) x[0], (float) y[0]);
    }

    public float getAspectRatio() {
        return (float) width / (float) height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
