package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Glass extends Block {
    public Glass(Vector3 pos, Chunk chunk) {
        super(pos, Type.GLASS, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("blocks/glass");
    }
}
