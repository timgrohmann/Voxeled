package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Leaves extends Block {
    public Leaves(Vector3 pos, Chunk chunk) {
        super(pos, Type.LEAVES, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("blocks/leaves");
    }
}
