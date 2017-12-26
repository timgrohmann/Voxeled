package Blocks;

import Entities.Block;
import Entities.HitBox;
import GL_Math.Vector3;
import World.Chunk;

public class Water extends Block {
    public Water(Vector3 pos, Chunk chunk) {
        super(pos, Type.WATER, chunk);
        loadTextures(205,true);
    }

    @Override
    public HitBox getHitbox() {
        return null;
    }
}
