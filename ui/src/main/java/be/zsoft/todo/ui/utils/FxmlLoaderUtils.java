package be.zsoft.todo.ui.utils;

import be.zsoft.todo.ui.ResourceLoader;
import javafx.fxml.FXMLLoader;
import javafx.util.Pair;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@UtilityClass
public class FxmlLoaderUtils {

    public static <T, C> Pair<T, C> loadView(String fxmlPath, ApplicationContext context) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ResourceLoader.loadURL(fxmlPath));
        fxmlLoader.setControllerFactory(type -> getControllerInstanceFromSpringContainer(context, type));

        T root = fxmlLoader.load();
        C controller = fxmlLoader.getController();

        return new Pair<>(root, controller);
    }

    public static <T > T loadControl(String fxmlPath, Object controller) throws IOException {
        return loadControl(fxmlPath, controller, true);
    }

    public static <T > T loadControl(String fxmlPath, Object controller, boolean withRoot) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ResourceLoader.loadURL(fxmlPath));
        if (withRoot) {
            fxmlLoader.setRoot(controller);
        }
        fxmlLoader.setController(controller);

        return fxmlLoader.load();
    }

    private static <T> T getControllerInstanceFromSpringContainer(ApplicationContext appContext, Class<T> type) {
        T instance = null;
        try {
            instance = type.getDeclaredConstructor().newInstance();
            injectMembersIntoSpringBean(appContext, instance);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("Cannot create instance of specified class:  " + type.getName(), e);
        }
        return instance;
    }

    private static void injectMembersIntoSpringBean(ApplicationContext appContext, Object instance) {
        appContext.getAutowireCapableBeanFactory().autowireBean(instance);
    }
}
