package Models;

import GL_Math.Vector3;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.Map;

public class EntityModel extends Model {
    private CuboidModel[] cuboidModels;

    public boolean transparent;
    public HitBoxModel hitBoxModel;

    private Map<String, Boolean> options = new HashMap<>();


    public EntityModel(CuboidModel[] cuboidModels, boolean transparent) {
        this.cuboidModels = cuboidModels;
        this.origin = new Vector3();
        this.transparent = transparent;
        this.hitBoxModel = EntityModel.makeHitBoxModel(cuboidModels);
    }

    private static HitBoxModel makeHitBoxModel(CuboidModel[] cuboidModels) {
        if (cuboidModels.length == 0) throw new RuntimeException(String.format("%s model without elements!",EntityModel.class));

        Vector3 min = Vector3.max();
        Vector3 max = Vector3.min();

        for (CuboidModel model: cuboidModels) {
            Vector3 minM = model.origin;
            Vector3 maxM = model.origin.added(model.size);
            min.x = Float.min(minM.x,min.x);
            min.y = Float.min(minM.y,min.y);
            min.z = Float.min(minM.z,min.z);

            max.x = Float.max(maxM.x,max.x);
            max.y = Float.max(maxM.y,max.y);
            max.z = Float.max(maxM.z,max.z);
        }

        return new HitBoxModel(min,max);
    }

    public CuboidModel[] getCuboidModels() {
        return cuboidModels;
    }

    public int getVertexCount(Culling culling) {
        return getVertexCount(culling, new HashMap<>());
    }

    public int getVertexCount(Culling culling, Map<String, Boolean> options) {
        int sum = 0;
        for (CuboidModel m: cuboidModels) {
            if (!m.shouldBeRendered(options)) continue;
            sum += m.getVertexCount(culling);
        }
        return sum;
    }

    public ModelVertex[] getModelVertices(Culling culling) {
        return getModelVertices(culling, new HashMap<>());
    }

    public ModelVertex[] getModelVertices(Culling culling, Map<String, Boolean> options) {
        ModelVertex[] modelVertices = new ModelVertex[getVertexCount(culling,options)];

        int s = 0;
        for (CuboidModel m: cuboidModels) {
            if (!m.shouldBeRendered(options)) continue;
            ModelVertex[] cuboidVertices = m.getModelVertices(culling);
            System.arraycopy(cuboidVertices,0,modelVertices,s,cuboidVertices.length);
            s += cuboidVertices.length;
        }
        return modelVertices;
    }
}
