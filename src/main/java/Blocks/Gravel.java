package Blocks;

import GL_Math.Vector3;
import World.Chunk;

public class Gravel extends GravityBlock{
    public Gravel(Vector3 pos, Chunk chunk) {
        super(pos, Type.GRAVEL, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("block/gravel");
    }

}
