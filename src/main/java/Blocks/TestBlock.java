package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class TestBlock extends Block {
    public TestBlock(Vector3 pos, Chunk chunk) {
        super(pos, Type.TEST_TYPE, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("block/stone");
    }
}