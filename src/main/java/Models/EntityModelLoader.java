package Models;

import GL_Math.Vector2;
import GL_Math.Vector3;
import Textures.Texture;
import org.json.*;
import org.lwjgl.system.CallbackI;

import java.io.InputStream;
import java.util.*;

public class EntityModelLoader {


    public EntityModel loadState(String stateName) {
        try {
            return loadFromStateFile(stateName);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private EntityModel loadFromStateFile(String stateFile) throws Exception{
        JSONObject jsonObject = stateFileJSONObject(stateFile);

        JSONArray parts = jsonObject.getJSONArray("parts");

        List<CuboidModel> cuboidModelList = new ArrayList<>();

        for (int i = 0; i < parts.length(); i++) {
            JSONObject part = parts.getJSONObject(i);
            String modelName = part.getString("name");
            int rotation = part.optInt("rotation", 0);
            List<CuboidModel> models = loadFromEntityFile(modelName, rotation, false);

            JSONObject iff = part.optJSONObject("if");
            if (iff != null) {
                String[] conditions = JSONObject.getNames(iff);
                ModelOptions optionsMap = new ModelOptions();
                for (String condition: conditions) {
                    optionsMap.put(condition, iff.optString(condition,""));
                }
                for (CuboidModel m: models) {
                    m.setOptions(optionsMap);
                }
            }

            cuboidModelList.addAll(models);
        }

        boolean transparent = jsonObject.optBoolean("transparent",false);

        CuboidModel[] modelArr = new CuboidModel[cuboidModelList.size()];
        modelArr = cuboidModelList.toArray(modelArr);

        return new EntityModel(modelArr, transparent);
    }

    private List<CuboidModel> loadFromEntityFile(String entityName, int rotation, boolean uvLock) throws Exception {
        JSONObject jsonObject = modelFileJSONObject(entityName);
        jsonObject = loadDependecies(jsonObject);

        JSONArray elements = jsonObject.getJSONArray("elements");

        List<CuboidModel> cuboidModels = new ArrayList<>();

        JSONObject textures = jsonObject.getJSONObject("textures");



        for (int i = 0; i < elements.length(); i++) {
            JSONObject element = elements.getJSONObject(i);
            CuboidModel cuboidModel = getModelFromJSONObject(element, textures);
            cuboidModel.rotateY(rotation, uvLock);
            cuboidModels.add(cuboidModel);
        }

        return cuboidModels;
    }

    private CuboidModel getModelFromJSONObject(JSONObject element, JSONObject textures) throws JSONException {
        JSONArray originArr = element.getJSONArray("from");
        JSONArray endArr = element.getJSONArray("to");
        Vector3 origin = new Vector3(originArr.optDouble(0,0),originArr.optDouble(1,0),originArr.optDouble(2,0));
        Vector3 to = new Vector3(endArr.optDouble(0,0),endArr.optDouble(1,0),endArr.optDouble(2,0));
        Vector3 size = to.added(origin.multiplied(-1));

        JSONObject faces = element.getJSONObject("faces");

        CuboidModel newModel = new CuboidModel(origin,size);

        Map<CuboidFace.Face, CuboidFace> faceMap = new HashMap<>();

        for (CuboidFace.Face faceType: CuboidFace.Face.values()) {
            JSONObject face = faces.optJSONObject(faceType.rawValue);
            if (face == null) continue;
            JSONArray uv = face.optJSONArray("uv");
            String textureKey = face.optString("texture");
            Texture texture = textureForKey(textureKey, textures);
            boolean culling = face.optBoolean("cullface", false);
            int rotation = face.optInt("rotation", 0);

            Vector2 uvStart = new Vector2(0,0);
            Vector2 uvEnd = new Vector2(16,16);

            if (uv != null) {

                uvStart.x = (float) uv.optDouble(0,0);
                uvStart.y = (float) uv.optDouble(1,0);
                uvEnd.x = (float) uv.optDouble(2,16);
                uvEnd.y = (float) uv.optDouble(3,16);
            }

            Vector2 uvSize = uvEnd.added(uvStart.multiplied(-1));

            CuboidFace newFace = new CuboidFace(uvStart,uvSize,faceType, texture, newModel, rotation);
            newFace.culling = culling;
            faceMap.put(faceType,newFace);
        }

        newModel.faces = faceMap;



        //newModel.rotateY(1,true);

        return newModel;
    }

    private Texture textureForKey(String key, JSONObject lookup) {
        String textureName = textureLookUp(key, lookup);
        if (textureName.contains("&")) {
            return new Texture(textureName.substring(0,textureName.length() - 1), true);
        }else {
            return new Texture(textureName);
        }
    }

    private String textureLookUp(String key, JSONObject lookup) {

        if (key.startsWith("#")) {
            String val = lookup.optString(key.substring(1));
            return textureLookUp(val, lookup);
        } else {
            return key;
        }
    }

    private JSONObject loadDependecies(JSONObject object) throws Exception {
        String parentFile = object.optString("parent");
        if (parentFile != null && !Objects.equals(parentFile, "")) {
            JSONObject parentObject = modelFileJSONObject(parentFile);
            object.remove("parent");
            return loadDependecies(merge(parentObject,object));
        } else {
            return object;
        }
    }

    private JSONObject merge(JSONObject source, JSONObject target) {
        String[] names = JSONObject.getNames(source);

        try {
            for (String name: names) {
                Object value = source.get(name);
                if (target.has(name)) {
                    if (value instanceof JSONObject) {
                        merge((JSONObject) value, target.getJSONObject(name));
                    } else {
                        target.put(name, value);
                    }
                } else {
                    target.put(name, value);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return target;

    }

    private JSONObject stateFileJSONObject(String stateFileName) throws Exception {
        return fileToJSONObject("/states/" + stateFileName + ".json");
    }

    private JSONObject modelFileJSONObject(String modelFileName) throws Exception {
        return fileToJSONObject("/models/" + modelFileName + ".model");
    }

    private JSONObject fileToJSONObject(String fileName) throws Exception{
        InputStream is = this.getClass().getResourceAsStream(fileName);
        if (is == null) System.err.format("%s not found!", fileName);
        String input = convertStreamToString(is);
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            System.err.format("%s is not a valid file or not in correct format.", fileName);
            e.printStackTrace();
            throw e;
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
