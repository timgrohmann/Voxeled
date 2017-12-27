package Main_Package;

import Buffers.SkyBoxABO;
import GL_Math.Matrix4;
import GL_Math.Vector3;
import Shader.SkyBoxShaderProgram;
import Textures.SkyBoxTexture;

import java.util.ArrayList;
import java.util.Arrays;

class SkyBox {
    private final SkyBoxTexture skyBoxTexture;
    private SkyBoxShaderProgram skyBoxShaderProgram;
    private final SkyBoxABO skyBoxABO;

    private final float size = 500;

    float angle = 0;

    public SkyBox() {
        skyBoxTexture = new SkyBoxTexture();
        try {
            skyBoxShaderProgram = new SkyBoxShaderProgram();
        } catch (Exception e) {
            e.printStackTrace();
        }
        skyBoxABO = new SkyBoxABO(skyBoxShaderProgram);

        ArrayList<Vector3> vertices = new ArrayList<>();
        Vector3[] corners = {
                new Vector3(-size,size,-size),new Vector3(-size,size,size),new Vector3(size,size,size),new Vector3(size,size,-size),
                new Vector3(-size,-size,-size),new Vector3(size,-size,-size),new Vector3(size,-size,size),new Vector3(-size,-size,size),
        };

        vertices.addAll(quad(corners[0], corners[1], corners[2], corners[3]));
        vertices.addAll(quad(corners[0], corners[3], corners[5], corners[4]));
        vertices.addAll(quad(corners[3], corners[2], corners[6], corners[5]));
        vertices.addAll(quad(corners[2], corners[1], corners[7], corners[6]));
        vertices.addAll(quad(corners[0], corners[4], corners[7], corners[1]));
        vertices.addAll(quad(corners[4], corners[5], corners[6], corners[7]));

        Vector3[] vector3s = new Vector3[vertices.size()];
        vector3s = vertices.toArray(vector3s);

        skyBoxABO.load(vector3s);

    }

    public void render(Matrix4 matrix4, Vector3 playerPos) {
        skyBoxShaderProgram.use();
        //angle += 0.003;

        skyBoxShaderProgram.setUniformMatrix("mat", matrix4);
        skyBoxShaderProgram.setUniformFloat("timeAngle",angle);
        skyBoxShaderProgram.setUniformInt("texture_cube",2);
        skyBoxShaderProgram.setUniformVector("player_Pos", playerPos);
        skyBoxTexture.activateTextures();
        skyBoxABO.bind();
        skyBoxABO.render();
    }

    private ArrayList<Vector3> quad(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4) {
        return new ArrayList<>(Arrays.asList(
                v1,v2,v3,
                v3,v4,v1
        ));
    }
}
