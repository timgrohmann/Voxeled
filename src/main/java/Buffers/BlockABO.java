package Buffers;

import GL_Math.Vector3;
import Main_Package.Log;
import Models.Vertex;
import Shader.ShaderProgram;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class BlockABO extends ArrayBufferObject {
    public BlockABO(ShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    private static int STRIDE = 13;

    public void load(Vertex[] vertices) {
        bind();
        float[] values = new float[vertices.length * STRIDE];
        vertexCount = vertices.length;

        Vector3[] normals = getNormals(vertices);

        for (int i = 0; i < vertices.length; i++) {
            values[i * STRIDE] = vertices[i].x;
            values[i * STRIDE + 1] = vertices[i].y;
            values[i * STRIDE + 2] = vertices[i].z;
            values[i * STRIDE + 3] = vertices[i].u;
            values[i * STRIDE + 4] = vertices[i].v;
            values[i * STRIDE + 5] = vertices[i].texture.layer;
            values[i * STRIDE + 6] = normals[i].x;
            values[i * STRIDE + 7] = normals[i].y;
            values[i * STRIDE + 8] = normals[i].z;

            Vector3 blendColor = vertices[i].texture.blendColor;
            values[i * STRIDE + 9] = blendColor.x;
            values[i * STRIDE + 10] = blendColor.y;
            values[i * STRIDE + 11] = blendColor.z;

            values[i * STRIDE + 12] = vertices[i].texture.layerCount;
        }
        //glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER,values,GL_DYNAMIC_DRAW);
        loadAttributePointers();
    }

    public void loadAttributePointers() {
        int in_Position_Location = shaderProgram.attributeLocation("in_Position");
        GL20.glEnableVertexAttribArray(in_Position_Location);
        GL20.glVertexAttribPointer(in_Position_Location,3,GL_FLOAT,false,STRIDE * Float.SIZE / 8,0);

        int in_UV_Location = shaderProgram.attributeLocation("in_UVW");
        GL20.glEnableVertexAttribArray(in_UV_Location);
        GL20.glVertexAttribPointer(in_UV_Location,3,GL_FLOAT,false,STRIDE * Float.SIZE / 8,3 * Float.SIZE / 8);

        int in_Normal_Location = shaderProgram.attributeLocation("in_Normal");
        GL20.glEnableVertexAttribArray(in_Normal_Location);
        GL20.glVertexAttribPointer(in_Normal_Location,3,GL_FLOAT,false,STRIDE * Float.SIZE / 8,6 * Float.SIZE / 8);

        int in_BlendColor_Location = shaderProgram.attributeLocation("in_BlendColor");
        GL20.glEnableVertexAttribArray(in_BlendColor_Location);
        GL20.glVertexAttribPointer(in_BlendColor_Location,3,GL_FLOAT,false,STRIDE * Float.SIZE / 8,9 * Float.SIZE / 8);

        int in_LayerCount_Location = shaderProgram.attributeLocation("in_LayerCount");
        GL20.glEnableVertexAttribArray(in_LayerCount_Location);
        GL20.glVertexAttribPointer(in_LayerCount_Location,1,GL_FLOAT,false,STRIDE * Float.SIZE / 8,12 * Float.SIZE / 8);
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
