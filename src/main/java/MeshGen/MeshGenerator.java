package MeshGen;


class MeshGenerator {
    private final int cellCount;

    public MeshGenerator(int cellCount) {
        this.cellCount = cellCount;
    }

    public float[] getMeshValues(){
        float[] values = new float[cellCount * cellCount];

        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                float x = (float) i / cellCount;
                float y = (float) j / cellCount;

                float s = 50;

                //values[i * cellCount + j] = (float) PerlinNoise.noise(x * s,y * s) * 1;
            }
        }

        return values;
    }
}
