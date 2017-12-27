package Entities;

import Blocks.*;
import GL_Math.Vector3;
import Models.*;
import Registry.BlockRegistry;
import Textures.Texture;
import World.Chunk;

import java.lang.reflect.Constructor;

abstract public class Block extends DrawableEntity implements Collidable {
    private boolean visibleTop = true, visibleBottom = true,
            visibleLeft = true, visibleRight = true,
            visibleBack = true, visibleFront = true;

    private Texture topTexture = null;
    private Texture sideTexture = null;
    private Texture bottomTexture = null;

    public final Chunk chunk;
    private CubeModel model;

    public boolean transparent = false;

    public final Type type;

    protected Block(Vector3 pos, Type type, Chunk chunk) {
        super(pos);
        this.chunk = chunk;
        this.model = new CubeModel();
        this.type = type;

        if (chunk != null) {
            BlockRegistry registry = chunk.world.renderer.registry;
            Block singleton = registry.getBlockSingletonForType(type);
            topTexture = singleton.topTexture;
            sideTexture = singleton.sideTexture;
            bottomTexture = singleton.bottomTexture;
            transparent = singleton.transparent;
        }
    }

    public static Block fromByte(Vector3 pos, byte typeByte, Chunk chunk) {
        Type newType = null;
        for (Type t: Type.values()) {
            if (t.store_value == typeByte) {
                newType = t;
                break;
            }
        }

        if (newType == null) return null;

        return newType.createInstance(pos, chunk);
    }

    public abstract void registerTextures();

    protected void loadTextures(String topName, String sideName, String bottomName, boolean transparent, boolean foliage) {
        topTexture = new Texture(topName, foliage);
        sideTexture = new Texture(sideName);
        bottomTexture = new Texture(bottomName);

        this.transparent = transparent;
    }
    protected void loadTextures(String allName, boolean transparent, boolean foliage) {
        topTexture = sideTexture = bottomTexture = new Texture(allName,foliage);
        this.transparent = transparent;
    }

    protected void loadTextures(String allName) {
        loadTextures(allName,false,false);
    }

    public int vertexCount() {
        return (visibleTop ? 6 : 0) + (visibleBottom ? 6 : 0) + (visibleLeft ? 6 : 0) + (visibleRight ? 6 : 0)
                + (visibleFront ? 6 : 0) + (visibleBack ? 6 : 0);
    }

    public Vertex[] getVertices() {
        Vertex[] list = new Vertex[vertexCount()];
        int currentPointer = 0;

        if (visibleTop) {
            ModelVertex[] modelVertices = BlockFaceModel.getVertices(BlockFaceModel.Side.TOP);
            for (int i = 0; i < 6; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model,topTexture);
                currentPointer++;
            }
        }
        if (visibleBottom) {
            ModelVertex[] modelVertices = BlockFaceModel.getVertices(BlockFaceModel.Side.BOTTOM);
            for (int i = 0; i < 6; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model,bottomTexture);
                currentPointer++;
            }
        }
        if (visibleLeft) {
            ModelVertex[] modelVertices = BlockFaceModel.getVertices(BlockFaceModel.Side.LEFT);
            for (int i = 0; i < 6; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model,sideTexture);
                currentPointer++;
            }
        }
        if (visibleRight) {
            ModelVertex[] modelVertices = BlockFaceModel.getVertices(BlockFaceModel.Side.RIGHT);
            for (int i = 0; i < 6; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model,sideTexture);
                currentPointer++;
            }
        }
        if (visibleFront) {
            ModelVertex[] modelVertices = BlockFaceModel.getVertices(BlockFaceModel.Side.FRONT);
            for (int i = 0; i < 6; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model,sideTexture);
                currentPointer++;
            }
        }
        if (visibleBack) {
            ModelVertex[] modelVertices = BlockFaceModel.getVertices(BlockFaceModel.Side.BACK);
            for (int i = 0; i < 6; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model,sideTexture);
                currentPointer++;
            }
        }

        return list;
    }

    public Vertex[] getEdgeVertices() {
        Vertex[] list = new Vertex[vertexCount() / 6 * 8];
        int currentPointer = 0;

        if (visibleTop) {
            Vector3[] modelVertices = BlockFaceModel.getEdgeVertices(BlockFaceModel.Side.TOP);
            for (int i = 0; i < 8; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model);
                currentPointer++;
            }
        }
        if (visibleBottom) {
            Vector3[] modelVertices = BlockFaceModel.getEdgeVertices(BlockFaceModel.Side.BOTTOM);
            for (int i = 0; i < 8; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model);
                currentPointer++;
            }
        }
        if (visibleLeft) {
            Vector3[] modelVertices = BlockFaceModel.getEdgeVertices(BlockFaceModel.Side.LEFT);
            for (int i = 0; i < 8; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model);
                currentPointer++;
            }
        }
        if (visibleRight) {
            Vector3[] modelVertices = BlockFaceModel.getEdgeVertices(BlockFaceModel.Side.RIGHT);
            for (int i = 0; i < 8; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model);
                currentPointer++;
            }
        }
        if (visibleFront) {
            Vector3[] modelVertices = BlockFaceModel.getEdgeVertices(BlockFaceModel.Side.FRONT);
            for (int i = 0; i < 8; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model);
                currentPointer++;
            }
        }
        if (visibleBack) {
            Vector3[] modelVertices = BlockFaceModel.getEdgeVertices(BlockFaceModel.Side.BACK);
            for (int i = 0; i < 8; i++) {
                list[currentPointer] = new Vertex(modelVertices[i],this.pos,this.model);
                currentPointer++;
            }
        }

        return list;
    }

    public void tick() {}

    /**
     * Handles left-click
     */
    public void primaryInteraction() {
        destroy();
    }

    public void destroy() { chunk.removeBlock(this);}

    public byte store() {
        return type.store_value;
    }

    public enum Type {
        GRASS (1, Grass.class),
        STONE (2, Stone.class),
        LOGS (3, Blocks.Logs.class),
        DIRT (4, Dirt.class),
        WOOD (5, Wood.class),
        LEAF (6, Leaf.class),
        SAND (7, Sand.class),
        __TREE_SPAWNER (8, TreeSpawner.class),
        WATER (9, Water.class),
        BEDROCK (10, Bedrock.class);

        final byte store_value;

        final Class<? extends Block> blockClass;

        Type(int i, Class<? extends Block> blockClass) {
            store_value = (byte) i;
            this.blockClass = blockClass;
        }

        public Block createInstance(Vector3 pos, Chunk chunk) {
            try {
                @SuppressWarnings("JavaReflectionMemberAccess") Constructor<? extends Block> constructor = blockClass.getConstructor(Vector3.class, Chunk.class);
                return constructor.newInstance(pos, chunk);
            }catch (Exception e) {
                System.err.format("Fatal error:%nNo constructor found for %s!%n%n", blockClass.getName());
                e.printStackTrace();
                System.exit(-1);
            }

            return null;
        }

        public Block createSingletonInstance() {
            return createInstance(Vector3.zero(),null);
        }
    }

    public void setVisibleTop(boolean visibleTop) {
        this.visibleTop = visibleTop;
    }
    public void setVisibleBottom(boolean visibleBottom) {
        this.visibleBottom = visibleBottom;
    }
    public void setVisibleLeft(boolean visibleLeft) {
        this.visibleLeft = visibleLeft;
    }
    public void setVisibleRight(boolean visibleRight) {
        this.visibleRight = visibleRight;
    }
    public void setVisibleBack(boolean visibleBack) {
        this.visibleBack = visibleBack;
    }
    public void setVisibleFront(boolean visibleFront) {
        this.visibleFront = visibleFront;
    }

    public boolean isVisibleTop() {
        return visibleTop;
    }
    public boolean isVisibleBottom() {
        return visibleBottom;
    }
    public boolean isVisibleLeft() {
        return visibleLeft;
    }
    public boolean isVisibleRight() {
        return visibleRight;
    }
    public boolean isVisibleBack() {
        return visibleBack;
    }
    public boolean isVisibleFront() {
        return visibleFront;
    }

    public int getXPos() {return (int) this.pos.x;}
    public int getYPos() {return (int) this.pos.y;}
    public int getZPos() {return (int) this.pos.z;}

    public Texture getTopTexture() {
        return topTexture;
    }

    public Texture getSideTexture() {
        return sideTexture;
    }

    public Texture getBottomTexture() {
        return bottomTexture;
    }

    public boolean shouldMakeVertices() {
        return this.type != Type.__TREE_SPAWNER;
    }

    @Override
    public HitBox getHitbox() {
        return new HitBox(new Vector3(1f,1f,1f), this, new Vector3(0.5f,0.5f,0.5f));
    }
}
