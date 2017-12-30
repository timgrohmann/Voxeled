package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import Models.CuboidFace;
import World.Chunk;

import java.util.HashMap;
import java.util.Map;

public class Fence extends Block{
    public Fence(Vector3 pos, Chunk chunk) {
        super(pos, Type.FENCE, chunk);
        this.options.put("north", "false");
        this.options.put("south", "true");
        this.options.put("west", "false");
        this.options.put("east", "true");
        shouldUpdate = true;
    }


    @Override
    public void tick() {
        super.tick();

        Block north = chunk.world.getBlockForCoordinates(this.pos.added(new Vector3(0,0,1)));
        this.options.putBool("north", shouldConnectWith(north, CuboidFace.Face.FRONT));

        Block south = chunk.world.getBlockForCoordinates(this.pos.added(new Vector3(0,0,-1)));
        this.options.putBool("south", shouldConnectWith(south, CuboidFace.Face.BACK));

        Block west = chunk.world.getBlockForCoordinates(this.pos.added(new Vector3(-1,0,0)));
        this.options.putBool("west", shouldConnectWith(west, CuboidFace.Face.LEFT));

        Block east = chunk.world.getBlockForCoordinates(this.pos.added(new Vector3(1,0,0)));
        this.options.putBool("east", shouldConnectWith(east, CuboidFace.Face.RIGHT));

    }

    private boolean shouldConnectWith(Block b, CuboidFace.Face face) {
        return b != null && (b instanceof Fence || b.shouldCullFace(face.opposite()));
    }



    @Override
    public void registerTextures() {
        loadModel("blocks/fence");
    }
}
