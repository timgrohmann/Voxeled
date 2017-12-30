import Models.EntityModel;
import Models.EntityModelLoader;

public class EntityTest {
    public static void main(String[] args) {
        new EntityTest().runTest();
    }

    public void runTest() {
        EntityModel model = new EntityModelLoader().loadState("block/stone");
        System.out.println(model);
    }
}
