package Textures;

import Entities.Block;
import Models.CuboidFace;
import Models.CuboidModel;
import Models.EntityModel;
import Registry.BlockRegistry;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL42.glTexStorage3D;

public class BlockTextures {
    private int textureId = -1;

    private Texture[] textures;

    private int TEXTURE_SIZE = 16;


    public BlockTextures(BlockRegistry registry) {

        Block[] allBlocks = registry.allSingletons();

        ArrayList<Texture> textures = new ArrayList<>();

        for (Block block: allBlocks) {
            block.registerTextures();
            EntityModel model = block.model;
            if (model == null) continue;
            for (CuboidModel cuboidModel: model.getCuboidModels()) {
                for (CuboidFace cuboidFace: cuboidModel.getFaces()) {
                    Texture t = cuboidFace.getTexture();
                    if (!textures.contains(t)) textures.add(t);
                }
            }
        }

        this.textures = new Texture[textures.size()];
        this.textures = textures.toArray(this.textures);
    }

    public void load() {
        if (textureId == -1) textureId = GL11.glGenTextures();


        GL13.glActiveTexture(GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_2D_ARRAY, textureId);


        int layerCount = 0;
        TextureLoader[] textureLoaders = new TextureLoader[textures.length];

        for (int i = 0; i < textures.length; i++) {
            TextureLoader loader = TextureLoader.load("/textures/" + textures[i].name + ".png");

            int texCount = loader.tHeight / TEXTURE_SIZE;
            textureLoaders[i] = loader;
            layerCount += texCount;
        }

        glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, TEXTURE_SIZE, TEXTURE_SIZE, layerCount);

        int currentLayer = 0;

        for (int i = 0; i < textures.length; i++) {
            TextureLoader loader = textureLoaders[i];

            int texCount = loader.tHeight / TEXTURE_SIZE;

            glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, currentLayer, TEXTURE_SIZE, TEXTURE_SIZE, texCount, GL_RGBA, GL_UNSIGNED_BYTE, loader.buf);

            textures[i].setLayer(currentLayer);
            textures[i].setLayerCount(texCount);

            currentLayer += texCount;
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
