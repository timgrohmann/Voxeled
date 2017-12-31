package Blocks;

import Entities.Block;
import GL_Math.Vector3;
import Player.Player;
import Models.Vertex;
import World.Chunk;

public class PlanksSlab extends Block {
    public PlanksSlab(Vector3 pos, Chunk chunk) {
        super(pos, Type.PLANKS_SLAB, chunk);
        this.options.put("half", "bottom");
    }

    @Override
    public void registerTextures() {
        loadModel("blocks/planks_slab");
    }

    @Override
    public void updateOptionsWithPlacePos(Vector3 conPos) {
        super.updateOptionsWithPlacePos(conPos);

        float y = conPos.y;
        double d = y - Math.floor(y);

        if (d > 0.5 && d < 0.95 || d < 0.05) {
            this.options.put("half", "top");
        } else {
            this.options.put("half", "bottom");
        }
    }

    @Override
    public boolean secondaryInteraction(Player p) {
        if (p.getSelectedBlock() instanceof  PlanksSlab) {
            chunk.world.setBlockForCoordinates(Type.PLANKS, this.pos);
            return false;
        }
        return true;
    }

}