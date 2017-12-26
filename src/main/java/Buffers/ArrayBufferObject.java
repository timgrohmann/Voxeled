package Buffers;

import Shader.ShaderProgram;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

abstract public class ArrayBufferObject {
    final int id;

    private final int vaoID;

    int vertexCount = 0;

    final ShaderProgram shaderProgram;

    ArrayBufferObject(ShaderProgram shaderProgram) {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        this.shaderProgram = shaderProgram;
    }

    public void bind() {
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, id);
    }

    abstract public void render();
}
