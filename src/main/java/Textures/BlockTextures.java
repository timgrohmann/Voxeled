package Textures;

import Entities.Block;
import Main_Package.Log;
import Registry.BlockRegistry;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL42.glTexStorage3D;

public class BlockTextures {
    private final String texPath;

    private int textureId = -1;

    public Texture[] textures;

    private int TEXTURE_SIZE = 16;


    public BlockTextures(BlockRegistry registry) {
        this.texPath = "src/main/resources/terrain.png";

        Block[] allBlocks = registry.allSingletons();

        ArrayList<Texture> textures = new ArrayList<>();

        for (Block block: allBlocks) {
            block.registerTextures();
            if (block.getTopTexture() != null && !textures.contains(block.getTopTexture())) textures.add(block.getTopTexture());
            if (block.getSideTexture() != null && !textures.contains(block.getSideTexture())) textures.add(block.getSideTexture());
            if (block.getBottomTexture() != null && !textures.contains(block.getBottomTexture())) textures.add(block.getBottomTexture());
        }

        this.textures = new Texture[textures.size()];
        this.textures = textures.toArray(this.textures);
    }

    public void load() {
        if (textureId == -1) textureId = GL11.glGenTextures();

        TextureLoader texture = TextureLoader.load(texPath);



        GL13.glActiveTexture(GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D_ARRAY, textureId);


        glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, TEXTURE_SIZE, TEXTURE_SIZE, textures.length);



        for (int i = 0; i < textures.length; i++) {
            TextureLoader loader = TextureLoader.load("textures/PureBDcraft  16x MC112/assets/minecraft/textures/blocks/" + textures[i].name + ".png");
            glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, TEXTURE_SIZE, TEXTURE_SIZE, 1, GL_RGBA, GL_UNSIGNED_BYTE, loader.buf);

            textures[i].layer = i;
        }

        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D_ARRAY,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);

        //GL30.glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void activateTextures() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D_ARRAY, this.textureId);
    }
}
