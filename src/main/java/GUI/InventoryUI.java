package GUI;

import Entities.Block;
import GL_Math.Vector2;
import Player.Inventory;

public class InventoryUI extends UIBasicTexturedComponent implements Interactable{

    private static Vector2 UV_ORIGIN = new Vector2(0,0);
    private static Vector2 UV_SIZE = new Vector2(195,136);

    private static float BLOCK_SIZE = 0.23f;

    private GUIDrawer drawer;
    private final Inventory inventory;

    public InventoryUI(Vector2 pos, float width, boolean center, Inventory inventory, GUIDrawer drawer) {
        super(pos, width, center, UV_ORIGIN, UV_SIZE);
        this.drawer = drawer;
        this.inventory = inventory;
        this.textureDescriptor = new GUITextureDescriptor("/textures/gui/container/creative_inventory/tab_items.png", false);
    }

    public void render() {
        if (!isVisible()) return;

        ItemBarBlock itemBarBlock = new ItemBarBlock(BLOCK_SIZE, drawer);
        itemBarBlock.setUp();

        for (int d = 0; d < inventory.inventoryBlocks.size(); d++) {
            Block b = inventory.inventoryBlocks.get(d);
            if (b == null) return;
            int i = d % 9;
            int j = d / 9;

            itemBarBlock.render(b,posForLocation(i, j));
        }

    }
    private Vector2 posForLocation(int i, int j) {
        return this.pos.added(new Vector2(i * this.size.x * 18f/195 + 10f/195,- j * this.size.y * 18f/136 - 20f/136));
    }

    @Override
    public void click(Vector2 pos) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                Vector2 subPos = posForLocation(i,j);
                Vector2 size = new Vector2(BLOCK_SIZE,BLOCK_SIZE);
                if (GUIMouseControl.isInside(pos, subPos, size)) {

                    select(i,j);
                    return;
                }
            }
        }
    }

    @Override
    public void setHover(boolean hov) {

    }

    @Override
    public GUIRectangle getRect() {
        return new GUIRectangle(this.pos, this.size);
    }

    @Override
    public boolean currentlyInteractable() {
        return isVisible();
    }

    private void select(int i, int j) {
        int ind = i + j * 9;
        try {
            Block selectedBlock = inventory.inventoryBlocks.get(ind);
            if (selectedBlock != null){
                //Shifts the current bar blocks right by one
                System.arraycopy(inventory.barBlocks, 0, inventory.barBlocks, 1, inventory.barBlocks.length - 1);
                inventory.barBlocks[0] = selectedBlock;
            }
        } catch (IndexOutOfBoundsException ignored) { }

    }
}
