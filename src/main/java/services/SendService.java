package services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import order.Order;
import utils.ReportService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class SendService {
    private final VkApiClient api;
    private GroupActor groupActor;
    private final ReportService reportService = new ReportService();
    private final NameConvertorService nameConvertorService;

    private int currentSendCount = 0;

    public SendService(VkApiClient api) {
        this.api = api;
        nameConvertorService = new NameConvertorService(api);
    }

    public void executeSendOrder(Order order) {
        currentSendCount = 0;

        reportService.createNewReport();

        List<Integer> ids = convertNamesToIds(order.getNames());

        ids.parallelStream()
                .forEach(
                        (id) -> sendToUser(id, order.getMsg())
                );

        reportService.addMessageToReport(currentSendCount + "/" + order.getNames().length +
                " is successfully sent");

        reportService.finalReport();

        System.out.println("Report is ready!");
    }

    private List<Integer> convertNamesToIds(String[] names) {
        return Arrays.stream(names)
                .map((name) -> {
                    try {
                        return nameConvertorService.convertNameToId(name);
                    } catch (ClientException | ApiException e) {
                        reportService.addMessageToReport(
                                "ERROR WITH NAME CONVERTING: " + name + " Details: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
            reportService.addMessageToReport("TROUBLES WITH SENDING: " + id + ". Details: " + e.getMessage());
        }
    }

    public void changeGroupActor(GroupActor groupActor) {
        this.groupActor = groupActor;

        nameConvertorService.changeGroupActor(groupActor);
    }
}
