import Main_Package.GL_Window;
import Main_Package.Renderer;
import org.lwjgl.*;

import static org.lwjgl.glfw.GLFW.*;

class Main {
    private GL_Window window;

    private void run() {
        System.out.println("[Version] LWJGL " + Version.getVersion());

        init();

        Renderer renderer = new Renderer(window);
        System.out.println("[Main_Package.Renderer initialized]");
        renderer.start();
        System.out.println("[Main_Package.Renderer done]");

        window.destroy();

        glfwTerminate();
    }

    private void init() {
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Create the window
        window = new GL_Window(700,1200);

        window.show();
    }

    public static void main(String[] args) {
        new Main().run();
    }

}