package Textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class SkyBoxTexture {
    private final int id;

    private final static String[] filnames = {
            "xpos.png",
            "xneg.png",
            "ypos.png",
            "yneg.png",
            "zpos.png",
            "zneg.png",
    };

    public SkyBoxTexture() {
        id = glGenTextures();

        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_CUBE_MAP, this.id);

        for (int i = 0; i < 6; i++) {
            TextureLoader loader = TextureLoader.load("/skybox/" + filnames[i]);
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, loader.tWidth, loader.tHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, loader.buf);
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public void activateTextures() {
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_CUBE_MAP, this.id);
    }
}
