package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class SkyBoxShaderProgram extends ShaderProgram {
    public SkyBoxShaderProgram() throws Exception {
        super("skybox");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
