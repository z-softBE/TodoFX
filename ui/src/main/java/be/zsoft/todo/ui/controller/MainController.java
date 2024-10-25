package be.zsoft.todo.ui.controller;

import be.zsoft.todo.ui.ResourceLoader;
import be.zsoft.todo.ui.control.AddListButton;
import be.zsoft.todo.ui.event.TodoListChangedEvent;
import be.zsoft.todo.ui.service.DialogService;
import be.zsoft.todo.ui.service.NavbarBuilder;
import be.zsoft.todo.ui.utils.FxmlLoaderUtils;
import be.zsoft.todo.ui.utils.navbar.NavbarItem;
import be.zsoft.todo.utils.event.Event;
import be.zsoft.todo.utils.event.EventListener;
import be.zsoft.todo.utils.event.GlobalEventEmitter;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class MainController {

    private static final String GITHUB_URL = "https://github.com/z-softBE/TodoFX";
    private static final String ZSOFT_URL = "https://z-soft.be";
    private static final String DONATE_URL = "https://buymeacoffee.com/zsoft";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HostServices hostServices;

    @Autowired
    private NavbarBuilder navbarBuilder;

    @Autowired
    private DialogService dialogService;

    @FXML
    private BorderPane contentPane;

    @FXML
    private MFXScrollPane navbarScrollPane;

    @FXML
    private VBox navbarVBox;

    @FXML
    private StackPane logoContainer;

    @FXML
    private ImageView zsoftLogo;

    @FXML
    private HBox titleHbox;

    private ToggleGroup toggleGroup;
    private DetailsViewController detailsViewController;

    @FXML
    public void initialize() {
        try {
            loadController();
            buildNavbar(null);
            ScrollUtils.addSmoothScrolling(navbarScrollPane);
            initializeLogo();
            titleHbox.getChildren().add(new AddListButton(dialogService.createAddListDialog(contentPane)));
            initializeZSoft();
            setupGlobalListener();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    void onDonate(ActionEvent event) {
        hostServices.showDocument(DONATE_URL);
    }

    @FXML
    void onGithub(ActionEvent event) {
        hostServices.showDocument(GITHUB_URL);
    }

    private void buildNavbar(String idToSelect) {
        toggleGroup = new ToggleGroup();
        List<Node> navbarNodes = navbarBuilder.buildNavbar(toggleGroup, contentPane);
        navbarVBox.getChildren().clear();
        navbarVBox.getChildren().setAll(navbarNodes);

        toggleGroup.selectToggle(toggleGroup.getToggles().getFirst());
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                toggleGroup.selectToggle(oldValue);
                return;
            }

            loadPage((NavbarItem) newValue.getUserData());
        });

        Toggle toggleToSelect = Optional.ofNullable(idToSelect)
                .flatMap(id -> navbarBuilder.getToggleToSelect(id, navbarNodes))
                .orElse(toggleGroup.getToggles().getFirst());
        toggleGroup.selectToggle(toggleToSelect);
        loadPage((NavbarItem) toggleToSelect.getUserData());
    }

    private void initializeLogo() {
        Image logoImage = new Image(ResourceLoader.load("logo.png"), 32, 32, true, true);
        ImageView logoView = new ImageView(logoImage);
        Circle clip = new Circle(30);
        clip.centerXProperty().bind(logoView.layoutBoundsProperty().map(Bounds::getCenterX));
        clip.centerYProperty().bind(logoView.layoutBoundsProperty().map(Bounds::getCenterY));
        logoView.setClip(clip);
        logoContainer.getChildren().add(logoView);
    }

    private void initializeZSoft() {
        zsoftLogo.addEventHandler(MouseEvent.MOUSE_ENTERED, _ -> zsoftLogo.getScene().setCursor(Cursor.HAND));
        zsoftLogo.addEventHandler(MouseEvent.MOUSE_EXITED, _ -> zsoftLogo.getScene().setCursor(Cursor.DEFAULT));
        zsoftLogo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;
            hostServices.showDocument(ZSOFT_URL);
        });
    }

    private void loadController() throws IOException {
        Pair<Node, Object> loaderData = FxmlLoaderUtils.loadView("fxml/details-view.fxml", applicationContext);
        contentPane.getChildren().clear();
        contentPane.setCenter(loaderData.getKey());

        detailsViewController = (DetailsViewController) loaderData.getValue();
    }

    private void loadPage(NavbarItem navbarItem) {
        detailsViewController.initController(navbarItem, null);
    }

    private void setupGlobalListener() {
        GlobalEventEmitter.registerListener(this.getClass().getName(), new EventListener() {
            @Override
            public void onEvent(Event event) {
                TodoListChangedEvent realEvent = (TodoListChangedEvent) event;
                buildNavbar(realEvent.listId());
            }

            @Override
            public boolean supports(Event event) {
                return event instanceof TodoListChangedEvent;
            }
        });
    }

}
