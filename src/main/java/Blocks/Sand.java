package Blocks;

import GL_Math.Vector3;
import World.Chunk;

public class Sand extends GravityBlock {
    public Sand(Vector3 pos, Chunk chunk) {
        super(pos, Type.SAND, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("blocks/sand");
    }

}
