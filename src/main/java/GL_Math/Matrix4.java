package GL_Math;

public class Matrix4 {
    public float[] components = new float[16];

    private Matrix4(float[] components) {
        this.components = components;
    }

    public static Matrix4 identity() {
        return new Matrix4(new float[]{
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1,
        });
    }

    public void apply(Matrix4 mat2) {
        float[] n = new float[16];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float s = 0;
                for (int k = 0; k < 4; k++) {
                    float v1 = mat2.getValue(row, k);
                    float v2 = this.getValue(k, col);
                    s += v1 * v2;
                }
                n[row * 4 + col] = s;
            }
        }

        this.components = n;
    }

    public static Matrix4 rotationMatrixY(float angle) {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);

        return new Matrix4(new float[]{
                cos,0,sin,0,
                0,1,0,0,
                -sin,0,cos,0,
                0,0,0,1,
        });
    }

    public static Matrix4 rotationMatrixX(float angle) {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);

        return new Matrix4(new float[]{
                1,0,0,0,
                0,cos,sin,0,
                0,-sin,cos,0,
                0,0,0,1,
        });
    }

    public Matrix4 copy() {
        float[] newArray = new float[16];
        System.arraycopy(components,0,newArray,0,16);
        return new Matrix4(newArray);
    }

    public void reset() {
        this.components = identity().components;
    }

    private float getValue(int row, int col) {
        return components[row * 4 + col];
    }

    private void setValue(int row, int col, float val) {
        components[row * 4 + col] = val;
    }

    public static Matrix4 projectionMatrix(float fov, float aspect, float near, float far) {
        float tan = (float) Math.tan(fov);

        float rl = tan * near * aspect;
        float tb = tan * near;

        return new Matrix4(new float[]{
                near/rl,0,0,0,
                0,near/tb,0,0,
                0,0,-(far+near)/(far-near),-2*far*near/(far-near),
                0,0,-1,0
        });
    }

    public static Matrix4 translationMatrix(Vector3 v) {
        return  Matrix4.translationMatrix(v.x,v.y,v.z);
    }

    private static Matrix4 translationMatrix(float x, float y, float z) {
        return new Matrix4(new float[]{
                1,0,0,x,
                0,1,0,y,
                0,0,1,z,
                0,0,0,1
        });
    }

    public static Matrix4 guiMatrix(float scal, float aspect) {
        return new Matrix4(new float[]{
                scal / aspect,0,0,0,
                0,scal,0,0,
                0,0,1,0,
                0,0,0,1,
        });
    }
}
