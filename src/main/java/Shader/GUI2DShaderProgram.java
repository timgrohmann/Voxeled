package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class GUI2DShaderProgram extends ShaderProgram{
    public GUI2DShaderProgram() throws Exception {
        super("untextured_2D");
        glBindFragDataLocation(program,0,"colorOut");
    }
}

