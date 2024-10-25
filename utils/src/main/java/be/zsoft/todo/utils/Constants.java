package be.zsoft.todo.utils;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {

    public static final Path APPLICATION_DIRECTORY = Path.of(System.getProperty("user.home"), ".todoFX");
    public static final DateTimeFormatter DUE_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
