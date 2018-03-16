package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import Models.CuboidFace;
import World.Chunk;

public class Torch extends Block {
    public Torch(Vector3 pos, Chunk chunk) {
        super(pos, Type.TORCH, chunk);
        options.put("facing", "down");
    }

    @Override
    public void registerTextures() {
        loadModel("blocks/torch");
    }

    @Override
    public void updateOptionsWithPlacePos(Vector3 conPos) {
        super.updateOptionsWithPlacePos(conPos);
        Block bottomBlock = chunk.world.getBlockForCoordinates(pos.added(new Vector3(0,-1,0)));

        float x0 = conPos.x - (float) Math.floor(conPos.x);
        float z0 = conPos.z - (float) Math.floor(conPos.z);
        float tol = 0.05f;

        if (x0 < tol) {
            options.put("facing", "east");
        } else if (x0 > 1 - tol) {
            options.put("facing", "west");
        } else if (z0 < tol) {
            options.put("facing", "south");
        } else if (z0 > 1 - tol) {
            options.put("facing", "north");
        } else if (bottomBlock != null && bottomBlock.shouldCullFace(CuboidFace.Face.TOP)) {
            options.put("facing", "down");
        } else {
            chunk.removeBlock(this);
        }
    }

    /*@Override
    public HitBox getHitbox() {
        return null;
    }*/
}
