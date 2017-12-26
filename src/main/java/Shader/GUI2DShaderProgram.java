package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class GUI2DShaderProgram extends ShaderProgram{
    public GUI2DShaderProgram() throws Exception {
        super("src/main/resources/shaders/vert2D.shader","src/main/resources/shaders/frag2D.shader");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
