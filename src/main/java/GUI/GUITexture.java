package GUI;

import Textures.TextureLoader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class GUITexture {
    private int textureId = -1;
    private int textureComponent = -1;

    public GUITexture(int textureComponent) {
        this.textureId = glGenTextures();
        this.textureComponent = textureComponent;
    }

    public GUITexture load(String filePath, boolean interpolate) {
        TextureLoader textureLoader = TextureLoader.load(filePath);

        bind();

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureLoader.tWidth, textureLoader.tHeight, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureLoader.buf);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, interpolate ? GL_LINEAR : GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, interpolate ? GL_LINEAR : GL_NEAREST);

        GL30.glGenerateMipmap(GL_TEXTURE_2D);

        return this;
    }

    void bind() {
        glActiveTexture(GL_TEXTURE0 + textureComponent);
        glBindTexture(GL_TEXTURE_2D, this.textureId);
    }
}
