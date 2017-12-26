package Main_Package;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

public class Log {
    public static void logGLError(){
        int error = glGetError();
        while (error != GL_NO_ERROR) {
            System.err.println(error);
            error = glGetError();
        }
    }

    public static void logGLError(String note){
        int error = glGetError();
        if (error == GL_NO_ERROR) System.out.format("No error at %s%n", note);
        while (error != GL_NO_ERROR) {
            System.err.println("[" + note + "]: " + String.valueOf(error));
            error = glGetError();
        }
    }
}
