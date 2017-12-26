package Buffers;

import GL_Math.Vector3;
import Models.Vertex;
import Shader.ShaderProgram;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;

public class BlockABO extends ArrayBufferObject {
    public BlockABO(ShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    public void load(Vertex[] vertices) {
        bind();
        float[] values = new float[vertices.length * 8];
        vertexCount = vertices.length;

        Vector3[] normals = getNormals(vertices);

        for (int i = 0; i < vertices.length; i++) {
            values[i * 8] = vertices[i].x;
            values[i * 8 + 1] = vertices[i].y;
            values[i * 8 + 2] = vertices[i].z;
            values[i * 8 + 3] = vertices[i].u;
            values[i * 8 + 4] = vertices[i].v;
            values[i * 8 + 5] = normals[i].x;
            values[i * 8 + 6] = normals[i].y;
            values[i * 8 + 7] = normals[i].z;
        }
        //glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER,values,GL_DYNAMIC_DRAW);
        loadAttributePointers();
    }

    public void loadAttributePointers() {
        int in_Position_Location = shaderProgram.attributeLocation("in_Position");
        GL20.glEnableVertexAttribArray(in_Position_Location);
        GL20.glVertexAttribPointer(in_Position_Location,3,GL_FLOAT,false,8 * Float.SIZE / 8,0);

        int in_UV_Location = shaderProgram.attributeLocation("in_UV");
        GL20.glEnableVertexAttribArray(in_UV_Location);
        GL20.glVertexAttribPointer(in_UV_Location,2,GL_FLOAT,false,8 * Float.SIZE / 8,3 * Float.SIZE / 8);

        int in_Normal_Location = shaderProgram.attributeLocation("in_Normal");
        GL20.glEnableVertexAttribArray(in_Normal_Location);
        GL20.glVertexAttribPointer(in_Normal_Location,3,GL_FLOAT,false,8 * Float.SIZE / 8,5 * Float.SIZE / 8);
    }

    private Vector3[] getNormals(Vertex[] vertices) {
        Vector3[] normals = new Vector3[vertices.length];
        assert(vertices.length % 3 == 0);

        for (int i = 0; i < vertices.length / 3; i++) {
            Vertex v1 = vertices[i * 3];
            Vertex v2 = vertices[i * 3 + 1];
            Vertex v3 = vertices[i * 3 + 2];

            Vector3 v12 = new Vector3(v2.x-v1.x,v2.y-v1.y,v2.z-v1.z);
            Vector3 v13 = new Vector3(v3.x-v1.x,v3.y-v1.y,v3.z-v1.z);

            Vector3 normal = Vector3.crossProduct(v12,v13);
            normals[i * 3] = normal;
            normals[i * 3 + 1] = normal;
            normals[i * 3 + 2] = normal;
        }

        return normals;
    }

    public void render() {
        glDrawArrays(GL_TRIANGLES,0,vertexCount);
    }
}
