package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Bedrock extends Block {
    public Bedrock(Vector3 pos, Chunk chunk) {
        super(pos, Type.BEDROCK, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("blocks/bedrock");
    }

    @Override
    public void primaryInteraction() {
        // Not mine-able!
    }
}
