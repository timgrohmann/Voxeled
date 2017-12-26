package Textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class BlockTextures {
    private final String texPath;

    private int textureId = -1;

    public BlockTextures() {
        this.texPath = "src/main/resources/terrain.png";

    }

    public void load() {
        if (textureId == -1) textureId = GL11.glGenTextures();

        TextureLoader texture = TextureLoader.load(texPath);

        GL13.glActiveTexture(GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D, textureId);

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);


        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, texture.tWidth, texture.tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texture.buf);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        GL30.glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void activateTextures() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }
}
