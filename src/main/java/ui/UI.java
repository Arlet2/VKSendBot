package ui;

import services.SendService;

public abstract class UI {

    protected SendService sendService;

    UI(SendService sendService) {
        this.sendService = sendService;
    }

    public abstract void startInteraction();
}
