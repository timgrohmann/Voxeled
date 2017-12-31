package Models;

import GL_Math.Vector3;

public class EntityModel extends Model {
    private CuboidModel[] cuboidModels;

    public boolean transparent;

    public EntityModel(CuboidModel[] cuboidModels, boolean transparent) {
        this.cuboidModels = cuboidModels;
        this.origin = new Vector3();
        this.transparent = transparent;
    }

    public HitBoxModel getHitBoxModel(ModelOptions modelOptions) {
        //Optimization: Store generated HBMs
        if (cuboidModels.length == 0) throw new RuntimeException(String.format("%s model without elements!",EntityModel.class));

        Vector3 min = Vector3.max();
        Vector3 max = Vector3.min();

        for (CuboidModel model: cuboidModels) {
            if (!model.shouldBeRendered(modelOptions)) continue;

            for (ModelVertex vertex: model.getModelVertices(new Culling(true))) {
                min.x = Float.min(vertex.x,min.x);
                min.y = Float.min(vertex.y,min.y);
                min.z = Float.min(vertex.z,min.z);

                max.x = Float.max(vertex.x,max.x);
                max.y = Float.max(vertex.y,max.y);
                max.z = Float.max(vertex.z,max.z);
            }

        }

        return new HitBoxModel(min,max);
    }

    public CuboidModel[] getCuboidModels() {
        return cuboidModels;
    }


    public int getVertexCount(Culling culling) {
        return getVertexCount(culling, new ModelOptions());
    }

    public int getVertexCount(Culling culling, ModelOptions options) {
        int sum = 0;
        for (CuboidModel m: cuboidModels) {
            if (!m.shouldBeRendered(options)) continue;
            sum += m.getVertexCount(culling);
        }
        return sum;
    }

    public ModelVertex[] getModelVertices(Culling culling) {
        return getModelVertices(culling, new ModelOptions());
    }

    public ModelVertex[] getModelVertices(Culling culling, ModelOptions options) {
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
