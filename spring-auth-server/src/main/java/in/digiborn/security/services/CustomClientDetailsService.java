package in.digiborn.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import in.digiborn.security.entities.Client;
import in.digiborn.security.repositories.ClientRepository;

@Service
@RequiredArgsConstructor
public class CustomClientDetailsService implements RegisteredClientRepository {

    private final ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(Client.from(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return clientRepository.findById(Integer.parseInt(id))
            .map(Client::from)
            .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
            .map(Client::from)
            .orElse(null);
    }
}
