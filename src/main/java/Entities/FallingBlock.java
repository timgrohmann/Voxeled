package Entities;

import GL_Math.Vector3;

public class FallingBlock extends Block {
    public Block blockClass;

    public FallingBlock(Block blockClass) {
        super(blockClass.getPos(), blockClass.type, blockClass.chunk);
        this.blockClass = blockClass;
    }

    @Override
    public void registerTextures() {
        loadModel(blockClass.model);
        transparent = blockClass.transparent;
    }

    @Override
    public void update() {
        super.update();
        this.speed.y -= 0.008f;
        this.speed.y *= 0.95f;
        this.pos.add(speed);

        Block below = chunk.world.getBlockForCoordinates(this.pos);
        if (below != null && below.getHitbox() != null) {
            chunk.world.entities.remove(this);
            chunk.world.setBlockForCoordinates(blockClass.type, pos.added(new Vector3(0,1,0)));
        }
    }
}
