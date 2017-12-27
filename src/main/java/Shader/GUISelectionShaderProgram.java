package Shader;

import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class GUISelectionShaderProgram extends ShaderProgram{
    public GUISelectionShaderProgram() throws Exception {
        super("block_selection");
        glBindFragDataLocation(program,0,"colorOut");
    }
}
