package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import World.Chunk;

public class Logs extends Block{
    public Logs(Vector3 pos, Chunk chunk) {
        super(pos, Type.LOGS, chunk);
        loadTextures(4);
    }
}
