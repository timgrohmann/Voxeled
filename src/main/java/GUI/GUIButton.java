package GUI;

import GL_Math.Vector2;


class GUIButton extends UIBasicTexturedComponent implements Interactable{

    private final GUIText guiText;

    private static Vector2 UV_ORIGIN = new Vector2(0,66);
    private static Vector2 UV_SIZE = new Vector2(200,20);

    private Runnable executer = () -> {};

    public GUIButton(Vector2 pos, float width, String text) {
        super(pos, width, true, UV_ORIGIN, UV_SIZE);

        guiText = new GUIText(text,this.centerPos(),0.08f,true);

        this.textureDescriptor = GUIDrawer.WIDGET_TEXTURE;
    }

    public void setExecuter(Runnable executer) {
        this.executer = executer;
    }

    GUIText getText() {
        return guiText;
    }

    @Override
    public void click(Vector2 pos) {
        System.out.format("Clicked button%n");
        executer.run();
    }

    @Override
    public GUIRectangle getRect() {
        return new GUIRectangle(this.pos, this.size);
    }

    @Override
    public boolean currentlyInteractable() {
        return isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        guiText.setVisible(visible);
    }
}
