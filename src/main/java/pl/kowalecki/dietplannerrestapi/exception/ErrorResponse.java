package pl.kowalecki.dietplannerrestapi.exception;

public class ErrorResponse {
    private String message;
    private String errorType;
    private int statusCode;
    private String serviceName;
    private String requestedUrl;
    private String timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRequestedUrl() {
        return requestedUrl;
    }

    public void setRequestedUrl(String requestedUrl) {
        this.requestedUrl = requestedUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ErrorResponse(String message, String errorType, int statusCode, String serviceName, String requestedUrl, String timestamp) {
        this.message = message;
        this.errorType = errorType;
        this.statusCode = statusCode;
        this.serviceName = serviceName;
        this.requestedUrl = requestedUrl;
        this.timestamp = timestamp;
    }
}