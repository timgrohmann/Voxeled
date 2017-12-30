package Blocks;

import Entities.Block;
import Entities.HitBox;
import GL_Math.Vector3;
import World.Chunk;

public class Water extends Block {
    public Water(Vector3 pos, Chunk chunk) {
        super(pos, Type.WATER, chunk);
    }

    @Override
    public void registerTextures() {
        loadModel("blocks/water");
    }

    @Override
    public HitBox getHitbox() {
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (chunk.world.getBlockForCoordinates(pos.added(new Vector3(1,0,0))) == null) {
            chunk.world.setBlockForCoordinates(Type.WATER, pos.added(new Vector3(1,0,0)));
        }
        if (chunk.world.getBlockForCoordinates(pos.added(new Vector3(-1,0,0))) == null) {
            chunk.world.setBlockForCoordinates(Type.WATER, pos.added(new Vector3(-1,0,0)));
        }
        if (chunk.world.getBlockForCoordinates(pos.added(new Vector3(0,0,1))) == null) {
            chunk.world.setBlockForCoordinates(Type.WATER, pos.added(new Vector3(0,0,1)));
        }
        if (chunk.world.getBlockForCoordinates(pos.added(new Vector3(0,0,-1))) == null) {
            chunk.world.setBlockForCoordinates(Type.WATER, pos.added(new Vector3(0,0,-1)));
        }
        if (chunk.world.getBlockForCoordinates(pos.added(new Vector3(0,-1,0))) == null) {
            chunk.world.setBlockForCoordinates(Type.WATER, pos.added(new Vector3(0,-1,0)));
        }
    }
}
