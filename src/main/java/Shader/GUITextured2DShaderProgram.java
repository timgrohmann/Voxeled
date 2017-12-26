package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class GUITextured2DShaderProgram extends ShaderProgram{
    public GUITextured2DShaderProgram() throws Exception {
        super("src/main/resources/shaders/vert2D_tex.shader","src/main/resources/shaders/frag2D_tex.shader");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
