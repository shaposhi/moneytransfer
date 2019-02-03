package edu.shapo.exprs.model;

public class TransferStatus {
    private String status;
    private String message;
    private ErrorCode errorCode;

    public TransferStatus(String status, String message, ErrorCode errorCode) {
        setStatus(status);
        setMessage(message);
        setErrorCode(errorCode);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
