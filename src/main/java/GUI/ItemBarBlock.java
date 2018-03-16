package GUI;

import Buffers.BlockABO;
import Buffers.Triangle2DABO;
import Entities.Block;
import GL_Math.Matrix4;
import GL_Math.Vector2;
import GL_Math.Vector3;
import Main_Package.Renderer;
import Shader.GUITextured2DShaderProgram;
import Shader.WorldShaderProgram;

class ItemBarBlock extends UIBasicTexturedComponent {

    private static Vector2 UV_ORIGIN = new Vector2(0,0);
    private static Vector2 UV_SIZE = new Vector2(256,256);

    private final Vector2 initialPos;

    private final WorldShaderProgram blockShader;
    private BlockABO inventoryBlockABO;
    private final ItemBarFBO itemBarFBO;
    private final Renderer r;
    private final GUITextured2DShaderProgram guiShader;
    private final Triangle2DABO guiABO;
    private final Matrix4 itemBarBlockProjection;

    public ItemBarBlock(float width, GUIDrawer guiDrawer) {
        super(Vector2.zero(), width, false, UV_ORIGIN, UV_SIZE);
        initialPos = pos.copy();

        this.r = guiDrawer.renderer;
        this.blockShader = r.worldShader;
        this.guiShader = guiDrawer.guiTextured2DShaderProgram;
        this.guiABO = guiDrawer.mainGuiBuffer;
        itemBarFBO = new ItemBarFBO(r.getWindow());


        itemBarBlockProjection = Matrix4.translationMatrix(new Vector3(-0.5f,-0.5f,-0.5f));
        itemBarBlockProjection.apply(Matrix4.scaleMatrix(1,-1,1));
        itemBarBlockProjection.apply(Matrix4.rotationMatrixY((float) Math.PI / 180 * 20));
        itemBarBlockProjection.apply(Matrix4.rotationMatrixX((float) Math.PI / 8));
        itemBarBlockProjection.apply(Matrix4.translationMatrix(new Vector3(0,0,-4f)));
        itemBarBlockProjection.apply(Matrix4.projectionMatrix(0.3f,1,0.1f,10));
    }

    void setScrollState(int slot /*0-8*/) {
        pos.x = initialPos.x + slot * size.x;
        generateVertices();
    }

    public void render(Block b, Vector2 pos) {
        this.pos.x = pos.x;
        this.pos.y = pos.y;
        generateVertices();


        blockShader.use();
        inventoryBlockABO.load(b.getVertices());
        r.activateBlockTextures();
        itemBarFBO.bind();
        Renderer.setDepthTest(true);
        inventoryBlockABO.render();
        itemBarFBO.unbind();


        guiShader.use();

        guiABO.load(this.getVertices());

        itemBarFBO.activateTexture();
        Renderer.setDepthTest(false);
        guiABO.render();
    }

    public void setUp() {
        if (inventoryBlockABO == null) inventoryBlockABO = this.r.world.entityABO;

        blockShader.use();
        blockShader.setUniformMatrix("mat", itemBarBlockProjection);
        blockShader.setUniformVector("light_dir", new Vector3(1,3,2));

        guiShader.use();
        guiShader.setUniformFloat("aspect", r.getWindow().getAspectRatio());
        guiShader.setUniformInt("texture_diffuse", 0);
    }
}
