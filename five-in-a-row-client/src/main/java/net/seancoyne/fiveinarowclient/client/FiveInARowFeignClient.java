package net.seancoyne.fiveinarowclient.client;

import net.seancoyne.fiveinarowclient.model.request.CreateGameRequest;
import net.seancoyne.fiveinarowclient.model.response.CreateGameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "FiveInARowFeignClient", url = "http://localhost:8080")
public interface FiveInARowFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/createGame", consumes = "application/json", produces = "application/json")
    public CreateGameResponse createGame(CreateGameRequest request);
}
