package GL_Math;

public class Vector3 {
    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public static Vector3 zero (){
        return new Vector3(0,0,0);
    }

    public static Vector3 crossProduct(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x
        );
    }

    public Vector3 normalize(){
        float m = mag();
        this.x /= m;
        this.y /= m;
        this.z /= m;
        return this;
    }

    private float mag() {
        return (float) Math.sqrt(x*x+y*y+z*z);
    }

    public float[] components(){
        return new float[]{x,y,z};
    }

    public Vector3 multiply(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    public Vector3 multiplied(float scalar) {
        Vector3 copy = copy();
        return copy.multiply(scalar);
    }

    public Vector3 add(Vector3 adder) {
        this.x += adder.x;
        this.y += adder.y;
        this.z += adder.z;
        return this;
    }

    public Vector3 added(Vector3 adder) {
        Vector3 copy = copy();
        return copy.add(adder);
    }

    public Vector3 copy() {
        return new Vector3(x,y,z);
    }

    public void setAll(float all) {
        this.x = this.y = this.z = all;
    }
}