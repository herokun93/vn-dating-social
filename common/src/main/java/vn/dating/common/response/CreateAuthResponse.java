package vn.dating.common.response;

public class CreateAuthResponse {
    private String message;
    private String service;

    public CreateAuthResponse() {
    }

    public CreateAuthResponse(String message, String service) {
        this.message = message;
        this.service = service;
    }

    @Override
    public String toString() {
        return "CreateAuthResponse{" +
                "message='" + message + '\'' +
                ", service='" + service + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
