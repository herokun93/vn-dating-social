package vn.dating.keycloak.event;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class SampleEventListenerProviderFactory implements EventListenerProviderFactory {
    public SampleEventListenerProviderFactory() {
    }

    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new SampleEventListenerProvider(keycloakSession);
    }

    public void init(Config.Scope scope) {
    }

    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
    }

    public void close() {
    }

    public String getId() {
        return "vn-dating_event_listener";
    }
}
