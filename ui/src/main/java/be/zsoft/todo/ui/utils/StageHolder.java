package be.zsoft.todo.ui.utils;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StageHolder {

    @Getter
    private static Stage primaryStage = null;

    public static void holdPrimaryStage(Stage primaryStage) {
        StageHolder.primaryStage = primaryStage;
    }


}
