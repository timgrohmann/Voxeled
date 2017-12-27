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
import Models.GUITexturedVertex;
import Models.GUIVertex;
import Models.Vertex;
import Shader.GUI2DShaderProgram;
import Shader.GUISelectionShaderProgram;
import Shader.GUITextured2DShaderProgram;
import Shader.WorldShaderProgram;
import Textures.Texture;

import java.util.ArrayList;
import java.util.Arrays;
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
    }

    public void renderStaticGUI(Matrix4 matrix4) {
        if (menuShown) {
            GUIButton closeButton = new GUIButton(new Vector2(0,0), 1.6f,"Spiel beenden?");
            renderComponent(closeButton);
        } else {
            renderCrosshair(matrix4);
            renderComponent(itemBar);
            itemBarSelector.setScrollState(renderer.gameIO.selectedSlot);
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

        renderer.setDepthTest(false);

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
        inventoryBlockShaderProgram.setUniformMatrix("mat", Matrix4.guiMatrix(1,renderer.getWindow().getAspectRatio()));
        inventoryBlockShaderProgram.setUniformVector("light_dir", new Vector3(0,-5,-5));

        Block[] inventory = renderer.player.inventory();
        ArrayList<Vertex> displayBlockVertices = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (inventory[i] == null) continue;
            displayBlockVertices.addAll(Arrays.asList(blockDisplayQuad(i,inventory[i])));
        }

        Vertex[] vertices1 = new Vertex[displayBlockVertices.size()];
        vertices1 = displayBlockVertices.toArray(vertices1);
        inventroyBlockABO.load(vertices1);
        inventroyBlockABO.render();

        renderer.setDepthTest(true);
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

        Vertex[] vertices = highlightedBlock.getEdgeVertices();
        if (vertices == null || vertices.length == 0) return;
        selectionShaderProgram.use();


        arrayBuffer3D.bind();
        arrayBuffer3D.load(vertices);
        selectionShaderProgram.setUniformMatrix("matGUI", mat);
        arrayBuffer3D.render();
    }

    private static Vertex[] blockDisplayQuad(int slot, Block block) {
        Texture topTexture = block.getTopTexture();
        Texture sideTexture = block.getSideTexture();
        float h = 0.10f;
        float xMid = -0.8f + 0.2f * slot;
        float yMid = -0.85f;
        float inCircleRad = 0.866f * h / 2;

        //top side
        Vertex top = new Vertex(new Vector3(xMid,yMid + h / 2,0), 0, 0, topTexture);
        Vertex right = new Vertex(new Vector3(xMid + inCircleRad,yMid + h / 4,0), 0, 1, topTexture);
        Vertex bottom = new Vertex(new Vector3(xMid,yMid,0), 1, 1, topTexture);
        Vertex left = new Vertex(new Vector3(xMid - inCircleRad,yMid + h / 4,0), 1, 0, topTexture);


        //right side
        Vertex rightTop = new Vertex(new Vector3(xMid + inCircleRad,yMid + h / 4,0), 0, 0, sideTexture);
        Vertex rightBottom = new Vertex(new Vector3(xMid + inCircleRad,yMid - h / 4,0), 0, 1, sideTexture);
        Vertex rightMidBottom = new Vertex(new Vector3(xMid,yMid - h / 2,0), 1, 1, sideTexture);
        Vertex rightCenter = new Vertex(new Vector3(xMid,yMid,0), 1, 0, sideTexture);

        //left side
        Vertex leftTop = new Vertex(new Vector3(xMid - inCircleRad,yMid + h / 4,0), 1, 0, sideTexture);
        Vertex leftBottom = new Vertex(new Vector3(xMid - inCircleRad,yMid - h / 4,0), 1, 1, sideTexture);
        Vertex leftMidBottom = new Vertex(new Vector3(xMid,yMid - h / 2,0), 0, 1, sideTexture);
        Vertex leftCenter = new Vertex(new Vector3(xMid,yMid,0), 0, 0, sideTexture);

        return new Vertex[]{
                top,right,bottom,bottom,left,top,
                rightTop,rightBottom,rightMidBottom,rightMidBottom,rightCenter,rightTop,
                leftTop,leftBottom,leftMidBottom,leftMidBottom,leftCenter,leftTop
        };
    }
}
