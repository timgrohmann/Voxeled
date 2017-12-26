package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Wood extends Block {
    public Wood(Vector3 pos, Chunk chunk) {
        super(pos, Type.WOOD, chunk);
    }

    @Override
    public void registerTextures() {
        loadTextures("log_oak_top", "log_oak", "log_oak_top", false);
    }
}
