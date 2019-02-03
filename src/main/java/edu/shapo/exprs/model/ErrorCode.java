package edu.shapo.exprs.model;

public enum ErrorCode {

    ERROR_001("So sense operation, transfer for same account"),
    ERROR_002("Incorrect source account"),
    ERROR_003("Incorrect destination account"),
    ERROR_004("Insufficient amount on source account"),

    ERROR_010("Syntax error for Source account ID"),
    ERROR_011("Syntax error for Destination account ID"),
    ERROR_012("Syntax error for Amount value"),
    ERROR_013("Syntax error for Initiator value"),
    ERROR_014("Amount scale could not be more that 2"),



    ERROR_100("Error parsing request");


    ErrorCode(String desc) {
        this.description = desc;
    }

    private String description;

    public String getDescription() {
        return description;
    }
}
