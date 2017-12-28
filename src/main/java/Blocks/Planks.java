package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Planks extends Block{
    public Planks(Vector3 pos, Chunk chunk) {
        super(pos, Type.PLANKS, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("block/planks");
    }
}
