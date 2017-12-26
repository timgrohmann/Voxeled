package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class WorldShaderProgram extends ShaderProgram{
    public WorldShaderProgram() throws Exception {
        super("src/main/resources/shaders/vertex.shader","src/main/resources/shaders/fragment.shader");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
