package edu.shapo.exprs.model;

public class TransferStatus {
    private TransferStatusCode status;
    private String message;
    private ErrorCode errorCode;

    public TransferStatus(TransferStatusCode status, String message, ErrorCode errorCode) {
        setStatus(status);
        setMessage(message);
        setErrorCode(errorCode);
    }

    public TransferStatusCode getStatus() {
        return status;
    }

    public void setStatus(TransferStatusCode status) {
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
