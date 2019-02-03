package edu.shapo.exprs.api

import com.google.gson.Gson
import edu.shapo.exprs.exception.MoneyTransferException
import edu.shapo.exprs.model.Constant
import edu.shapo.exprs.model.ErrorCode
import edu.shapo.exprs.model.TransferStatus
import edu.shapo.exprs.service.TransferService
import edu.shapo.exprs.to.TransferRequestTO
import edu.shapo.exprs.to.TransferResponseTO
import edu.shapo.exprs.validation.RequestSyntaxValidator
import edu.shapo.exprs.validation.SyntaxValidationResult
import spark.Request
import spark.Response
import spock.lang.Specification

class MoneyTransferApiHanplerSpec extends Specification {


    TransferService transferService = Mock(TransferService.class)
    RequestSyntaxValidator validator = Mock(RequestSyntaxValidator.class)

    MoneyTransferApiHandler handler = new MoneyTransferApiHandler()

    def setup() {
        handler.transferService = transferService
        handler.requestSyntaxValidator = validator
    }

    def "check that we handle incorrect request"(){

        given:
        Response response = Mock(Response.class)
        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(new TransferRequestTO())
        validator.validate(inputRequst) >> new SyntaxValidationResult(isValid: false, errorMessage: "Error")

        when:
        String res = handler.handleTransferRequest(inputRequst, response)
        TransferResponseTO result = new Gson().fromJson(res, TransferResponseTO.class)

        then:
        result
        result.status == Constant.TRANSFER_FAIL
        result.message.contains("Error")

    }

    def "check that we handle error during transfer"(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 5L, targetAccountId: 7L, transferAmount: new BigDecimal(100), initiator: "John")
        Response response = Mock(Response.class)
        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)
        validator.validate(inputRequst) >> new SyntaxValidationResult(isValid: true, requestTO: transferRequestTO)
        transferService.makeTransfer(5L, 7L, new BigDecimal(100), "John") >> {throw new MoneyTransferException("ERROR_004")}


        when:
        String res = handler.handleTransferRequest(inputRequst, response)
        TransferResponseTO result = new Gson().fromJson(res, TransferResponseTO.class)

        then:
        result
        result.status == Constant.TRANSFER_FAIL
        result.message.contains(ErrorCode.ERROR_004.description)

    }

    def "check that we handle correct transfer"(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 5L, targetAccountId: 7L, transferAmount: new BigDecimal(100), initiator: "John")
        Response response = Mock(Response.class)
        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)
        validator.validate(inputRequst) >> new SyntaxValidationResult(isValid: true, requestTO: transferRequestTO)
        transferService.makeTransfer(5L, 7L, new BigDecimal(100), "John") >>
                new TransferStatus(Constant.TRANSFER_SUCCESSFUL, "transfer successful", null)


        when:
        String res = handler.handleTransferRequest(inputRequst, response)
        TransferResponseTO result = new Gson().fromJson(res, TransferResponseTO.class)

        then:
        result
        result.status == Constant.TRANSFER_SUCCESSFUL
        result.message.contains("transfer successful")

    }

}
