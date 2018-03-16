package Models;

import java.util.HashMap;

public class ModelOptions extends HashMap<String, String> {
    public ModelOptions() {
        super();
    }

    public void putBool(String key, boolean bool) {
        put(key, bool ? "true" : "false");
    }
}
