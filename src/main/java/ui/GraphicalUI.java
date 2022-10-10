package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import services.SendService;

public class GraphicalUI extends UI {
    public GraphicalUI(SendService sendService) {
        super(sendService);
    }

    @Override
    public void startInteraction() {
        Application application = new Application() {
            @Override
            public void start(Stage stage) throws Exception {
                stage.show();
            }
        };
    }
}
