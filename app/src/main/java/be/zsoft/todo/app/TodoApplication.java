package be.zsoft.todo.app;

import be.zsoft.todo.ui.exception.ExceptionHandler;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import be.zsoft.todo.utils.Constants;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j

@SpringBootApplication(
        scanBasePackages = {"com.gluonhq.ignite.spring", "be.zsoft.todo"},
        exclude = {DataSourceAutoConfiguration.class}
)
public class TodoApplication {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        SetupSwingLookAndFeel();
        SetupApplicationDirectory();
        Application.launch(TodoFXApplication.class, args);
    }

    private static void SetupSwingLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            log.error(ex.getMessage(), ex);
            System.exit(-1);
        }
    }

    private static void SetupApplicationDirectory() {
        log.info("Creating application directory");

        try {
            Files.createDirectories(Constants.APPLICATION_DIRECTORY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
