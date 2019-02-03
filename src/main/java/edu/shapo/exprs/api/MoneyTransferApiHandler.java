package edu.shapo.exprs.api;

import com.google.gson.Gson;
import com.google.inject.Inject;
import edu.shapo.exprs.exception.MoneyTransferException;
import edu.shapo.exprs.model.Constant;
import edu.shapo.exprs.model.ErrorCode;
import edu.shapo.exprs.model.TransferStatus;
import edu.shapo.exprs.service.TransferService;
import edu.shapo.exprs.to.TransferRequestTO;
import edu.shapo.exprs.to.TransferResponseTO;
import edu.shapo.exprs.validation.RequestSyntaxValidator;
import edu.shapo.exprs.validation.SyntaxValidationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

public class MoneyTransferApiHandler {

    private static final Logger log = LogManager.getLogger(MoneyTransferApiHandler.class);

    @Inject
    private TransferService transferService;

    @Inject
    private RequestSyntaxValidator requestSyntaxValidator;


    public String handleTransferRequest(Request request, Response response) {
        response.type("application/json");
        TransferRequestTO requestTO = null;

        TransferStatus transferStatus;

        SyntaxValidationResult validationResult = requestSyntaxValidator.validate(request);
        if (!validationResult.isValid()) {
            return new Gson().toJson(new TransferResponseTO(Constant.TRANSFER_FAIL, validationResult.getErrorMessage()));
        }

        requestTO = validationResult.getRequestTO();

        try {
            transferStatus = transferService.makeTransfer(
                    requestTO.getSourceAccountId(), requestTO.getTargetAccountId(),
                    requestTO.getTransferAmount(), requestTO.getInitiator());


        } catch (MoneyTransferException mte) {
            ErrorCode errorCode = ErrorCode.valueOf(mte.getMessage());

            return new Gson().toJson(new TransferResponseTO(Constant.TRANSFER_FAIL,
                    "Error: " + errorCode.name() + " " + errorCode.getDescription()));
        } catch (Exception e) {
            return new Gson().toJson(new TransferResponseTO(Constant.TRANSFER_FAIL, e.getMessage()));
        }


        return new Gson().toJson(new TransferResponseTO(transferStatus.getStatus(), transferStatus.getMessage()));

    }



    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }

    public void setRequestSyntaxValidator(RequestSyntaxValidator requestSyntaxValidator) {
        this.requestSyntaxValidator = requestSyntaxValidator;
    }
}
