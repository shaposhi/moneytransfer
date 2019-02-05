package edu.shapo.exprs.unit.validation;

import edu.shapo.exprs.to.TransferRequestTO;

public class SyntaxValidationResult {
    private boolean isValid;
    private String errorMessage;
    private TransferRequestTO requestTO;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TransferRequestTO getRequestTO() {
        return requestTO;
    }

    public void setRequestTO(TransferRequestTO requestTO) {
        this.requestTO = requestTO;
    }
}
