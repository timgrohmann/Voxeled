package GUI;

import Buffers.GUILineABO;
import Buffers.GUISelectionABO;
import Buffers.Triangle2DABO;
import Entities.Block;
import GL_Math.Matrix4;
import GL_Math.Vector2;
import GL_Math.Vector3;
import Main_Package.Renderer;
import Models.GUIVertex;
import Shader.GUI2DShaderProgram;
import Shader.GUISelectionShaderProgram;
import Shader.GUITextured2DShaderProgram;
import Shader.WorldShaderProgram;

import java.util.*;

public class GUIDrawer {
    final Renderer renderer;

    private GUI2DShaderProgram gui2DShaderProgram;
    private GUISelectionShaderProgram selectionShaderProgram;
    GUITextured2DShaderProgram guiTextured2DShaderProgram;
    private WorldShaderProgram inventoryBlockShaderProgram;

    private GUILineABO arrayBuffer;
    private GUISelectionABO arrayBuffer3D;
    Triangle2DABO mainGuiBuffer;

    private ItemBar itemBar;
    private ItemBarSelector itemBarSelector;
    private ItemBarBlock itemBarBlock;
    private GUIButton closeButton;
    private InventoryUI inventoryUI;

    public boolean menuShown = false;
    public boolean inventoryShown = false;

    private Block highlightedBlock;

    private final Map<String, GUITexture> textureMap = new HashMap<>();

    public static GUITextureDescriptor WIDGET_TEXTURE = new GUITextureDescriptor("textures/default/textures/gui/widgets.png", false);
    public static GUITextureDescriptor TEXT_TEXTURE = new GUITextureDescriptor("src/main/resources/text.png", true);

    public GUIMouseControl mouseControl;

    public GUIDrawer(Renderer renderer) {
        this.renderer = renderer;
        try {
            this.gui2DShaderProgram = new GUI2DShaderProgram();
            this.selectionShaderProgram = new GUISelectionShaderProgram();
            this.guiTextured2DShaderProgram = new GUITextured2DShaderProgram();
            this.inventoryBlockShaderProgram = new WorldShaderProgram();
        }catch (Exception e) {
            e.printStackTrace();
        }

        load();
    }

    private void load() {
        arrayBuffer = new GUILineABO(this.gui2DShaderProgram);
        GUIVertex[] vertices = getVertices();
        arrayBuffer.load(vertices);
        arrayBuffer3D = new GUISelectionABO(selectionShaderProgram);

        mainGuiBuffer = new Triangle2DABO(this.guiTextured2DShaderProgram);
        //inventroyBlockABO = new BlockABO(this.inventoryBlockShaderProgram);

        itemBar = new ItemBar(new Vector2(0, -0.85f), 1.8f, true);
        itemBarSelector = new ItemBarSelector(new Vector2(-0.8f,-0.85f),0.2f,true);

        itemBarBlock = new ItemBarBlock(0.2f, this);
        //itemBarFBO = new ItemBarFBO(renderer.getWindow());
        mouseControl = new GUIMouseControl(this);
        inventoryUI = new InventoryUI(new Vector2(0,0), 2, true,renderer.player.inventory, this);
        closeButton = new GUIButton(new Vector2(0,0), 1.6f,"Spiel beenden?");

        closeButton.setExecuter(renderer.getWindow()::close);

        mouseControl.registerComponent(inventoryUI);
        mouseControl.registerComponent(closeButton);
    }

    public void renderStaticGUI(Matrix4 matrix4) {

        boolean itemBarShown = true;

        closeButton.setVisible(menuShown);
        renderButton(closeButton);

        if (!menuShown) renderCrosshair(matrix4);


        inventoryUI.setVisible(inventoryShown);
        render(inventoryUI);
        inventoryUI.render();

        //Debug info
        GUIText chunkText = new GUIText(String.format("Chunks loaded: %d", renderer.world.chunks.size()),
                new Vector2(-renderer.getWindow().getAspectRatio() + 0.01f, 0.99f),0.04f, false);
        GUIText coordText = new GUIText(String.format("x: %.2f y: %.2f, z: %.2f", renderer.player.getPos().x, renderer.player.getPos().y, renderer.player.getPos().z),
                new Vector2(-renderer.getWindow().getAspectRatio() + 0.01f, 0.99f - 0.05f),0.04f, false);

        render(chunkText);
        render(coordText);


        //Render inventory bar
        itemBar.setVisible(itemBarShown);
        render(itemBar);

        //Render blocks in inventory
        if (itemBarShown) {
            itemBarSelector.setScrollState(renderer.player.selectedSlot);
            render(itemBarSelector);
            itemBarBlock.setUp();

            Block[] inv = renderer.player.inventory.barBlocks;
            for (int i = 0; i < 9; i++) {
                Block invBlock = inv[i];
                if (invBlock == null) continue;
                itemBarBlock.render(invBlock, itemBar.positionForSlot(i));
            }
        }

        Renderer.setFaceCulling(true);
        Renderer.setDepthTest(true);
    }

    private void mainRenderSetup() {
        Renderer.setDepthTest(false);
        Renderer.setFaceCulling(false);
        guiTextured2DShaderProgram.use();
        guiTextured2DShaderProgram.setUniformFloat("aspect", renderer.getWindow().getAspectRatio());
        guiTextured2DShaderProgram.setUniformInt("texture_diffuse", 1);
    }

    private void render(UIComponent component) {
        if (!component.isVisible()) return;

        mainRenderSetup();
        GUITexture tex = textureMap.computeIfAbsent(component.textureDescriptor.texturePath,
                k -> new GUITexture(1).load(component.textureDescriptor.texturePath, component.textureDescriptor.interpolate));
        tex.bind();
        mainGuiBuffer.load(component.getVertices());
        mainGuiBuffer.render();
    }

    private void renderButton(GUIButton component) {
        render(component);
        render(component.getText());
    }

    private void renderCrosshair(Matrix4 matrix4) {
        gui2DShaderProgram.use();
        gui2DShaderProgram.setUniformMatrix("mat", matrix4);

        arrayBuffer.bind();
        arrayBuffer.render();
    }

    public Matrix4 guiMatrix() {
        return Matrix4.guiMatrix(0.05f, renderer.getWindow().getAspectRatio());
    }

    private GUIVertex[] getVertices() {
        return new GUIVertex[]{
                new GUIVertex(-1,0), new GUIVertex(1,0),
                new GUIVertex(0,1), new GUIVertex(0,-1),
        };
    }

    public void highlightBlock(Block selected) {
        highlightedBlock = selected;
    }

    public void render3D(Matrix4 mat) {
        if (highlightedBlock == null) return;

        Vector3[] vertices = highlightedBlock.getEdgeVertices();
        if (vertices == null || vertices.length == 0) return;
        selectionShaderProgram.use();


        arrayBuffer3D.bind();
        arrayBuffer3D.load(vertices);
        selectionShaderProgram.setUniformMatrix("matGUI", mat);
        arrayBuffer3D.render();
    }
}
