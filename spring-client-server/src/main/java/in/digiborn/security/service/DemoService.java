package in.digiborn.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import in.digiborn.security.external.SpringResourceServerClientApi;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final SpringResourceServerClientApi resourceServerClientApi;

    public String demo() {
        return resourceServerClientApi.demo();
    }
}
