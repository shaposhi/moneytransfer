package edu.shapo.exprs.validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import edu.shapo.exprs.model.ErrorCode;
import edu.shapo.exprs.to.TransferRequestTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.util.StringUtils;
import spark.Request;
import spark.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class RequestSyntaxValidator {

    private static final Logger log = LogManager.getLogger(RequestSyntaxValidator.class);

    public SyntaxValidationResult validate(Request request) {
        List<ErrorCode> syntaxErrors;
        SyntaxValidationResult result = new SyntaxValidationResult();
        result.setValid(true);

        TransferRequestTO requestTO;
        try {
            requestTO = new Gson().fromJson(request.body(), TransferRequestTO.class);
            result.setRequestTO(requestTO);
        } catch (JsonSyntaxException jse) {
            log.error("Request parse error", jse);
            result.setValid(false);
            result.setErrorMessage(jse.getMessage());
            return result;
        }


        syntaxErrors = validateRequest(requestTO);

        if (!CollectionUtils.isEmpty(syntaxErrors)) {
            result.setValid(false);
            StringJoiner sj = new StringJoiner(";");
            syntaxErrors.forEach(se -> sj.add("Error: " + se.name() + " " + se.getDescription()));

            result.setErrorMessage(sj.toString());
            return result;
        }


        return result;
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
            if (requestTO.getTransferAmount() != null && requestTO.getTransferAmount().scale() > 2 ) {
                results.add(ErrorCode.ERROR_014);
            }

        } else {
            results.add(ErrorCode.ERROR_100);
        }
        return results;
    }

}
