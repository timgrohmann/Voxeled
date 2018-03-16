import Models.EntityModel;
import Models.EntityModelLoader;

class EntityTest {
    public static void main(String[] args) {
        new EntityTest().runTest();
    }

    private void runTest() {
        EntityModel model = new EntityModelLoader().loadState("block/stone");
        System.out.println(model);
    }
}
