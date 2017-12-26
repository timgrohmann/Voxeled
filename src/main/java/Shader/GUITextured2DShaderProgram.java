package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class GUITextured2DShaderProgram extends ShaderProgram{
    public GUITextured2DShaderProgram() throws Exception {
        super("textured_2D");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
