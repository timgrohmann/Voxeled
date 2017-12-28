package Models;

public class Culling {
    public boolean top, bottom, left, right, back, front;

    public Culling(boolean top, boolean bottom, boolean left, boolean right, boolean back, boolean front) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.back = back;
        this.front = front;
    }

    public Culling(boolean all) {
        this(all,all,all,all,all,all);
    }
}
