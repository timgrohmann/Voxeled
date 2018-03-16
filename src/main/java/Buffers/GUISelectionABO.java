package Buffers;

import GL_Math.Vector3;
import Shader.GUISelectionShaderProgram;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class GUISelectionABO extends ArrayBufferObject{
    public GUISelectionABO(GUISelectionShaderProgram shaderProgram) {
        super(shaderProgram);
        loadAttributePointers();
    }

    public void load(Vector3[] vertices) {
        float[] vals = new float[vertices.length * 3];
        vertexCount = vertices.length;

        for (int i = 0; i < vertices.length; i++) {
            vals[i * 3] = vertices[i].x;
            vals[i * 3 + 1] = vertices[i].y;
            vals[i * 3 + 2] = vertices[i].z;
        }
        glBindBuffer(GL_ARRAY_BUFFER, this.id);
        glBufferData(GL_ARRAY_BUFFER, vals, GL_DYNAMIC_DRAW);
        loadAttributePointers();
    }

    private void loadAttributePointers() {
        int in_Position_Location = shaderProgram.attributeLocation("in_Position");
        GL20.glEnableVertexAttribArray(in_Position_Location);
        GL20.glVertexAttribPointer(in_Position_Location,3,GL_FLOAT,false,0,0);
    }

    public void render() {
        glDrawArrays(GL_LINES,0,vertexCount);
    }
}
