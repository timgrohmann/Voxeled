package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Sand extends Block {
    public Sand(Vector3 pos, Chunk chunk) {
        super(pos, Type.SAND, chunk);
        loadTextures(18);
    }
}
