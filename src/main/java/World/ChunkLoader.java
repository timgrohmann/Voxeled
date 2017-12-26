package World;

import java.nio.file.Files;
import java.nio.file.Paths;

class ChunkLoader {
    final int xPos;
    final int yPos;

    ChunkLoader(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    Chunk load(World world) {
        Chunk readChunk = readFromFile(world);

        if (readChunk == null) {
            return world.addChunk(xPos,yPos);
        } else {
            return world.addChunk(readChunk);
        }
    }

    private Chunk readFromFile(World world) {
        String fileName = "world/chunks/c_" + String.valueOf(xPos) + "_" + String.valueOf(yPos) + ".chunk";

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));

            return new Chunk(world, bytes, xPos, yPos);
        }catch (Exception e) {
            return null;
        }
    }
}
