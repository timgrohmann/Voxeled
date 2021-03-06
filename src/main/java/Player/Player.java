package Player;

import Entities.*;
import GL_Math.Vector3;
import Main_Package.FileManager;
import Main_Package.Renderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("FieldCanBeLocal")
public class Player extends Entity implements Collidable{
    private final Vector3 speed;
    private final Vector3 acc;

    float pitch;

    private HitBox hitBox;

    final Renderer renderer;

    private boolean onGround = false;
    private boolean walkedInThisTick = false;

    private boolean canFly = true;

    private static final float AIR_FRICTION = 0.999f;
    private static final float GROUND_FRICTION = 0.6f;
    private static final float EYE_HEIGHT = 1.6f;

    public int selectedSlot = 0;
    public Inventory inventory;

    private float walkBumpState = 0;

    private float lastX;

    private Player(Renderer renderer, Vector3 pos) {
        super(pos);
        this.renderer = renderer;

        speed = Vector3.zero();
        acc = Vector3.zero();
        hitBox = new HitBox(new Vector3(-0.4f,0,-0.4f), new Vector3(0.4f,1.8f,0.4f), this);
        inventory = new Inventory(this);
    }

    @Override
    public void update() {
        // Gravity

        keyInput();

        gravity();


        //pos.add(horizontalMov);
        speed.add(acc);
        pos.add(speed);


        collision();



        if (!onGround && !walkedInThisTick) {
            speed.x *= AIR_FRICTION;
            speed.z *= AIR_FRICTION;
        }else {
            speed.x *= GROUND_FRICTION;
            speed.z *= GROUND_FRICTION;
        }



        //pos.y += speedY;
        if (onGround) walkBumpState += (float) Math.sqrt(speed.x*speed.x + speed.z*speed.z) * 5f;
        walkBumpState = (walkBumpState > Math.PI * 2) ? walkBumpState - (float) Math.PI * 2 : walkBumpState;
        renderer.camera.setPosition(cameraPosition());
        //speed.x = 0; speed.z = 0;
        acc.setAll(0);
        walkedInThisTick = false;
        lastX = this.pos.x;
    }

    private void keyInput() {
        float speed = 0.04f;
        if (glfwGetKey(renderer.getWindow().identifier,GLFW_KEY_W) == GLFW_PRESS){
            this.moveForward(speed);
        }

        if (glfwGetKey(renderer.getWindow().identifier,GLFW_KEY_S) == GLFW_PRESS){
            this.moveBackward(speed);
        }

        if (glfwGetKey(renderer.getWindow().identifier,GLFW_KEY_A) == GLFW_PRESS){
            this.moveLeft(speed);
        }

        if (glfwGetKey(renderer.getWindow().identifier,GLFW_KEY_D) == GLFW_PRESS){
            this.moveRight(speed);
        }

        if (glfwGetKey(renderer.getWindow().identifier,GLFW_KEY_SPACE) == GLFW_PRESS){
            this.jump(0.14f);
        }
    }

    private void collision() {

        Vector3 corr = Vector3.zero();

        for (int h = -1; h <= 2; h++) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    CollisionResult collisionResult = collideWithBlockAtDiff(i,h,j);
                    if (collisionResult.xCorr != 0) {
                        this.speed.x = 0;
                        corr.x = maxAbs(collisionResult.xCorr, corr.x);
                    }
                    if (collisionResult.yCorr != 0) {
                        this.speed.y = 0;
                        corr.y = maxAbs(collisionResult.yCorr, corr.y);
                        //corr.y += collisionResult.yCorr;
                        if (collisionResult.yCorr > 0) onGround = true;
                    }
                    if (collisionResult.zCorr != 0) {
                        this.speed.z = 0;
                        corr.z = maxAbs(collisionResult.zCorr, corr.z);
                        //corr.z = collisionResult.zCorr;
                    }
                }
            }
        }

        this.pos.add(corr);
    }

    private float maxAbs(float a, float b) {
        if (Math.abs(a) > Math.abs(b)) return a;
        return b;
    }

    private  <T extends Collidable> CollisionResult collideWith(T e){
        if (e == null) return CollisionResult.none;
        return hitBox.checkCollision(e.getHitbox());
    }

    private CollisionResult collideWithBlockAtDiff(float dx, float dy, float dz) {
        Block block = renderer.world.getBlockForCoordinates(new Vector3(pos.x + dx, pos.y + dy, pos.z + dz));
        if (block == null) return CollisionResult.none;
        CollisionResult r = collideWith(block);

        if (!block.isVisibleLeft() && r.xCorr < 0) {
            r.xCorr = 0;
        }
        if (!block.isVisibleRight() && r.xCorr > 0) {
            r.xCorr = 0;
        }

        if (!block.isVisibleBack() && r.zCorr < 0) {
            r.zCorr = 0;
        }
        if (!block.isVisibleFront() && r.zCorr > 0) {
            r.zCorr = 0;
        }
        return r;
    }

    private float fractional(float a) {
        return a - (float) Math.floor(a);
    }

    private void gravity() {
        this.speed.y += -0.008f;
    }

    private Vector3 cameraPosition() {
        return new Vector3(this.pos.x, this.pos.y + Player.EYE_HEIGHT + (float) Math.sin(walkBumpState) * 0.07f, this.pos.z);
    }

    private void moveForward(float amount) {
        addHorizontalMov((float) Math.cos(renderer.camera.heading) * amount,(float) Math.sin(renderer.camera.heading) * amount);
    }
    private void moveBackward(float amount) {
        moveForward(-amount);
    }
    private void moveLeft(float amount) {
        addHorizontalMov((float) Math.cos(renderer.camera.heading - (float) Math.PI / 2) * amount,(float) Math.sin(renderer.camera.heading - (float) Math.PI / 2) * amount);
    }
    private void moveRight(float amount) {
        addHorizontalMov((float) Math.cos(renderer.camera.heading + (float) Math.PI / 2) * amount,(float) Math.sin(renderer.camera.heading + (float) Math.PI / 2) * amount);
    }

    private void jump(float amount) {
        if (onGround) {
            speed.y += amount;
            onGround = false;
        }
    }

    private void addHorizontalMov(float x, float z) {
        //if (!onGround) return;
        walkedInThisTick = true;
        /*float currentSpeed = (float) Math.sqrt(speed.x * speed.x + speed.z * speed.z);
        float maxSpeed = 0.1f;
        float fac = -1f/(maxSpeed*maxSpeed) * currentSpeed * currentSpeed + 1;
        fac = (fac < 0) ? 0 : (fac > 1) ? 1 : fac;*/
        this.acc.x += x;
        this.acc.z += z;
    }

    public Block getSelectedBlock() {
        return inventory.barBlocks[selectedSlot];
    }

    @Override
    public HitBox getHitbox() {
        return hitBox;
    }

    public static Player fromFile(String playerName, Renderer renderer) {

        try {
            byte[] bytes = FileManager.getFromFile(playerName + ".player");
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            float xPos = buffer.getFloat(0);
            float yPos = buffer.getFloat(4);
            float zPos = buffer.getFloat(8);
            System.out.format("Read pos: %.2f %.2f %.2f%n", xPos, yPos, zPos);
            return new Player(renderer, new Vector3(xPos,yPos,zPos));
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.format("Info: Player file %s.player not found!%n",playerName);
            return new Player(renderer, new Vector3(0, 100, 0));
        }
    }

    public void saveToFile() {
        String fileName = "0.player";

        byte[] xPos = floatToByteArray(pos.x);
        byte[] yPos = floatToByteArray(pos.y);
        byte[] zPos = floatToByteArray(pos.z);

        byte[] bs = new byte[xPos.length+yPos.length+zPos.length];
        System.arraycopy(xPos,0,bs,0,xPos.length);
        System.arraycopy(yPos,0,bs,xPos.length,yPos.length);
        System.arraycopy(zPos,0,bs,xPos.length + yPos.length,zPos.length);

        try{
            FileManager.writeToFile(bs, fileName);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte [] floatToByteArray(float value)
    {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }
}
