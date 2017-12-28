package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import Models.Vertex;
import World.Chunk;

public class PlanksSlab extends Block {
    public PlanksSlab(Vector3 pos, Chunk chunk) {
        super(pos, Type.PLANKS_SLAB, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("block/planks_slab");
    }

    @Override
    public Vertex[] getVertices() {
        return super.getVertices();
    }
}
