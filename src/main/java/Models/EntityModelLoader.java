package Models;

import GL_Math.Vector2;
import GL_Math.Vector3;
import Textures.Texture;
import org.json.*;
import org.lwjgl.system.CallbackI;

import java.io.InputStream;
import java.util.*;

public class EntityModelLoader {


    public EntityModel loadModel(String modelName) {
        try {
            return loadFromEntityFile(modelName);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private EntityModel loadFromEntityFile(String entityName) throws JSONException {
        JSONObject jsonObject = fileToJSONObject(entityName);
        jsonObject = loadDependecies(jsonObject);

        JSONArray elements = jsonObject.getJSONArray("elements");

        List<CuboidModel> cuboidModels = new ArrayList<>();

        JSONObject textures = jsonObject.getJSONObject("textures");

        boolean transparent = jsonObject.optBoolean("transparent",false);

        for (int i = 0; i < elements.length(); i++) {
            JSONObject element = elements.getJSONObject(i);
            CuboidModel cuboidModel = getModelFromJSONObject(element, textures);
            cuboidModels.add(cuboidModel);
        }

        CuboidModel[] cuboidModelArray = new CuboidModel[cuboidModels.size()];
        cuboidModelArray = cuboidModels.toArray(cuboidModelArray);
        return new EntityModel(cuboidModelArray, transparent);
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

        JSONObject iff = element.optJSONObject("if");
        if (iff != null) {
            String[] conditions = JSONObject.getNames(iff);
            Map<String,Boolean> optionsMap = new HashMap<>();
            for (String condition: conditions) {
                optionsMap.put(condition, iff.optBoolean(condition,true));
            }
            newModel.setOptions(optionsMap);
        }
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

    private JSONObject loadDependecies(JSONObject object) {
        String parentFile = object.optString("parent");
        if (parentFile != null && !Objects.equals(parentFile, "")) {
            JSONObject parentObject = fileToJSONObject(parentFile);
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

    private JSONObject fileToJSONObject(String fileName){
        InputStream is = this.getClass().getResourceAsStream("/models/" + fileName + ".model");
        if (is == null) System.err.format("%s not found!", fileName);
        String input = convertStreamToString(is);
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            System.err.format("%s is not a valid file or not in correct format.", fileName);
            e.printStackTrace();
            return null;
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
