package Models;

public class GUITexturedVertex {
    public final float x;
    public final float y;
    public final float u;
    public final float v;
    public final float w;

    public GUITexturedVertex(float x, float y, float u, float v) {
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.w = 0;
    }

    public GUITexturedVertex(float x, float y, float u, float v, int layer) {
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.w = layer;
    }
}
