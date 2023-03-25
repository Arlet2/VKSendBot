import auth.AuthData;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import services.SendService;
import ui.GraphicalUI;
import ui.UI;
import utils.file_readers.AuthJsonReader;

import java.io.FileNotFoundException;

public class Application {

    private static VkApiClient api;

    private static SendService sendService;

    public static void main(String[] args) {
        initApi();

        initServices();

        UI ui = new GraphicalUI(sendService);//new ConsoleUI(sendService);

        ui.startInteraction();
    }


    private static void initApi() {
        TransportClient transportClient = new HttpTransportClient();

        api = new VkApiClient(transportClient);

    }

    private static void initServices() {
        sendService = new SendService(api);

        try {
            AuthData authData = new AuthJsonReader().read("auth.json");
            sendService.changeGroupActor(authData.getGroupId(), authData.getToken());
        } catch (FileNotFoundException e) {
            System.out.println("Файл auth.json не был найден! Добавьте группу для использования бота");
        }
    }

}