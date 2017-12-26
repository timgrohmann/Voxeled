package Main_Package;

import GL_Math.*;

public class Camera {
    private final Vector3 pos;
    float heading;

    private float pitch;
    private float aFloat;

    Camera(Vector3 pos, float heading, float pitch) {
        this.pos = pos.multiply(-1);
        this.heading = heading;
        this.pitch = pitch;
    }

    private void moveForward(float amount) {
        pos.x -= (float) Math.cos(heading) * amount;
        pos.z -= (float) Math.sin(heading) * amount;
    }

    void moveBackward(float amount) {
        this.moveForward(-amount);
    }

    private void moveLeft(float amount) {
        pos.x += (float) Math.cos(heading + Math.PI / 2) * amount;
        pos.z += (float) Math.sin(heading + Math.PI / 2) * amount;
    }

    void moveRight(float amount) {
        this.moveLeft(-amount);
    }

    void yaw(float angle) {
        this.heading += angle;


        if (heading > Math.PI * 2) heading -= Math.PI * 2;
        if (heading < 0) heading += Math.PI * 2;
    }

    void pitch(float angle) {
        this.pitch += angle;


        if (this.pitch > Math.PI / 2) {
            this.pitch = (float) Math.PI/2;
        }
        if (this.pitch < -Math.PI / 2) {
            this.pitch = (float) -Math.PI/2;
        }
    }

    private void moveUp(float amount) {
        this.pos.y -= amount;
    }
    void moveDown(float amount) {
        this.moveUp(-amount);
    }

    void setHeading(float heading) {
        this.heading = heading;
    }

    void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /*void logData() {
        System.out.println("-----");
        System.out.println("x: " + String.valueOf(this.pos.x * -1) + "  dir x: " + String.valueOf(-Math.sin(this.heading) * Math.cos(pitch)));
        System.out.println("y: " + String.valueOf(this.pos.y * -1) + "  dir y: " + String.valueOf(Math.sin(this.pitch)));
        System.out.println("z: " + String.valueOf(this.pos.z * -1) + "  dir z: " + String.valueOf(-Math.cos(this.heading) * Math.cos(pitch)));
    }*/

    public Vector3 rayAtStep(float step) {
        float sinHead = (float) Math.sin(-heading - (float) Math.PI / 2);
        float cosHead = (float) Math.cos(-heading - (float) Math.PI / 2);
        float sinPitch = (float) Math.sin(pitch);
        float cosPitch = (float) Math.cos(pitch);
        return new Vector3(
                -this.pos.x - sinHead * cosPitch * step,
                -this.pos.y + sinPitch * step,
                -this.pos.z - cosHead * cosPitch * step
        );
    }


    Matrix4 cameraMatrix() {
        Matrix4 translationMatrix = Matrix4.translationMatrix(pos);

        Matrix4 rotationMatrix = Matrix4.rotationMatrixY(heading + (float) Math.PI / 2);
        Matrix4 pitchMatrix = Matrix4.rotationMatrixX(pitch);

        translationMatrix.apply(rotationMatrix);
        translationMatrix.apply(pitchMatrix);

        return translationMatrix;
    }

    public Vector3 getLightPos() {
        return new Vector3(-pos.x, -pos.y + 50, -pos.z);
    }

    public void setPosition(Vector3 position) {
        this.pos.x = -position.x;
        this.pos.y = -position.y;
        this.pos.z = -position.z;
    }


    public Vector3 getPosition() {
        return new Vector3(-this.pos.x,-this.pos.y,-this.pos.z);
    }
}
