package GUI;

import Buffers.GUILineABO;
import Buffers.GUISelectionABO;
import Buffers.Triangle2DABO;
import Entities.Block;
import GL_Math.Matrix4;
import Main_Package.Renderer;
import Models.GUITexturedVertex;
import Models.GUIVertex;
import Models.Vertex;
import Shader.GUI2DShaderProgram;
import Shader.GUISelectionShaderProgram;
import Shader.GUITextured2DShaderProgram;
import Textures.Texture;

import java.util.ArrayList;
import java.util.Arrays;

public class GUIDrawer {
    private final Renderer renderer;

    private GUI2DShaderProgram gui2DShaderProgram;
    private GUISelectionShaderProgram selectionShaderProgram;
    private GUITextured2DShaderProgram guiTextured2DShaderProgram;

    private GUILineABO arrayBuffer;
    private GUISelectionABO arrayBuffer3D;
    private Triangle2DABO mainGuiBuffer;

    public boolean menuShown = false;

    private Block highlightedBlock;

    private final GUITexture guiTexture;


    public GUIDrawer(Renderer renderer) {
        this.renderer = renderer;
        try {
            this.gui2DShaderProgram = new GUI2DShaderProgram();
            this.selectionShaderProgram = new GUISelectionShaderProgram();
            this.guiTextured2DShaderProgram = new GUITextured2DShaderProgram();
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
    }

    public void renderStaticGUI(Matrix4 matrix4) {

        if (!menuShown) {
            //Cross
            gui2DShaderProgram.use();
            arrayBuffer.bind();
            gui2DShaderProgram.setUniformMatrix("mat", matrix4);
            arrayBuffer.render();
        }

        guiTexture.activateMainTextures();
        guiTextured2DShaderProgram.use();
        mainGuiBuffer.bind();
        guiTextured2DShaderProgram.setUniformFloat("aspect", renderer.getWindow().getAspectRatio());
        guiTextured2DShaderProgram.setUniformInt("texture_diffuse", 1);

        GUIButton closeButton = new GUIButton(0,0,"Spiel beenden?");

        ArrayList<GUITexturedVertex> vertices = new ArrayList<>();
        vertices.addAll(Arrays.asList(itemBarQuad()));
        vertices.addAll(Arrays.asList(selectionQuad(renderer.gameIO.selectedSlot)));
        if (menuShown) {
            vertices.addAll(closeButton.buttonVertices);
        }


        renderer.setDepthTest(false);

        mainGuiBuffer.load(vertices);
        mainGuiBuffer.render();

        if (menuShown) {
            mainGuiBuffer.load(closeButton.getTextVertices());
            guiTexture.activateTextTextures();
            mainGuiBuffer.render();
        }

        GUIText chunkText = new GUIText("Chunks loaded: " + String.valueOf(renderer.chunkLoadedCount()), -renderer.getWindow().getAspectRatio() + 0.01f, 0.99f,0.04f, false);
        GUIText coordText = new GUIText(String.format("x: %.2f y: %.2f, z: %.2f", renderer.player.getPos().x, renderer.player.getPos().y, renderer.player.getPos().z), -renderer.getWindow().getAspectRatio() + 0.01f, 0.99f - 0.05f,0.04f, false);

        GUITexturedVertex[] textVertices = new GUITexturedVertex[chunkText.getVertices().length + coordText.getVertices().length];
        System.arraycopy(chunkText.getVertices(),0,textVertices,0,chunkText.getVertices().length);
        System.arraycopy(coordText.getVertices(),0,textVertices,chunkText.getVertices().length,coordText.getVertices().length);

        mainGuiBuffer.load(textVertices);
        guiTexture.activateTextTextures();
        mainGuiBuffer.render();


        //Switch back to texture slot 0
        guiTextured2DShaderProgram.setUniformInt("texture_diffuse", 0);

        Block[] inventory = renderer.player.inventory();
        ArrayList<GUITexturedVertex> displayBlockVertices = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (inventory[i] == null) continue;
            displayBlockVertices.addAll(Arrays.asList(blockDisplayQuad(i,inventory[i])));
        }

        mainGuiBuffer.load(displayBlockVertices);
        mainGuiBuffer.render();

        renderer.setDepthTest(true);
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

    private static GUITexturedVertex[] selectionQuad(int selectionIndex) {
        float start = -0.9f + 0.2f * selectionIndex;
        return texQuadAspectAndPixel(start,-0.75f,0.2f,0,24,22,46);
    }

    private static GUITexturedVertex[] itemBarQuad() {
        return texQuadAspectAndPixel(-0.9f,-0.75f,1.8f,0,182,1,21);
    }

    private static GUITexturedVertex[] blockDisplayQuad(int slot, Block block) {
        Texture topTexture = block.getTopTexture();
        Texture sideTexture = block.getSideTexture();
        float h = 0.10f;
        float xMid = -0.8f + 0.2f * slot;
        float yMid = -0.85f;
        float inCircleRad = 0.866f * h / 2;

        //top side
        GUITexturedVertex top = new GUITexturedVertex(xMid,yMid + h / 2, topTexture.convertedU(0), topTexture.convertedV(0));
        GUITexturedVertex right = new GUITexturedVertex(xMid + inCircleRad,yMid + h / 4, topTexture.convertedU(0), topTexture.convertedV(1));
        GUITexturedVertex bottom = new GUITexturedVertex(xMid,yMid, topTexture.convertedU(1), topTexture.convertedV(1));
        GUITexturedVertex left = new GUITexturedVertex(xMid - inCircleRad,yMid + h / 4, topTexture.convertedU(1), topTexture.convertedV(0));


        //right side
        GUITexturedVertex rightTop = new GUITexturedVertex(xMid + inCircleRad,yMid + h / 4, sideTexture.convertedU(0), sideTexture.convertedV(0));
        GUITexturedVertex rightBottom = new GUITexturedVertex(xMid + inCircleRad,yMid - h / 4, sideTexture.convertedU(0), sideTexture.convertedV(1));
        GUITexturedVertex rightMidBottom = new GUITexturedVertex(xMid,yMid - h / 2, sideTexture.convertedU(1), sideTexture.convertedV(1));
        GUITexturedVertex rightCenter = new GUITexturedVertex(xMid,yMid, sideTexture.convertedU(1), sideTexture.convertedV(0));

        //left side
        GUITexturedVertex leftTop = new GUITexturedVertex(xMid - inCircleRad,yMid + h / 4, sideTexture.convertedU(1), sideTexture.convertedV(0));
        GUITexturedVertex leftBottom = new GUITexturedVertex(xMid - inCircleRad,yMid - h / 4, sideTexture.convertedU(1), sideTexture.convertedV(1));
        GUITexturedVertex leftMidBottom = new GUITexturedVertex(xMid,yMid - h / 2, sideTexture.convertedU(0), sideTexture.convertedV(1));
        GUITexturedVertex leftCenter = new GUITexturedVertex(xMid,yMid, sideTexture.convertedU(0), sideTexture.convertedV(0));

        return new GUITexturedVertex[]{
                top,right,bottom,bottom,left,top,
                rightTop,rightBottom,rightMidBottom,rightMidBottom,rightCenter,rightTop,
                leftTop,leftBottom,leftMidBottom,leftMidBottom,leftCenter,leftTop
        };
    }

    static GUITexturedVertex[] texQuadAspectAndPixelCenter(float midX, float midY, float xWidth, float pixUStart, float pixUEnd, float pixVStart, float pixVEnd) {
        float minX = midX - xWidth / 2;
        float height = (pixVEnd - pixVStart) / (pixUEnd - pixUStart) * xWidth;
        float minY = midY + height / 2;

        return texQuadAspectAndPixel(minX,minY,xWidth,pixUStart,pixUEnd,pixVStart,pixVEnd);
    }

    private static GUITexturedVertex[] texQuadAspectAndPixel(float minX, float minY, float xWidth, float pixUStart, float pixUEnd, float pixVStart, float pixVEnd) {
        int texW = 256;
        float maxX = minX + xWidth;
        float height = (pixVEnd - pixVStart) / (pixUEnd - pixUStart) * xWidth;
        float maxY = minY - height;
        float minU = pixUStart / texW;
        float minV = pixVStart / texW;
        float maxU = pixUEnd / texW;
        float maxV = pixVEnd / texW;

        return texQuad(minX,minY,minU,minV,maxX,maxY,maxU,maxV);
    }

    static GUITexturedVertex[] texQuad(float minX, float minY, float minU, float minV,
                                       float maxX, float maxY, float maxU, float maxV) {
        return new GUITexturedVertex[]{
                new GUITexturedVertex(minX,minY,minU,minV), new GUITexturedVertex(maxX,minY,maxU,minV), new GUITexturedVertex(minX,maxY,minU,maxV),
                new GUITexturedVertex(maxX,minY,maxU,minV), new GUITexturedVertex(maxX,maxY,maxU,maxV), new GUITexturedVertex(minX,maxY,minU,maxV),
        };

    }
}
