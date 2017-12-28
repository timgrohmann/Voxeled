package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Leaf extends Block {
    public Leaf(Vector3 pos, Chunk chunk) {
        super(pos, Type.LEAF, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("block/leaf");
    }
}
