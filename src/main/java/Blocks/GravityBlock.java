package Blocks;

import Entities.Block;
import Entities.FallingBlock;
import GL_Math.Vector3;
import World.Chunk;

abstract public class GravityBlock extends Block{
    public GravityBlock(Vector3 pos, Type type, Chunk chunk) {
        super(pos, type, chunk);
    }

    @Override
    public void tick() {
        super.tick();

        Block below = chunk.world.getBlockForCoordinates(this.pos.added(new Vector3(0,-1,0)));
        if (below == null || below.getHitbox() == null) {
            destroy();
            FallingBlock fallingBlock = new FallingBlock(this);
            chunk.world.entities.add(fallingBlock);
        }
    }
}
