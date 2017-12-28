package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Log extends Block {
    public Log(Vector3 pos, Chunk chunk) {
        super(pos, Type.WOOD, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("block/log");
    }
}
