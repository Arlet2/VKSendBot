import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import auth.AuthData;
import auth.AuthDataReader;
import services.NameConvertorService;
import services.SendService;
import ui.ConsoleUI;
import ui.UI;


import java.io.FileNotFoundException;

public class Application {
    private static AuthData authData;

    private static VkApiClient api;
    private static GroupActor groupActor;

    private static SendService sendService;

    public static void main(String[] args) {

        readAuthInfo();

        initApi();

        initServices();

        UI ui = new ConsoleUI(sendService);

        ui.startInteraction();
    }

    private static void readAuthInfo() {
        try {
            authData = AuthDataReader.readAuthData("auth.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void initApi() {
        TransportClient transportClient = new HttpTransportClient();

        api = new VkApiClient(transportClient);

        groupActor = new GroupActor(authData.getGroupId(), authData.getToken());
    }

    private static void initServices() {
        NameConvertorService nameConvertorService = new NameConvertorService(api, groupActor);
        sendService = new SendService(api, groupActor, nameConvertorService);
    }

}