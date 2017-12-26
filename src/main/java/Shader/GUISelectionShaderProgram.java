package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class GUISelectionShaderProgram extends ShaderProgram{
    public GUISelectionShaderProgram() throws Exception {
        super("src/main/resources/shaders/vertex_selection.shader",
                "src/main/resources/shaders/fragment_selection.shader");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
