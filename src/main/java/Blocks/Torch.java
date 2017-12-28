package Blocks;

import Entities.Block;
import Entities.HitBox;
import GL_Math.Vector3;
import World.Chunk;

public class Torch extends Block {
    public Torch(Vector3 pos, Chunk chunk) {
        super(pos, Type.TORCH, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("block/normal_torch");
    }

    @Override
    public HitBox getHitbox() {
        return null;
    }
}
