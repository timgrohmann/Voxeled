package Buffers;

import GL_Math.Vector3;
import Shader.SkyBoxShaderProgram;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;

public class SkyBoxABO extends ArrayBufferObject {
    public SkyBoxABO(SkyBoxShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    public void load(Vector3[] vertices) {
        vertexCount = vertices.length;

        float[] vals = new float[vertices.length * 3];

        for (int i = 0; i < vertices.length; i++) {
            vals[i * 3] = vertices[i].x;
            vals[i * 3 + 1] = vertices[i].y;
            vals[i * 3 + 2] = vertices[i].z;
        }

        glBufferData(GL_ARRAY_BUFFER,vals,GL_STATIC_DRAW);
        loadAttributePointers();
    }

    private void loadAttributePointers() {
        int in_Position_Location = shaderProgram.attributeLocation("in_Position");
        GL20.glEnableVertexAttribArray(in_Position_Location);
        GL20.glVertexAttribPointer(in_Position_Location,3,GL_FLOAT,false,0,0);
    }

    public void render() {
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
    }
}
