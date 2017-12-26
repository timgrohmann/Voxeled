package GUI;

import Textures.TextureLoader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

class GUITexture {
    private int guiTextureId = -1;
    private int textTextureId = -1;

    public GUITexture() {
        this.guiTextureId = glGenTextures();
        this.textTextureId = glGenTextures();
    }

    void load() {
        TextureLoader textureLoader = TextureLoader.load("src/main/resources/gui.png");

        activateMainTextures();

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureLoader.tWidth, textureLoader.tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureLoader.buf);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        GL30.glGenerateMipmap(GL_TEXTURE_2D);


        textureLoader = TextureLoader.load("src/main/resources/text.png");
        activateTextTextures();
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureLoader.tWidth, textureLoader.tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureLoader.buf);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        GL30.glGenerateMipmap(GL_TEXTURE_2D);
    }

    void activateMainTextures() {
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, this.guiTextureId);
    }

    void activateTextTextures() {
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, this.textTextureId);
    }
}
