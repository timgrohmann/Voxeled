package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class TreeSpawner extends Block {
    public TreeSpawner(Vector3 pos, Chunk chunk) {
        super(pos, Type.__TREE_SPAWNER, chunk);
    }
}
