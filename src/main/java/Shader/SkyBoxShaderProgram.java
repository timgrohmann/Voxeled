package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class SkyBoxShaderProgram extends ShaderProgram {
    public SkyBoxShaderProgram() throws Exception {
        super("src/main/resources/shaders/vertex_skybox.shader","src/main/resources/shaders/fragment_skybox.shader");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
