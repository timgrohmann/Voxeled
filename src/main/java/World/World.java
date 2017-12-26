package World;

import Entities.Block;
import GL_Math.Matrix4;
import GL_Math.Vector3;
import Textures.BlockTextures;
import Main_Package.Renderer;

import java.util.ArrayList;
import java.util.Random;

public class World {
    public final ArrayList<Chunk> chunks;

    final double seed;

    final TerrainGenerator terrainGenerator;

    public final Renderer renderer;
    public final Random generalPurposeRandom;
    public final BlockTextures blockTextures;

    private int currentCenterX;
    private int currentCenterY;

    private final ArrayList<ChunkLoader> loaders;

    /**
     * Constructor with set world seed
     */
    public World(double seed, Renderer renderer) {
        this.seed = seed;
        this.renderer = renderer;
        terrainGenerator = new TerrainGenerator(this);
        chunks = new ArrayList<Chunk>();
        loaders = new ArrayList<ChunkLoader>();
        generalPurposeRandom = new Random((long) (seed * 100));
        blockTextures = new BlockTextures(renderer.registry);
    }

    public void loadTexture() {
        blockTextures.load();
    }

    /**
     * Makes and adds chunk to world at chunk position (1 chunk = 1 unit)
     */
    public Chunk addChunk(int x, int y) {
        if (getChunk(x,y) != null) return null;

        Chunk newChunk = new Chunk(this,x,y);
        newChunk.addStructures();
        chunks.add(newChunk);
        return newChunk;
    }

    /**
     * Adds pre-generated or pre-loaded Chunk
     */
    public Chunk addChunk(Chunk chunk) {
        if (getChunk(chunk.xOff,chunk.zOff) != null) return null;
        chunk.addStructures();
        chunks.add(chunk);
        return chunk;
    }

    /**
     * @param x x coordinate of searched chunk
     * @param z z coordinate of searched chunk
     * @return Found chunk, null if chunk not loaded.
     */
    Chunk getChunk(int x, int z) {
        for (Chunk c: chunks) {
            if (c.isEqual(x,z)) return c;
        }
        return null;
    }

    /**
     * Renders non-transparent blocks in this chunk.
     * @param mat transformation matrix to use
     */
    public void render(Matrix4 mat){
        renderer.worldShader.use();
        renderer.worldShader.setUniformVector("light_dir", renderer.camera.getLightPos());
        renderer.worldShader.setUniformMatrix("mat",mat);
        blockTextures.activateTextures();


        for (Chunk chunk: chunks){
            chunk.render();
        }
    }

    /**
     * Renders transparent blocks <i>(water)</i> in this chunk.
     * @param mat transformation matrix to use
     */
    public void renderWater(Matrix4 mat) {
        renderer.waterShader.use();
        renderer.waterShader.setUniformVector("light_dir", renderer.camera.getLightPos());
        renderer.waterShader.setUniformMatrix("mat",mat);
        for (Chunk chunk: chunks){
            chunk.renderWater();
        }
    }

    /**
     * Sets a block at the specified type at world coordinates
     * @param type type to set
     * @param x x position
     * @param y y position
     * @param z z position
     */
    public void setBlockForCoordinates(Block.Type type, int x, int y, int z) {
        int inChunkX = Math.floorMod(x, Chunk.chunkSize);
        int inChunkZ = Math.floorMod(z, Chunk.chunkSize);

        int chunkPosX = (x - inChunkX) / Chunk.chunkSize;
        int chunkPosZ = (z - inChunkZ) / Chunk.chunkSize;

        Chunk chunk = null;
        for (Chunk chunkCandidate : chunks){
            if (chunkCandidate.isEqual(chunkPosX,chunkPosZ)) {
                chunk = chunkCandidate;
            }
        }

        if (chunk == null) {
            return;
        }

        Block block = type.createInstance(new Vector3(x,y,z), chunk);
        chunk.setBlockAt(block, inChunkX, y, inChunkZ);

    }


    /**
     * Get block at position.
     * @param pos Components will be rounded to integer values.
     * @return The block found.
     */
    public Block getBlockForCoordinates(Vector3 pos) {
        int x = (int) Math.floor(pos.x);
        int y = (int) Math.floor(pos.y);
        int z = (int) Math.floor(pos.z);

        if (y > Chunk.chunkHeight - 1 || y < 0) return null;

        int inChunkX = Math.floorMod(x, Chunk.chunkSize);
        int inChunkZ = Math.floorMod(z, Chunk.chunkSize);

        int chunkPosX = (x - inChunkX) / Chunk.chunkSize;
        int chunkPosZ = (z - inChunkZ) / Chunk.chunkSize;

        Chunk chunk = getChunk(chunkPosX,chunkPosZ);
        if (chunk == null) return null;

        Block b = chunk.getBlockAt(inChunkX,y,inChunkZ);

        if (b == null) return null;

        return b;
    }


    /**
     * Check if chunk is already on load list
     * @param x Chunk's x offset
     * @param y Chunk's y offset
     * @return True if chunk is on load list, false otherwise.
     */
    private boolean chunkWillBeLoaded(int x, int y) {
        for (ChunkLoader loader: loaders) {
            if (loader.xPos == x && loader.yPos == y) return true;
        }
        return false;
    }

    /**
     * Adds chunk around position in a spiral-like pattern.
     * @param position Center's position (y-component will be ignored)
     * @param radius Radius around center to load
     */
    private void addNearChunksToLoadList(Vector3 position, int radius) {
        int chunkCenterX = (int) (position.x / Chunk.chunkSize);
        int chunkCenterY = (int) (position.z / Chunk.chunkSize);

        if (chunkCenterX == currentCenterX && chunkCenterY == currentCenterY && chunks.size() != 0) return;

        currentCenterX = chunkCenterX;
        currentCenterY = chunkCenterY;

        //Spiral-like pattern

        for (int rad = radius; rad >= 0; rad--) {
            for (int off = -rad; off <= rad; off++) {
                addLoadChunk(chunkCenterX + off, chunkCenterY - rad);
            }
            for (int off = -rad; off <= rad; off++) {
                addLoadChunk(chunkCenterX + off, chunkCenterY + rad);
            }

            for (int off = -rad + 1; off < rad; off++) {
                addLoadChunk(chunkCenterX + rad, chunkCenterY + off);
            }
            for (int off = -rad + 1; off < rad; off++) {
                addLoadChunk(chunkCenterX - rad, chunkCenterY + off);
            }
        }

        addLoadChunk(chunkCenterX,chunkCenterY);
    }


    /**
     * Adds chunk to load list if not already on it.
     * @param x Chunk's x offset
     * @param z Chunk's y offset
     */
    private void addLoadChunk(int x, int z) {
        Chunk c = getChunk(x, z);
        if (c == null && !chunkWillBeLoaded(x, z)){
            ChunkLoader newLoader = new ChunkLoader(x, z);
            loaders.add(newLoader);
        }
    }

    /**
     * Loads a single chunk from the load list if not empty
     */
    private void loadOneChunk() {
        if (loaders.size() > 0) {
            ChunkLoader loader = loaders.get(loaders.size() - 1);
            Chunk addedChunk = loader.load(this);
            reloadNeighbours(addedChunk.xOff, addedChunk.zOff);
            loaders.remove(loader);
        }

        for (Chunk c: chunks) {
            c.mayAddStructures();
        }
    }

    /**
     * Reload chunks on top, right, bottom and left side.
     * @param x Center chunk's x offset.
     * @param y Center chunk's y offset.
     */
    private void reloadNeighbours(int x, int y) {
        Chunk n1 = getChunk(x + 1,y);
        if (n1 != null) n1.reload(true);

        Chunk n2 = getChunk(x - 1,y);
        if (n2 != null) n2.reload(true);

        Chunk n3 = getChunk(x,y + 1);
        if (n3 != null) n3.reload(true);

        Chunk n4 = getChunk(x,y - 1);
        if (n4 != null) n4.reload(true);
    }

    /**
     * <b>Updates whole world</b>
     */
    public void tick() {
        this.addNearChunksToLoadList(renderer.camera.getPosition(), 5);
        this.unloadChunksOutsideOf(renderer.camera.getPosition(),7);

        this.loadOneChunk();


        for (Chunk chunk: chunks) chunk.tick();

        for (int i = chunks.size() - 1; i >= 0; i--) {
            if (chunks.get(i).willUnload) chunks.remove(i);
        }
    }

    /**
     * Unloads chunks that are to far away to stop memory overflows
     * @param position Player position
     * @param radius Unload radius
     */
    private void unloadChunksOutsideOf(Vector3 position, int radius) {
        int chunkCenterX = (int) (position.x / Chunk.chunkSize);
        int chunkCenterY = (int) (position.z / Chunk.chunkSize);

        for (Chunk c: chunks) {
            if (Math.abs(c.xOff - chunkCenterX) > radius || Math.abs(c.zOff - chunkCenterY) > radius) c.willUnload = true;
        }
    }

    /**
     * Stores all <i>loaded</i> chunks to files.
     */
    public void store() {
        renderer.player.saveToFile();
        for (Chunk c: chunks) {
            c.saveToFile();
        }
    }


}
