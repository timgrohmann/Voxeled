package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Grass extends Block{
    public Grass(Vector3 pos, Chunk chunk) {
        super(pos, Type.GRASS, chunk);
        loadTextures(0,3,2,false);
    }
}