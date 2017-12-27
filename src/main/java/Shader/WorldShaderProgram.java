package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class WorldShaderProgram extends ShaderProgram{
    public WorldShaderProgram() throws Exception {
        super("world");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
