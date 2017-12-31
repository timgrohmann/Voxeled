package World;

import Buffers.BlockABO;
import Entities.Block;
import GL_Math.Vector3;
import Models.CuboidFace;
import Models.Vertex;
import Structures.Tree;

import java.io.FileOutputStream;

/**
 * Defines a 16x16 block area
 */
public class Chunk {
    private Block[] blocks;

    public final World world;

    public static final int chunkSize = 16;
    static final int chunkHeight = 128;

    final int xOff;
    final int zOff;

    boolean willUnload = false;
    private boolean shouldReload = false;
    public boolean shouldUpdate = true;
    private boolean addedStructures = false;

    private BlockABO arrayBuffer;
    private BlockABO waterArrayBuffer;

    Chunk(World world, int xOff, int zOff) {
        this.world = world;
        this.xOff = xOff;
        this.zOff = zOff;
        generate();
        load();
    }

    Chunk(World world, byte[] bytes, int xPos, int zPos) {
        this.world = world;
        this.xOff = xPos;
        this.zOff = zPos;
        //Load from bytes
        blocks = new Block[blockCount()];
        assert(bytes.length == blocks.length);
        for (int i = 0; i < blocks.length; i++) {
            if (bytes[i] == -1) continue;
            //blocks[i] = new Block(vectorForIndex(i),bytes[i],this);
            blocks[i] = Block.fromByte(vectorForIndex(i),bytes[i],this);
        }
        load();
    }

    private void generate() {
        TerrainGenerator terrainGenerator = world.terrainGenerator;
        blocks = terrainGenerator.getBlocks(chunkSize * xOff, chunkSize * zOff, this);

        /*blocks = new Block[blockCount()];
        blocks[0] = new Block(0,0,0, Block.Type.GRASS);*/
    }

    /**
     * Tests block side visibility by checking neighbours and turning off renders for hidden faces.
     */
    private void testVisible() {
        for (int chunkY = 0; chunkY < Chunk.chunkHeight; chunkY++) {
            for (int chunkX = 0; chunkX < Chunk.chunkSize; chunkX++) {
                for (int chunkZ = 0; chunkZ < Chunk.chunkSize; chunkZ++) {
                    Block thisBlock = getBlockAt(chunkX,chunkY,chunkZ);
                    if (thisBlock == null) continue;
                    //boolean invisible = true;

                    if (chunkY > 0) {
                        Block bottomBlock = getBlockAt(chunkX,chunkY - 1,chunkZ);
                        thisBlock.setVisibleBottom(visibilityCheck(thisBlock,bottomBlock, CuboidFace.Face.BOTTOM));
                    }

                    if (chunkY < chunkHeight - 1) {
                        Block topBlock = getBlockAt(chunkX, chunkY + 1, chunkZ);
                        thisBlock.setVisibleTop(visibilityCheck(thisBlock,topBlock, CuboidFace.Face.TOP));
                    }

                    if (chunkX > 0) {
                        Block sideBlock = getBlockAt(chunkX - 1,chunkY,chunkZ);
                        thisBlock.setVisibleLeft(visibilityCheck(thisBlock,sideBlock, CuboidFace.Face.LEFT));
                    } else {
                        Chunk next = world.getChunk(xOff - 1, zOff);
                        if (next != null) {
                            Block otherBlock = next.getBlockAt(Chunk.chunkSize - 1,chunkY, chunkZ);
                            thisBlock.setVisibleLeft(visibilityCheck(thisBlock,otherBlock, CuboidFace.Face.LEFT));
                        }
                    }

                    if (chunkX < chunkSize - 1) {
                        Block sideBlock = getBlockAt(chunkX + 1, chunkY, chunkZ);
                        thisBlock.setVisibleRight(visibilityCheck(thisBlock,sideBlock, CuboidFace.Face.RIGHT));
                    } else {
                        Chunk next = world.getChunk(xOff + 1, zOff);
                        if (next != null) {
                            Block otherBlock = next.getBlockAt(0,chunkY, chunkZ);
                            thisBlock.setVisibleRight(visibilityCheck(thisBlock,otherBlock, CuboidFace.Face.RIGHT));
                        }
                    }

                    if (chunkZ > 0) {
                        Block sideBlock = getBlockAt(chunkX, chunkY, chunkZ - 1);
                        thisBlock.setVisibleBack(visibilityCheck(thisBlock,sideBlock, CuboidFace.Face.BACK));
                    } else {
                        Chunk next = world.getChunk(xOff, zOff - 1);
                        if (next != null) {
                            Block otherBlock = next.getBlockAt(chunkX,chunkY, Chunk.chunkSize - 1);
                            thisBlock.setVisibleBack(visibilityCheck(thisBlock,otherBlock, CuboidFace.Face.BACK));
                        }
                    }

                    if (chunkZ < chunkSize - 1) {
                        Block sideBlock = getBlockAt(chunkX,chunkY,chunkZ + 1);
                        thisBlock.setVisibleFront(visibilityCheck(thisBlock,sideBlock, CuboidFace.Face.FRONT));
                    } else {
                        Chunk next = world.getChunk(xOff, zOff + 1);
                        if (next != null) {
                            Block otherBlock = next.getBlockAt(chunkX,chunkY, 0);
                            thisBlock.setVisibleFront(visibilityCheck(thisBlock,otherBlock, CuboidFace.Face.FRONT));
                        }
                    }

                }
            }
        }
    }

    /**
     * Check if face between two blocks is visible.
     * Returns true for blocks with different transparency values.
     * @param thisBlock Input 1
     * @param otherBlock Input 2
     * @return True if face should be visible
     */
    private boolean visibilityCheck(Block thisBlock, Block otherBlock, CuboidFace.Face face) {
        if (otherBlock == null || (otherBlock.transparent && !thisBlock.transparent)) return true;
        if (otherBlock.model == null || thisBlock.model == null) return true;
        switch (face){
            case TOP:
                return !cullingForBlock(thisBlock, CuboidFace.Face.TOP)
                        || !cullingForBlock(otherBlock, CuboidFace.Face.BOTTOM);
            case BOTTOM:
                return !cullingForBlock(thisBlock,CuboidFace.Face.BOTTOM)
                        || !cullingForBlock(otherBlock,CuboidFace.Face.TOP);
            case LEFT:
                return !cullingForBlock(thisBlock,CuboidFace.Face.LEFT)
                        || !cullingForBlock(otherBlock,CuboidFace.Face.RIGHT);
            case RIGHT:
                return !cullingForBlock(thisBlock,CuboidFace.Face.RIGHT)
                        || !cullingForBlock(otherBlock,CuboidFace.Face.LEFT);
            case FRONT:
                return !cullingForBlock(thisBlock,CuboidFace.Face.FRONT)
                        || !cullingForBlock(otherBlock,CuboidFace.Face.BACK);
            case BACK:
                return !cullingForBlock(thisBlock,CuboidFace.Face.BACK)
                        || !cullingForBlock(otherBlock,CuboidFace.Face.FRONT);
        }
        return false;
        //return otherBlock == null || (otherBlock.transparent && !thisBlock.transparent);
    }

    private boolean cullingForBlock(Block b, CuboidFace.Face face) {
        //Map<CuboidFace.Face, CuboidFace> faceMap = b.model.getCuboidModels()[0].getFacesMap();
        //if (!faceMap.containsKey(face)) return false;
        return b.shouldCullFace(face);
    }


    /**
     * Load blocks to array buffer objects for later drawing
     */
    private void load() {
        testVisible();

        arrayBuffer = new BlockABO(world.renderer.worldShader);
        Vertex[] vertices = getVertices();
        arrayBuffer.load(vertices);
        arrayBuffer.loadAttributePointers();

        waterArrayBuffer = new BlockABO(world.renderer.worldShader);
        waterArrayBuffer.load(getTransparentVertices());
        waterArrayBuffer.loadAttributePointers();
    }

    void tick() {
        if (!shouldUpdate) return;
        for (Block b: blocks) {
            if (b != null && b.shouldUpdate) {
                b.tick();
            }
        }
        reload(true);
        shouldUpdate = false;
    }

    /**
     * Reload blocks if shouldReload or forced is true.
     * @param forced Ignores value of shouldReload if true.
     */
    void reload(boolean forced) {
        if (forced) shouldReload = true;
        if (!shouldReload) return;
        testVisible();
        arrayBuffer.load(getVertices());
        arrayBuffer.loadAttributePointers();

        waterArrayBuffer.load(getTransparentVertices());
        waterArrayBuffer.loadAttributePointers();

        shouldReload = false;
    }

    /**
     * Render all non-transparent blocks
     */
    void render() {
        arrayBuffer.bind();
        arrayBuffer.render();
    }

    /**
     * Render all transparent blocks (only water atm)
     */
    void renderWater() {
        waterArrayBuffer.bind();
        waterArrayBuffer.render();
    }

    private int getVertexCount() {
        int c = 0;
        for (Block e: blocks) {
            if (e == null || e.technicalBlock() || e.transparent) continue;
            c += e.vertexCount();
        }
        return c;
    }

    private int getTransparentVertexCount() {
        int c = 0;
        for (Block e: blocks) {
            if (e == null || e.technicalBlock() || !e.transparent) continue;
            c += e.vertexCount();
        }
        return c;
    }

    private Vertex[] getVertices() {
        int c = getVertexCount();
        Vertex[] vertices = new Vertex[c];

        int currentPos = 0;
        for (Block block: blocks) {
            if (block == null || block.technicalBlock() || block.transparent) continue;
            Vertex[] entityVertices = block.getVertices();
            for (Vertex entityVertex: entityVertices) {
                vertices[currentPos] = entityVertex;
                currentPos++;
            }
        }

        return vertices;
    }

    private Vertex[] getTransparentVertices() {
        int c = getTransparentVertexCount();
        Vertex[] vertices = new Vertex[c];

        int currentPos = 0;
        for (Block block: blocks) {
            if (block == null || block.technicalBlock() || !block.transparent) continue;
            Vertex[] entityVertices = block.getVertices();
            for (Vertex entityVertex: entityVertices) {
                vertices[currentPos] = entityVertex;
                currentPos++;
            }
        }

        return vertices;
    }

    public Block getBlockAt(int x, int y, int z) {
        return blocks[indexForPosition(x,y,z)];
    }

    void setBlockAt(Block block, int x, int y, int z){
        if (y < 0 || y > chunkHeight - 1) return;
        blocks[indexForPosition(x,y,z)] = block;
        shouldReload = true;

        changesToBlock(block);
    }


    /**
     * @param x row
     * @param y layer
     * @param z column
     * @return index
     */
    static int indexForPosition(int x, int y, int z) {
        return x + y * chunkSize * chunkSize + z * chunkSize;
    }

    private Vector3 vectorForIndex(int i) {
        int y = i / (chunkSize * chunkSize);
        int z = (i-y * chunkSize * chunkSize) / chunkSize;
        int x = (i - y * chunkSize * chunkSize - z * chunkSize);
        return new Vector3(x + chunkSize * xOff, y,z + chunkSize * zOff);
    }

    /**
     * @return Maximal possible block count.
     */
    static int blockCount() {
        return chunkSize * chunkSize * chunkHeight;
    }

    boolean isEqual(int x, int z){
        return  (x == xOff) && (z == zOff);
    }

    void addStructures() {
        for (Block b: this.blocks) {
            if (b == null) continue;
            if (b.type == Block.Type.__TREE_SPAWNER) {
                new Tree(this.world,b.getXPos(),b.getYPos(),b.getZPos()).add();
            }
        }
    }

    public void removeBlock(Block block) {
        blocks[indexForPosition((int) block.getPos().x - xOff * Chunk.chunkSize,(int) block.getPos().y,(int) block.getPos().z - zOff * Chunk.chunkSize)] = null;

        shouldReload = true;
        shouldUpdate = true;
        world.updateBlocksAround(block.getPos());
        changesToBlock(block);
    }

    private void changesToBlock(Block block) {
        if (Math.floorMod(block.getXPos(),Chunk.chunkSize) == Chunk.chunkSize - 1) {
            Chunk neighbour = world.getChunk(xOff + 1, zOff);
            if (neighbour != null) neighbour.load();
        }
        if (Math.floorMod(block.getXPos(),Chunk.chunkSize) == 0) {
            Chunk neighbour = world.getChunk(xOff - 1, zOff);
            if (neighbour != null) neighbour.load();
        }
        if (Math.floorMod(block.getZPos(),Chunk.chunkSize) == Chunk.chunkSize - 1) {
            Chunk neighbour = world.getChunk(xOff, zOff + 1);
            if (neighbour != null) neighbour.load();
        }
        if (Math.floorMod(block.getZPos(),Chunk.chunkSize) == 0) {
            Chunk neighbour = world.getChunk(xOff, zOff - 1);
            if (neighbour != null) neighbour.load();
        }
    }

    /**
     * Adds structures if all surrounding chunks are loaded
     */
    void mayAddStructures() {
        if (addedStructures) return;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (world.getChunk(xOff + i, zOff + j) == null) return;
            }
        }

        addStructures();
        addedStructures = true;
    }

    /**
     * Saves chunk's block's byte data to .chunk file in /world/chunks
     */
    void saveToFile() {
        String fileName = "world/chunks/c_" + String.valueOf(xOff) + "_" + String.valueOf(zOff) + ".chunk";

        byte[] data = new byte[blockCount()];

        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] == null) {
                data[i] = -1;
                continue;
            }
            data[i] = blocks[i].store();
        }

        try{
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(data);
            out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
