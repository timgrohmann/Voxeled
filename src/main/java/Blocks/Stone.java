package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Stone extends Block {
    public Stone(Vector3 pos, Chunk chunk) {
        super(pos, Type.STONE, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("blocks/stone");
    }
}
