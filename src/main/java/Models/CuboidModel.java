package Models;

import GL_Math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CuboidModel extends Model {
    Vector3 origin;
    Vector3 size;

    CuboidFace[] faces;

    public CuboidModel(Vector3 origin, Vector3 size, CuboidFace[] faces) {
        origin.multiply(1f/16);
        size.multiply(1f/16);
        this.origin = origin;
        this.size = size;
        this.faces = faces;

        Vector3[] corners;

        List<ModelVertex> vertexList = new ArrayList<>();

        for (CuboidFace face: faces) {
            switch (face.face) {
                case TOP:
                    corners = new Vector3[]{origin.added(0,size.y,0),
                            origin.added(size.x,size.y,0),
                            origin.added(size.x,size.y,size.z),
                            origin.added(0,size.y,size.z)};
                    vertexList.addAll(quadFromCorners(face.texturedVertices(corners)));
                    break;
                case BOTTOM:
                    corners = new Vector3[]{origin.added(0,0,0),
                            origin.added(0,0,size.z),
                            origin.added(size.x,0,size.z),
                            origin.added(size.x,0,0)};
                    vertexList.addAll(quadFromCorners(face.texturedVertices(corners)));
                    break;
                case LEFT:
                    corners = new Vector3[]{origin.added(0,size.y,0),
                            origin.added(0,size.y,size.z),
                            origin.added(0,0,size.z),
                            origin.added(0,0,0),};
                    vertexList.addAll(quadFromCorners(face.texturedVertices(corners)));
                    break;
                case RIGHT:
                    corners = new Vector3[]{origin.added(size.x,size.y,size.z),
                            origin.added(size.x,size.y,0),
                            origin.added(size.x,0,0),
                            origin.added(size.x,0,size.z)};
                    vertexList.addAll(quadFromCorners(face.texturedVertices(corners)));
                    break;
                case FRONT:
                    corners = new Vector3[]{origin.added(0,size.y,size.z),
                            origin.added(size.x,size.y,size.z),
                            origin.added(size.x,0,size.z),
                            origin.added(0,0,size.z)};
                    vertexList.addAll(quadFromCorners(face.texturedVertices(corners)));
                    break;
                case BACK:
                    corners = new Vector3[]{origin.added(size.x,size.y,0),
                            origin.added(0,size.y,0),
                            origin.added(0,0,0),
                            origin.added(size.x,0,0)};
                    vertexList.addAll(quadFromCorners(face.texturedVertices(corners)));
                    break;
            }
        }

        vertices = new ModelVertex[vertexList.size()];
        vertices = vertexList.toArray(vertices);
    }


    private List<ModelVertex> quadFromCorners(List<ModelVertex> ms) {
        return Arrays.asList(
                ms.get(0),ms.get(1),ms.get(2),
                ms.get(2),ms.get(3),ms.get(0)
        );
    }

}
