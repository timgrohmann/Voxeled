package Player;

import Entities.Block;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    public List<Block> inventoryBlocks;
    public Block[] barBlocks;

    private Player player;

    Inventory(Player player) {
        this.player = player;

        inventoryBlocks = Arrays.asList(player.renderer.registry.allSingletons());
        inventoryBlocks = inventoryBlocks.stream().filter(k -> !k.technicalBlock()).collect(Collectors.toList());

        barBlocks = new Block[9];
        for (int i = 0; i < barBlocks.length; i++) {
            barBlocks[i] = inventoryBlocks.get(i);
        }
    }


}
