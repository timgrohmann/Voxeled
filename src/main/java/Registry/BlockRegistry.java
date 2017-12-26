package Registry;

import Entities.Block;
import Textures.Texture;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {
    private Map<Block.Type, Block> blockReferences= new HashMap<>();

    public Block getBlockSingletonForType(Block.Type type) {
        Block ref = blockReferences.get(type);
        if (ref == null) {
            Block newRef = type.createSingletonInstance();
            blockReferences.put(type, newRef);
            return newRef;
        } else {
            return ref;
        }
    }

    public Block[] allSingletons() {
        Block.Type[] allTypes = Block.Type.values();
        Block[] blocks = new Block[allTypes.length];

        for (int i = 0; i < allTypes.length; i++) {
            blocks[i] = getBlockSingletonForType(allTypes[i]);
        }

        return blocks;
    }
}
