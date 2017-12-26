package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import Models.Vertex;
import World.Chunk;

public class LogsSlab extends Block {
    public LogsSlab(Vector3 pos, Chunk chunk) {
        super(pos, Block.Type.LOGS, chunk);
    }

    @Override
    public void registerTextures() {
        loadTextures("planks_oak");
    }

    @Override
    public Vertex[] getVertices() {
        return super.getVertices();
    }
}
