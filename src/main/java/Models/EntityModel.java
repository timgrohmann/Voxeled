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

    public CuboidModel[] getCuboidModels() {
        return cuboidModels;
    }

    public int getVertexCount(Culling culling) {
        int sum = 0;
        for (CuboidModel m: cuboidModels) {
            sum += m.getVertexCount(culling);
        }
        return sum;
    }

    public ModelVertex[] getModelVertices(Culling culling) {
        ModelVertex[] modelVertices = new ModelVertex[getVertexCount(culling)];

        int s = 0;
        for (CuboidModel m: cuboidModels) {
            ModelVertex[] cuboidVertices = m.getModelVertices(culling);
            System.arraycopy(cuboidVertices,0,modelVertices,s,cuboidVertices.length);
            s += cuboidVertices.length;
        }
        return modelVertices;
    }
}
