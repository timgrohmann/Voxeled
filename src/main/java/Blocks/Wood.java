package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Wood extends Block {
    public Wood(Vector3 pos, Chunk chunk) {
        super(pos, Type.WOOD, chunk);
        loadTextures(21,20,21,false);
    }
}
