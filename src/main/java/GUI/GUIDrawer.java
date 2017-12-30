package GUI;

import Buffers.BlockABO;
import Buffers.GUILineABO;
import Buffers.GUISelectionABO;
import Buffers.Triangle2DABO;
import Entities.Block;
import GL_Math.Matrix4;
import GL_Math.Vector2;
import GL_Math.Vector3;
import Main_Package.Renderer;
import Models.CuboidFace;
import Models.GUITexturedVertex;
import Models.GUIVertex;
import Models.Vertex;
import Shader.GUI2DShaderProgram;
import Shader.GUISelectionShaderProgram;
import Shader.GUITextured2DShaderProgram;
import Shader.WorldShaderProgram;
import Textures.Texture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GUIDrawer {
    private final Renderer renderer;

    private GUI2DShaderProgram gui2DShaderProgram;
    private GUISelectionShaderProgram selectionShaderProgram;
    private GUITextured2DShaderProgram guiTextured2DShaderProgram;
    private WorldShaderProgram inventoryBlockShaderProgram;

    private GUILineABO arrayBuffer;
    private GUISelectionABO arrayBuffer3D;
    private Triangle2DABO mainGuiBuffer;
    private BlockABO inventroyBlockABO;

    private ItemBar itemBar;
    private ItemBarSelector itemBarSelector;

    private ItemBarBlock itemBarBlock;
    private ItemBarFBO itemBarFBO;
    private Matrix4 itemBarBlockProjection;

    public boolean menuShown = false;

    private Block highlightedBlock;

    private final GUITexture guiTexture;

    private List<GUITexturedVertex> mainVertices = new ArrayList<>();
    private List<GUITexturedVertex> textVertices = new ArrayList<>();

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

        guiTexture = new GUITexture();
        guiTexture.load();
        load();
    }

    private void load() {
        arrayBuffer = new GUILineABO(this.gui2DShaderProgram);
        GUIVertex[] vertices = getVertices();
        arrayBuffer.load(vertices);
        arrayBuffer3D = new GUISelectionABO(selectionShaderProgram);

        mainGuiBuffer = new Triangle2DABO(this.guiTextured2DShaderProgram);
        inventroyBlockABO = new BlockABO(this.inventoryBlockShaderProgram);

        itemBar = new ItemBar(new Vector2(0, -0.85f), 1.8f, true);
        itemBarSelector = new ItemBarSelector(new Vector2(-0.8f,-0.85f),0.2f,true);

        itemBarBlock = new ItemBarBlock(new Vector2(-0.8f,-0.85f),0.2f,true);
        itemBarFBO = new ItemBarFBO(renderer.getWindow());

        itemBarBlockProjection = Matrix4.translationMatrix(new Vector3(-0.5f,-0.5f,-0.5f));
        itemBarBlockProjection.apply(Matrix4.scaleMatrix(1,-1,1));
        itemBarBlockProjection.apply(Matrix4.rotationMatrixY((float) Math.PI / 180 * 20));
        //mat.apply(Matrix4.rotationMatrixY(a));
        //a+=0.01;
        itemBarBlockProjection.apply(Matrix4.rotationMatrixX((float) Math.PI / 8));
        itemBarBlockProjection.apply(Matrix4.translationMatrix(new Vector3(0,0,-4f)));
        itemBarBlockProjection.apply(Matrix4.projectionMatrix(0.3f,1,0.1f,10));
    }

    private float a = 0;
    public void renderStaticGUI(Matrix4 matrix4) {
        if (menuShown) {
            GUIButton closeButton = new GUIButton(new Vector2(0,0), 1.6f,"Spiel beenden?");
            renderComponent(closeButton);
        } else {
            renderCrosshair(matrix4);
            renderComponent(itemBar);
            itemBarSelector.setScrollState(renderer.player.selectedSlot);
            renderComponent(itemBarSelector);
        }

        //Debug info
        GUIText chunkText = new GUIText(String.format("Chunks loaded: %d", renderer.world.chunks.size()),
                new Vector2(-renderer.getWindow().getAspectRatio() + 0.01f, 0.99f),0.04f, false);
        GUIText coordText = new GUIText(String.format("x: %.2f y: %.2f, z: %.2f", renderer.player.getPos().x, renderer.player.getPos().y, renderer.player.getPos().z),
                new Vector2(-renderer.getWindow().getAspectRatio() + 0.01f, 0.99f - 0.05f),0.04f, false);

        renderComponent(chunkText);
        renderComponent(coordText);

        //Actual render pass

        guiTextured2DShaderProgram.use();
        guiTextured2DShaderProgram.setUniformFloat("aspect", renderer.getWindow().getAspectRatio());
        guiTextured2DShaderProgram.setUniformInt("texture_diffuse", 1);

        Renderer.setDepthTest(false);
        Renderer.setFaceCulling(false);

        mainGuiBuffer.load(mainVertices);
        guiTexture.activateMainTextures();
        mainGuiBuffer.render();

        mainGuiBuffer.load(textVertices);
        guiTexture.activateTextTextures();
        mainGuiBuffer.render();

        mainVertices.clear();
        textVertices.clear();



        //Render blocks in inventory

        inventoryBlockShaderProgram.use();
        inventoryBlockShaderProgram.setUniformMatrix("mat", itemBarBlockProjection);
        inventoryBlockShaderProgram.setUniformVector("light_dir", new Vector3(1,3,2));

        guiTextured2DShaderProgram.use();
        guiTextured2DShaderProgram.setUniformFloat("aspect", renderer.getWindow().getAspectRatio());
        guiTextured2DShaderProgram.setUniformInt("texture_diffuse", 0);

        Block[] inv = renderer.player.inventory();
        for (int i = 0; i < 9; i++) {
            Block invBlock = inv[i];
            if (invBlock == null) continue;
            inventoryBlockShaderProgram.use();

            inventroyBlockABO.load(invBlock.getVertices());
            renderer.world.blockTextures.activateTextures();
            itemBarFBO.bind();
            Renderer.setDepthTest(true);
            inventroyBlockABO.render();
            itemBarFBO.unbind();


            guiTextured2DShaderProgram.use();
            itemBarBlock.setScrollState(i);
            mainGuiBuffer.load(itemBarBlock.getVertices());
            itemBarFBO.activateTexture();

            Renderer.setDepthTest(false);
            mainGuiBuffer.render();
        }

        Renderer.setFaceCulling(true);
        Renderer.setDepthTest(true);
    }

    private void renderComponent(UIBasicTexturedComponent component) {
        mainVertices.addAll(component.getVerticesList());
    }

    private void renderComponent(GUIButton button) {
        mainVertices.addAll(button.getVerticesList());
        textVertices.addAll(button.getText().getVerticesList());
    }

    private void renderComponent(GUIText text) {
        textVertices.addAll(text.getVerticesList());
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
