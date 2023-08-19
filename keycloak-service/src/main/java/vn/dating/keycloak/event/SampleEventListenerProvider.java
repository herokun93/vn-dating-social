package vn.dating.keycloak.event;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

public class SampleEventListenerProvider implements EventListenerProvider {
    private static final Logger log = LoggerFactory.getLogger(SampleEventListenerProvider.class);
    private final KeycloakSession keycloakSession;

    public SampleEventListenerProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    public void onEvent(Event event) {
        log.info("TYPE->>>>>>>>>>>" + event.getType());
    }

    public void onEvent(AdminEvent adminEvent, boolean b) {
    }

    public void close() {
    }

    private String toString(Event event) {
        StringBuilder sb = new StringBuilder();
        sb.append("type=");
        sb.append(event.getType());
        sb.append(", realmId=");
        sb.append(event.getRealmId());
        sb.append(", clientId=");
        sb.append(event.getClientId());
        sb.append(", userId=");
        sb.append(event.getUserId());
        sb.append(", ipAddress=");
        sb.append(event.getIpAddress());
        if (event.getError() != null) {
            sb.append(", error=");
            sb.append(event.getError());
        }

        if (event.getDetails() != null) {
            Iterator var3 = event.getDetails().entrySet().iterator();

            while(true) {
                while(var3.hasNext()) {
                    Map.Entry<String, String> e = (Map.Entry)var3.next();
                    sb.append(", ");
                    sb.append((String)e.getKey());
                    if (e.getValue() != null && ((String)e.getValue()).indexOf(32) != -1) {
                        sb.append("='");
                        sb.append((String)e.getValue());
                        sb.append("'");
                    } else {
                        sb.append("=");
                        sb.append((String)e.getValue());
                    }
                }

                return sb.toString();
            }
        } else {
            return sb.toString();
        }
    }

    private String toString(AdminEvent adminEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("operationType=");
        sb.append(adminEvent.getOperationType());
        sb.append(", realmId=");
        sb.append(adminEvent.getAuthDetails().getRealmId());
        sb.append(", clientId=");
        sb.append(adminEvent.getAuthDetails().getClientId());
        sb.append(", userId=");
        sb.append(adminEvent.getAuthDetails().getUserId());
        sb.append(", ipAddress=");
        sb.append(adminEvent.getAuthDetails().getIpAddress());
        sb.append(", resourcePath=");
        sb.append(adminEvent.getResourcePath());
        if (adminEvent.getError() != null) {
            sb.append(", error=");
            sb.append(adminEvent.getError());
        }

        return sb.toString();
    }
}
