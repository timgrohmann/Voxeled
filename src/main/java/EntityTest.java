import Models.EntityModel;
import Models.EntityModelLoader;
import org.json.JSONException;

public class EntityTest {
    public static void main(String[] args) {
        new EntityTest().runTest();
    }

    public void runTest() {
        EntityModelLoader loader = new EntityModelLoader();
        try {
            EntityModel model = loader.loadFromEntityFile("block/stone");
            System.out.println(model);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
