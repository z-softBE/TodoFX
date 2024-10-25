package be.zsoft.todo.ui;

import lombok.experimental.UtilityClass;

import java.net.URL;

@UtilityClass
public class ResourceLoader {

    public static URL loadURL(String path) {
        return ResourceLoader.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }
}
