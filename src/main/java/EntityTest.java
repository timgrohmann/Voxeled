import Models.EntityModel;
import Models.EntityModelLoader;
import org.json.JSONException;

public class EntityTest {
    public static void main(String[] args) {
        new EntityTest().runTest();
    }

    public void runTest() {
        EntityModel model = new EntityModelLoader().loadModel("block/stone");
        System.out.println(model);
    }
}
