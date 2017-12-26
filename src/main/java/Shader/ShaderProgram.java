package Shader;

import GL_Math.Matrix4;
import GL_Math.Vector3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Character.getName;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

abstract public class ShaderProgram {
    int program;

    private String log;

    ShaderProgram(String shaderDir) throws Exception {
        int vertex = compileShader("src/main/resources/shaders/" + shaderDir + "/vertex.shader", GL_VERTEX_SHADER);
        int fragment = compileShader("src/main/resources/shaders/" + shaderDir + "/fragment.shader", GL_FRAGMENT_SHADER);

        //create the program
        program = glCreateProgram();

        //attach the shaders
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);


        //link our program
        glLinkProgram(program);



        //grab our info log
        String infoLog = glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH));

        //if some log exists, append it
        if (infoLog.trim().length() != 0)
            log += infoLog;

        //if the link failed, throw some sort of exception
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
            throw new Exception(
                    "Failure in linking program. Error log:\n" + infoLog);

        //detach and delete the shaders which are no longer needed
        glDetachShader(program, vertex);
        glDetachShader(program, fragment);
        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    public int attributeLocation(String name) {
        return glGetAttribLocation(program, name);
    }

    public void use() {
        glUseProgram(this.program);
    }

    private int compileShader(String sourcePath, int type) throws Exception {
        //create a shader object
        int shader = glCreateShader(type);
        //pass the source string

        String source = readShaderFile(sourcePath);

        glShaderSource(shader, source);
        //compile the source
        glCompileShader(shader);

        //if info/warnings are found, append it to our shader log
        String infoLog = glGetShaderInfoLog(shader,
                glGetShaderi(shader, GL_INFO_LOG_LENGTH));

        if (infoLog.trim().length()!=0)
            log += getName(type) +": "+infoLog + "\n";

        //if the compiling was unsuccessful, throw an exception
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
            throw new Exception("Failure in compiling " + getName(type)
                    + ". Error log:\n" + infoLog);

        return shader;
    }

    private String readShaderFile(String filePath) {
        BufferedReader br = null;
        StringBuilder all = new StringBuilder();

        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                all.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return all.toString();
    }

    public int uniformLocation(String name) {
        return glGetUniformLocation(program,name);
    }

    public void setUniformVector(String posName, Vector3 vec){
        glUniform3fv(uniformLocation(posName),vec.components());
    }

    public void setUniformMatrix(String posName, Matrix4 vec){
        glUniformMatrix4fv(uniformLocation(posName), true, vec.components);
    }

    public void setUniformFloat(String posName, float value) {
        glUniform1f(uniformLocation(posName),value);
    }

    public void setUniformInt(String posName, int value) {
        glUniform1i(uniformLocation(posName),value);
    }
}
