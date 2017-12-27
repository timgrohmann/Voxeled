package Buffers;

import Models.GUITexturedVertex;
import Shader.GUITextured2DShaderProgram;
import org.lwjgl.opengl.GL20;


import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;

public class Triangle2DABO extends ArrayBufferObject {
    public Triangle2DABO(GUITextured2DShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    public void load(GUITexturedVertex[] vertices) {
        bind();
        float[] vals = new float[vertices.length * 4];
        vertexCount = vertices.length;

        for (int i = 0; i < vertices.length; i++) {
            GUITexturedVertex c = vertices[i];
            vals[i * 4] = c.x;
            vals[i * 4 + 1] = c.y;
            vals[i * 4 + 2] = c.u;
            vals[i * 4 + 3] = c.v;
        }

        glBufferData(GL_ARRAY_BUFFER,vals,GL_STATIC_DRAW);
        loadAttributePointers();
    }

    public void load(List<GUITexturedVertex> vertices) {
        GUITexturedVertex[] arr = new GUITexturedVertex[vertices.size()];
        arr = vertices.toArray(arr);
        load(arr);
    }

    private void loadAttributePointers() {
        int in_Position_Location = shaderProgram.attributeLocation("in_Pos2D");
        GL20.glEnableVertexAttribArray(in_Position_Location);
        GL20.glVertexAttribPointer(in_Position_Location,2,GL_FLOAT,false,4 * Float.SIZE / 8,0);

        int in_UV_Location = shaderProgram.attributeLocation("in_UV");
        GL20.glEnableVertexAttribArray(in_UV_Location);
        GL20.glVertexAttribPointer(in_UV_Location,2,GL_FLOAT,false,4 * Float.SIZE / 8,2 * Float.SIZE / 8);
    }

    public void render() {
        glDrawArrays(GL_TRIANGLES,0,vertexCount);
    }
}
