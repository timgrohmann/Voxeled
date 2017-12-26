package World;

import Entities.Block;
import GL_Math.Vector3;
import Noise.PerlinNoise;

import java.util.Random;

class TerrainGenerator {
    private final int size;

    private final double seed;
    private final World world;

    private final PerlinNoise terrainGen;

    private final Random random;

    private final Block[] outEntities;
    private Chunk currentChunk;

    private final WorldGenElement sandDecider;

    private final WorldGenElement[] elements = new WorldGenElement[]{
            new WorldGenElement(0.05,5,0),
            new WorldGenElement(0.01,20,68),
    };

    public TerrainGenerator(World world) {
        this.size = Chunk.chunkSize;
        this.world = world;
        this.seed = world.seed;
        terrainGen = new PerlinNoise(this.seed);

        random = new Random();
        random.setSeed((long) (0.1 * seed));

        outEntities = new Block[Chunk.blockCount()];

        sandDecider = new WorldGenElement(0.04,2,0,12,120);
    }

    public Block[] getBlocks(int offX, int offY, Chunk chunk) {

        for (int i = 0; i < outEntities.length; i++) {
            outEntities[i] = null;
        }
        Random randomGenerator = chunk.world.generalPurposeRandom;
        currentChunk = chunk;

        for (int i = 0; i < size; i++) {


            for (int j = 0; j < size; j++) {

                int realX = i + offX;
                int realY = j + offY;

                int height  = getHeight(realX,realY);

                int dirtHeight = height - 8;

                setBlock(i,0,j, Block.Type.BEDROCK);
                for (int m = 1; m < dirtHeight; m++) {
                    setBlock(i,m,j, Block.Type.STONE);
                    //outEntities[Chunk.indexForPosition(i,j,m)] = new Block(new Vector3(realX,(float)m,realY), Block.Type.STONE, chunk);
                }


                for (int m = dirtHeight; m < height; m++) {
                    setBlock(i,m,j, Block.Type.DIRT);
                }

                if (height < 66 + sandDecider.height(realX,realY)) {
                    for (int w = height; w <= 64; w++) {
                        setBlock(i,w,j, Block.Type.WATER);
                    }
                    setBlock(i,height,j, Block.Type.SAND);
                }else {
                    setBlock(i,height,j, Block.Type.GRASS);
                }

                if (randomGenerator.nextDouble() < 0.005 && height > 66) {
                    setBlock(i,height + 1,j, Block.Type.__TREE_SPAWNER);
                }

            }
        }

        Block[] copy = new Block[outEntities.length];
        System.arraycopy(outEntities, 0, copy, 0,
                outEntities.length);
        return copy;
    }

    private void setBlock(int i, int h, int j, Block.Type type) {
        int realX = i + currentChunk.xOff * Chunk.chunkSize;
        int realY = j + currentChunk.zOff * Chunk.chunkSize;
        outEntities[Chunk.indexForPosition(i,h,j)] = type.createInstance(new Vector3(realX,(float)h,realY), currentChunk);
    }

    private int getHeight(int x, int y) {
        double scale = 0.03;

        float currentHeight = 0;




        for (WorldGenElement element: elements) {
            currentHeight += element.height(x,y);
        }

        return (int) Math.floor(currentHeight);
    }

    class WorldGenElement{
        final double inScale;
        final double outScale;
        final double bias;
        final double xOff;
        final double yOff;

        WorldGenElement(double inScale, double outScale, double bias) {
            this.inScale = inScale;
            this.outScale = outScale;
            this.bias = bias;
            this.xOff = this.yOff = 0;
        }

        WorldGenElement(double inScale, double outScale, double bias, double xOff, double yOff) {
            this.inScale = inScale;
            this.outScale = outScale;
            this.bias = bias;
            this.xOff = xOff;
            this.yOff = yOff;
        }

        double height(int x, int y) {
            double xP = (double) x * inScale;
            double yP = (double) y * inScale;
            //return bias;
            return terrainGen.noise(xP + xOff,yP + yOff) * outScale + bias;
        }
    }
}
