package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class WaterShaderProgram extends ShaderProgram {
    public WaterShaderProgram() throws Exception {
        super("src/main/resources/shaders/vertex.shader","src/main/resources/shaders/fragment_water.shader");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
