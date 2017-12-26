package Main_Package;

import Entities.Entity;
import GL_Math.Matrix4;
import GL_Math.Vector3;
import GUI.GUIDrawer;
import Registry.BlockRegistry;
import Shader.WaterShaderProgram;
import Shader.WorldShaderProgram;
import World.World;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;
import static org.lwjgl.opengl.GL20.*;

public class Renderer {

    private final GL_Window window;

    private final ArrayList<Entity> entities;

    public final World world;

    public WorldShaderProgram worldShader;
    public WaterShaderProgram waterShader;
    public final Game_IO gameIO;
    public final Camera camera;
    public final BlockRegistry registry;
    GUIDrawer guiDrawer;
    public Player player;
    private SkyBox skyBox;

    private int matLocation;

    private final Matrix4 mat;
    private Matrix4 perspectiveMatrix;

    private float a = 0;

    private int chunkCoolDown = 0;


    public Renderer(GL_Window window) {
        this.window = window;
        entities = new ArrayList<>();

        mat = Matrix4.identity();
        perspectiveMatrix = Matrix4.projectionMatrix((float) (30.f/180*Math.PI),(float)window.width/window.height,0.2f,1000);

        window.setResizeCallBack((w,newWidth,newHeight)->{
            this.window.height = newHeight;
            this.window.width = newWidth;
            perspectiveMatrix = Matrix4.projectionMatrix((float) (30.f/180*Math.PI),(float)window.width/window.height,0.2f,1000);
        });

        gameIO = new Game_IO(this);
        camera = new Camera(new Vector3(4 * 16,200,4 * 16),0,-0.25f);

        registry = new BlockRegistry();

        world = new World(100, this);

        player = Player.fromFile("0", this);

    }

    public void start() {
        GL.createCapabilities();

        System.out.format("[GLSL Version] %s%n",    glGetString(GL_SHADING_LANGUAGE_VERSION));
        System.out.format("[OpenGl Version] %s%n",  glGetString(GL_VERSION));

        //Set clear color
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        //Enable depth test
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_LINE_SMOOTH);


        //Enable alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBlendEquation(GL_FUNC_ADD);


        glEnable(GL_LINE_SMOOTH);


        try {
            worldShader =  new WorldShaderProgram();
            waterShader = new WaterShaderProgram();
        } catch (Exception e) {
            e.printStackTrace();
        }


        skyBox = new SkyBox();
        guiDrawer = new GUIDrawer(this);
        worldShader.use();



        world.loadTexture();


        Vector3 lightDirection = new Vector3(2,100,1);
        worldShader.setUniformVector("light_dir", lightDirection);

        matLocation = worldShader.uniformLocation("mat");




        /*  * * * * * * * * *
        *   MAIN GAME LOOP  *
        * * * * * * * * * * */

        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            gameIO.update();

            player.update();

            world.tick();


            render();



            window.swapBuffers();
            glfwPollEvents();
        }

        world.store();

    }

    private void render() {
        mat.reset();

        mat.apply(camera.cameraMatrix());
        mat.apply(perspectiveMatrix);

        world.render(mat);
        world.renderWater(mat);
        skyBox.render(mat, player.getPos());

        guiDrawer.render3D(mat);
        mat.reset();
        mat.apply(guiDrawer.guiMatrix());

        guiDrawer.renderStaticGUI(mat);

        Log.logGLError();
    }

    public GL_Window getWindow() {
        return window;
    }

    public void setDepthTest(boolean enabled) {
        if (enabled) {
            glEnable(GL_DEPTH_TEST);
        } else {
            glDisable(GL_DEPTH_TEST);
        }
    }

    public int chunkLoadedCount() {
        return world.chunks.size();
    }

    public void activateBlockTextures() {
        world.blockTextures.activateTextures();
    }

}
