package GL_Math;

public class Vector2 {
    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public static Vector2 zero = new Vector2(0, 0);

    public Vector2 normalize() {
        float m = mag();
        this.x /= m;
        this.y /= m;
        return this;
    }

    private float mag() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float[] components() {
        return new float[]{x, y};
    }

    public Vector2 multiply(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2 multiplied(float scalar) {
        Vector2 copy = copy();
        return copy.multiply(scalar);
    }

    public Vector2 add(Vector3 adder) {
        this.x += adder.x;
        this.y += adder.y;
        return this;
    }

    public Vector2 added(Vector3 adder) {
        Vector2 copy = copy();
        return copy.add(adder);
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    public void setAll(float all) {
        this.x = this.y = all;
    }
}