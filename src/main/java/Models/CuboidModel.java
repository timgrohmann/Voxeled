package Models;

import GL_Math.Vector3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CuboidModel extends Model {
    Vector3 origin;
    Vector3 size;

    Map<CuboidFace.Face, CuboidFace> faces;

    private Map<String, Boolean> options = new HashMap<>();
    //CuboidFace[] faces;

    public CuboidModel(Vector3 origin, Vector3 size) {
        origin.multiply(1f/16);
        size.multiply(1f/16);
        this.origin = origin;
        this.size = size;
    }

    public int getVertexCount(Culling culling) {
        int sum = 0;
        for (CuboidFace face: faces.values()) {
            sum += face.getVertexCount(culling);
        }
        return sum;
    }

    @Override
    ModelVertex[] getModelVertices(Culling culling) {
        ModelVertex[] modelVertices = new ModelVertex[getVertexCount(culling)];
        int p = 0;
        for (CuboidFace cuboidFace: faces.values()) {
            if (cuboidFace.visible(culling)) {
                System.arraycopy(cuboidFace.getModelVertices(culling),0,modelVertices,p,6);
                p += 6;
            }
        }

        return modelVertices;
    }

    public Collection<CuboidFace> getFaces() {
        return faces.values();
    }

    public Map<CuboidFace.Face, CuboidFace> getFacesMap() {
        return faces;
    }

    public void setOptions(Map<String, Boolean> options) {
        this.options = options;
    }

    boolean shouldBeRendered(Map<String,Boolean> testOptions) {

        for(Map.Entry<String, Boolean> entry : options.entrySet()) {
            String key = entry.getKey();
            Boolean expectedValue = entry.getValue();

            Boolean testOptionsVal = testOptions.get(key);


            if (testOptionsVal == null) {
                return false;
            } else if (testOptionsVal.booleanValue() != expectedValue) {
                return false;
            }

        }

        return true;
    }
}
