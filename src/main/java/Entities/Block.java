package Entities;

import Blocks.*;
import GL_Math.Vector3;
import Player.Player;
import Models.*;
import Registry.BlockRegistry;
import World.Chunk;

import java.lang.reflect.Constructor;
import java.util.Map;

abstract public class Block extends DrawableEntity implements Collidable {

    private Culling culling = new Culling(true);
    public ModelOptions options = new ModelOptions();

    public boolean shouldUpdate = false;

    public final Chunk chunk;

    public boolean transparent = false;

    public final Type type;

    protected Block(Vector3 pos, Type type, Chunk chunk) {
        super(pos);
        this.chunk = chunk;
        this.type = type;

        if (chunk != null) {
            BlockRegistry registry = chunk.world.renderer.registry;
            Block singleton = registry.getBlockSingletonForType(type);
            model = singleton.model;
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

    protected void loadModel(String modelName) {
        loadModel(new EntityModelLoader().loadState(modelName));
    }

    protected void loadModel(EntityModel model) {
        this.model = model;
        transparent = model.transparent;
    }

    public int vertexCount() {
        if (model == null) return 0;
        return model.getVertexCount(culling, options);
    }

    public Vertex[] getVertices() {
        if (model == null) return new Vertex[0];
        ModelVertex[] modelVertices = model.getModelVertices(culling, options);
        Vertex[] vertices = new Vertex[modelVertices.length];

        for (int i = 0; i < modelVertices.length; i++) {
            ModelVertex modelVertex = modelVertices[i];
            vertices[i] = new Vertex(modelVertex, pos, model);
        }

        return vertices;
    }


    public Vector3[] getEdgeVertices() {
        HitBox hitBox = this.getHitbox();
        if (hitBox == null) return new Vector3[0];
        return hitBox.getEdgeVertices();
    }

    public void tick() {
        shouldUpdate = false;
    }

    /**
     * Handles left-click
     */
    public void primaryInteraction() {
        destroy();
    }

    /**
     * Handles right-click
     * @return True if a block should be placed normally.
     */
    public boolean secondaryInteraction(Player p) {
        return true;
    }

    /**
     * Called by <i>Game_IO</i> class after placing this block.
     * @param conPos The world position the click creating this block hit the others block hitbox.
     */
    public void updateOptionsWithPlacePos(Vector3 conPos) {

    }

    public void destroy() { chunk.removeBlock(this);}

    public byte store() {
        return type.store_value;
    }

    public enum Type {
        GRASS (1, Grass.class),
        STONE (2, Stone.class),
        PLANKS(3, Planks.class),
        DIRT (4, Dirt.class),
        WOOD (5, Log.class),
        LEAVES(6, Leaves.class),
        SAND (7, Sand.class),
        __TREE_SPAWNER (8, TreeSpawner.class),
        WATER (9, Water.class),
        BEDROCK (10, Bedrock.class),
        GRAVEL(11, Gravel.class),
        PLANKS_SLAB(12, PlanksSlab.class),
        TORCH(13, Torch.class),
        FENCE(14, Fence.class),
        GLASS(15, Glass.class),
        TEST_TYPE(250, TestBlock.class);

        private final byte store_value;

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

    public boolean shouldCullFace(CuboidFace.Face face) {
        for (CuboidModel cubModel: model.getCuboidModels()) {
            if (!cubModel.shouldBeRendered(this.options)) continue;
            Map<CuboidFace.Face, CuboidFace> faceMap = cubModel.getFacesMap();
            if (!faceMap.containsKey(face)) continue;
            if (faceMap.get(face).culling) return true;
        }
        return false;
    }

    public void setVisibleTop(boolean visibleTop) {
        this.culling.top = visibleTop;
    }
    public void setVisibleBottom(boolean visibleBottom) {
        this.culling.bottom = visibleBottom;
    }
    public void setVisibleLeft(boolean visibleLeft) {
        this.culling.left = visibleLeft;
    }
    public void setVisibleRight(boolean visibleRight) {
        this.culling.right = visibleRight;
    }
    public void setVisibleBack(boolean visibleBack) {
        this.culling.back = visibleBack;
    }
    public void setVisibleFront(boolean visibleFront) {
        this.culling.front = visibleFront;
    }

    public void setOptions(ModelOptions options) {
        this.options = options;
    }

    public boolean isVisibleTop() {
        return culling.top;
    }
    public boolean isVisibleBottom() {
        return culling.bottom;
    }
    public boolean isVisibleLeft() {
        return culling.left;
    }
    public boolean isVisibleRight() {
        return culling.right;
    }
    public boolean isVisibleBack() {
        return culling.back;
    }
    public boolean isVisibleFront() {
        return culling.front;
    }

    public int getXPos() {return (int) this.pos.x;}
    public int getYPos() {return (int) this.pos.y;}
    public int getZPos() {return (int) this.pos.z;}


    public boolean technicalBlock() {
        return this.type == Type.__TREE_SPAWNER;
    }

    @Override
    public HitBox getHitbox() {
        if (model == null) return null;
        //return new HitBox(new Vector3(0f,0f,0f), new Vector3(1f,0.5f,1f), this);
        return model.getHitBoxModel(options).linkedWith(this);
    }
}
