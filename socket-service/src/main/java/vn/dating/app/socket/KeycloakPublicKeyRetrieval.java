package vn.dating.app.socket;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class KeycloakPublicKeyRetrieval {

    public static void main(String[] args) {
        String keycloakBaseUrl = "http://localhost:8181";
        String realm = "dating-chat-oauth"; // Replace with your Keycloak realm

        String publicKeyEndpoint = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/certs";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = new RestTemplate().exchange(
                publicKeyEndpoint,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Public Key: " + response.getBody());
        } else {
            System.out.println("Failed to retrieve public key. Status code: " + response.getStatusCodeValue());
        }
    }
}

