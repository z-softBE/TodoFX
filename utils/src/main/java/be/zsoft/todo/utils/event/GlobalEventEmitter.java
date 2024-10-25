package be.zsoft.todo.utils.event;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class GlobalEventEmitter {

    private static final Map<String, EventListener> listeners = new HashMap<>();

    public static void registerListener(String name, EventListener listener) {
        listeners.put(name, listener);
    }

    public static void removeListener(String name) {
        listeners.remove(name);
    }

    public static void emit(Event event) {
        listeners.values().stream()
                .filter(listener -> listener.supports(event))
                .forEach(listener -> listener.onEvent(event));
    }
}
