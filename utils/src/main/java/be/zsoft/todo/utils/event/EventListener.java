package be.zsoft.todo.utils.event;

public interface EventListener {

    void onEvent(Event event);

    boolean supports(Event event);
}
