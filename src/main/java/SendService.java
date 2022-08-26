import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import order.Order;
import utils.ReportService;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class SendService {
    private final VkApiClient api;
    private final GroupActor groupActor;
    private final ReportService reportService;
    private final NameConvertor nameConvertor;

    private int currentSendCount = 0;

    SendService(VkApiClient api, GroupActor groupActor, NameConvertor nameConvertor) {
        this.api = api;
        this.groupActor = groupActor;
        this.nameConvertor = nameConvertor;
        this.reportService = new ReportService();
    }

    public void executeSendOrder(Order order) {
        currentSendCount = 0;

        reportService.createNewReport();

        Integer[] ids = convertNamesToIds(order.getNames());

        Arrays.stream(ids).parallel().forEach((id) -> {
            sendToUser(id, order.getMsg());
        });

        reportService.addMessageToReport(currentSendCount + "/" + ids.length + " is successfully sent");

        reportService.finalReport();
    }

    private Integer[] convertNamesToIds(String[] names) {
        return (Integer[])
                Arrays.stream(names)
                        .map((name) -> {
                            try {
                                return nameConvertor.convertNameToId(name);
                            } catch (ClientException | ApiException e) {
                                e.printStackTrace();
                                reportService.addMessageToReport(
                                        "Error to convert name: " + name + " Details: " + e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toArray();
    }

    private void sendToUser(int id, String msg) {
        try {
            api.messages().send(groupActor)
                    .message(msg)
                    .userId(id)
                    .randomId(new Random().nextInt())
                    .execute();
            currentSendCount++;
        } catch (ClientException | ApiException e) {
            reportService.addMessageToReport("troubles with: " + id + ". Details:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
