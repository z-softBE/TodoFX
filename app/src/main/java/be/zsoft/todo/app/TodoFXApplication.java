package be.zsoft.todo.app;

import be.zsoft.todo.ui.MainUIBuilder;
import be.zsoft.todo.ui.splash.SplashScreen;
import be.zsoft.todo.ui.utils.StageHolder;
import com.gluonhq.ignite.spring.SpringContext;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodoFXApplication extends Application {

    private final SpringContext context = new SpringContext(this);

    private ConfigurableApplicationContext ctx;
    private SplashScreen splashScreen;

    @Override
    public void init() throws Exception {
        super.init();

        splashScreen = new SplashScreen();
        context.init(() -> {
            ctx = SpringApplication.run(TodoApplication.class);
            return ctx;
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (ctx == null) {
            throw new Exception("The Spring context is not yet build!");
        }

        StageHolder.holdPrimaryStage(stage);
        ctx.getBean(MainUIBuilder.class).buildMainStage(stage);
        splashScreen.dispose();
    }

    @Override
    public void stop() {
        // Close this application context,
        // destroys all beans in its bean factory
        ctx.close();
    }

    @Bean
    public HostServices hostServices() {
        return super.getHostServices();
    }
}
