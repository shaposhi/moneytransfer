package edu.shapo.exprs.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import edu.shapo.exprs.exception.MoneyTransferException;
import edu.shapo.exprs.model.Constant;
import edu.shapo.exprs.model.ErrorCode;
import edu.shapo.exprs.model.TransferStatus;
import edu.shapo.exprs.service.TransferService;
import edu.shapo.exprs.to.TransferRequestTO;
import edu.shapo.exprs.to.TransferResponseTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.util.StringUtils;
import spark.Request;
import spark.Response;
import spark.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MoneyTransferApiHandler {

    private static final Logger log = LogManager.getLogger(MoneyTransferApiHandler.class);

    @Inject
    private TransferService transferService;


    public String handleTransferRequest(Request request, Response response) {
        response.type("application/json");
        TransferRequestTO requestTO = null;
        List<ErrorCode> syntaxErrors;
        TransferStatus transferStatus;

        try {
            requestTO = new Gson().fromJson(request.body(), TransferRequestTO.class);
        } catch (JsonSyntaxException jse) {
            log.error("Request parse error", jse);
            return new Gson().toJson(new TransferResponseTO(Constant.TRANSFER_FAIL, jse.getMessage()));
        }


        syntaxErrors = validateRequest(requestTO);
        if (!CollectionUtils.isEmpty(syntaxErrors)) {
            StringJoiner sj = new StringJoiner(";");
            syntaxErrors.forEach(se -> sj.add("Error: " + se.name() + " " + se.getDescription()));

            return new Gson().toJson(new TransferResponseTO(Constant.TRANSFER_FAIL, sj.toString()));
        }

        try {
            transferStatus = transferService.makeTransfer(
                    requestTO.getSourceAccountId(), requestTO.getTargetAccountId(),
                    requestTO.getTransferAmount(), requestTO.getInitiator());


        } catch (MoneyTransferException mte) {
            ErrorCode errorCode = ErrorCode.valueOf(mte.getMessage());

            return new Gson().toJson(new TransferResponseTO(Constant.TRANSFER_FAIL,
                    "Error: " + errorCode.name() + " " + errorCode.getDescription()));
        }


        return new Gson().toJson(new TransferResponseTO(transferStatus.getStatus(), transferStatus.getMessage()));

    }

    private List<ErrorCode> validateRequest(TransferRequestTO requestTO) {
        List<ErrorCode> results = new ArrayList<>();
        if (requestTO != null) {
            if (requestTO.getSourceAccountId() <= 0L) {
                results.add(ErrorCode.ERROR_010);
            }
            if (requestTO.getTargetAccountId() <= 0L) {
                results.add(ErrorCode.ERROR_011);
            }
            if (requestTO.getTransferAmount() == null || requestTO.getTransferAmount().longValue() <= 0L) {
                results.add(ErrorCode.ERROR_012);
            }
            if (StringUtils.isNullOrEmpty(requestTO.getInitiator())) {
                results.add(ErrorCode.ERROR_013);
            }
            if (requestTO.getTransferAmount().scale() > 2 ) {
                results.add(ErrorCode.ERROR_014);
            }


        } else {
            results.add(ErrorCode.ERROR_100);
        }
        return results;
    }

    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }
}
