package be.zsoft.todo.ui.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error(e.getMessage(), e);
        ExceptionDialog.show(e);
    }
}
