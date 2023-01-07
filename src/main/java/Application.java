import auth.AuthData;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import services.SendService;
import ui.ConsoleUI;
import ui.UI;
import utils.files.file_readers.AuthJsonReader;

import java.io.FileNotFoundException;

public class Application {

    private static VkApiClient api;

    private static SendService sendService;

    public static void main(String[] args) {
        initApi();

        initServices();

        UI ui = new ConsoleUI(sendService);

        ui.startInteraction();
    }


    private static void initApi() {
        TransportClient transportClient = new HttpTransportClient();

        api = new VkApiClient(transportClient);

    }

    private static void initServices() {
        sendService = new SendService(api);

        AuthData authData = null;
        try {
            authData = new AuthJsonReader().read("auth.json");
        } catch (FileNotFoundException e) {
            System.out.println("Файл auth.json не был найден!");
            System.exit(1);
        }

        sendService.changeGroupActor(new GroupActor(authData.getGroupId(), authData.getToken())); // temp
    }

}