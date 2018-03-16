package GUI;

import Main_Package.GL_Window;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

class ItemBarFBO {
    private int id;

    private int textureID;

    private GL_Window window;

    private static int WIDTH = 256;
    private static int HEIGHT = 256;
    private static float[] clearColor = new float[]{0,0,0,0};

    public ItemBarFBO(GL_Window window) {
        this.id = glGenFramebuffers();
        this.window = window;

        glBindFramebuffer(GL_FRAMEBUFFER,id);



        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, WIDTH, HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, textureID, 0);


        int depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH_COMPONENT, WIDTH, HEIGHT);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);

        unbind();

    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER,id);
        glViewport(0,0,WIDTH,HEIGHT);
        glClearBufferfv(GL_COLOR, 0, clearColor);
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        glViewport(0,0,window.getWidth(),window.getHeight());
        //glEnable(GL_BLEND);
    }

    public void activateTexture() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }
}