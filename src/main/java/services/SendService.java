package services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import order.Order;
import utils.ReportService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class SendService {
    private final VkApiClient api;
    private GroupActor groupActor = null;
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
                " успешно отправлены");

        reportService.finalReport();

        System.out.println("Отчёт готов!");
    }

    private List<Integer> convertNamesToIds(String[] names) {
        return Arrays.stream(names)
                .map((name) -> {
                    try {
                        return nameConvertorService.convertNameToId(name);
                    } catch (ClientException | ApiException e) {
                        reportService.addMessageToReport(
                                "Ошибка с конвертацией имени: " + name + " Детали: " + e.getMessage());
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
            reportService.addMessageToReport("Проблемы с отправкой к : " + id + ". Детали: " + e.getMessage());
        }
    }

    public void changeGroupActor(int groupId, String token) {
        groupActor = new GroupActor(groupId, token);
        nameConvertorService.changeGroupActor(groupActor);
    }

    public GroupActor getGroupActor() {
        return groupActor;
    }
}
