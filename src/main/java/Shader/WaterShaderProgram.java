package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class WaterShaderProgram extends ShaderProgram {
    public WaterShaderProgram() throws Exception {
        super("water");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
