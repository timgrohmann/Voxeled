package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Dirt extends Block {
    public Dirt(Vector3 pos, Chunk chunk) {
        super(pos, Type.DIRT, chunk);
    }

    @Override
    public void registerTextures() {
        loadTextures("dirt");
    }
}
