package services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.utils.responses.ResolveScreenNameResponse;

public class NameConvertorService {
    private final VkApiClient api;
    private GroupActor groupActor;

    public NameConvertorService(VkApiClient api) {
        this.api = api;
    }

    public int convertNameToId(String name) throws ClientException, ApiException {
        ResolveScreenNameResponse response = api.utils().resolveScreenName(groupActor, name).execute();
        return response.getObjectId();
    }

    public void changeGroupActor(GroupActor groupActor) {
        this.groupActor = groupActor;
    }
}
