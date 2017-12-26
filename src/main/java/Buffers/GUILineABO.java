package Buffers;

import Models.GUIVertex;
import Shader.GUI2DShaderProgram;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;

public class GUILineABO extends ArrayBufferObject {
    public GUILineABO(GUI2DShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    public void load(GUIVertex[] vertices) {
        float[] vals = new float[vertices.length * 2];
        vertexCount = vertices.length;

        for (int i = 0; i < vertices.length; i++) {
            vals[i * 2] = vertices[i].x;
            vals[i * 2 + 1] = vertices[i].y;
        }

        glBufferData(GL_ARRAY_BUFFER,vals,GL_STATIC_DRAW);
        loadAttributePointers();
    }

    private void loadAttributePointers() {
        int in_Position_Location = shaderProgram.attributeLocation("in_Pos2D");
        GL20.glEnableVertexAttribArray(in_Position_Location);
        GL20.glVertexAttribPointer(in_Position_Location,2,GL_FLOAT,false,0,0);
    }

    public void render() {
        glDrawArrays(GL_LINES, 0, vertexCount);
    }

}
