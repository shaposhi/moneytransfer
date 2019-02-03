package edu.shapo.exprs.validation

import com.google.gson.Gson
import edu.shapo.exprs.model.ErrorCode
import edu.shapo.exprs.to.TransferRequestTO
import spark.Request
import spock.lang.Specification

class RequestSyntaxValidatorSpec extends Specification {

    RequestSyntaxValidator validator = new RequestSyntaxValidator()

    def setup() {
    }

    def "check for correct source ID"(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: -1L, targetAccountId: 7L, transferAmount: new BigDecimal(100), initiator: "John")

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage.contains(ErrorCode.ERROR_010.description)

    }

    def "check for correct dest ID"(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 4L, targetAccountId: -74L, transferAmount: new BigDecimal(100), initiator: "John")

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage.contains(ErrorCode.ERROR_011.description)

    }

    def "check for correct amount "(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 4L, targetAccountId: 5L, transferAmount: null, initiator: "John")

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage.contains(ErrorCode.ERROR_012.description)

    }

    def "check for correct amount 2 "(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 4L, targetAccountId: 5L, transferAmount: new BigDecimal("0.10"), initiator: "John")

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        result.valid


    }

    def "check for correct amount negative "(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 4L, targetAccountId: 5L, transferAmount: new BigDecimal(-100), initiator: "John")

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage.contains(ErrorCode.ERROR_012.description)

    }

    def "check for correct initiator "(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 4L, targetAccountId: 2L, transferAmount: new BigDecimal(100), initiator: null)

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage.contains(ErrorCode.ERROR_013.description)

    }

    def "check for correct amount scale "(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 4L, targetAccountId: 1L, transferAmount: new BigDecimal(100.001), initiator: null)

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage.contains(ErrorCode.ERROR_014.description)

    }

    def "check for more that one error "(){

        given:
        TransferRequestTO transferRequestTO = new  TransferRequestTO(sourceAccountId: 4L, targetAccountId: -0L, transferAmount: new BigDecimal(100.001), initiator: null)

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage.contains(ErrorCode.ERROR_014.description)
        result.errorMessage.contains(ErrorCode.ERROR_011.description)

    }

    def "check for null input "(){

        given:
        TransferRequestTO transferRequestTO = null

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson(transferRequestTO)

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage.contains(ErrorCode.ERROR_100.description)

    }

    def "check for incorrect body "(){

        given:

        Request inputRequst = Mock(Request.class)
        inputRequst.body() >> new Gson().toJson("foo and bar")

        when:
        SyntaxValidationResult result = validator.validate(inputRequst)

        then:
        result
        !result.valid
        result.errorMessage
        result.errorMessage.contains("Expected")

    }
}
