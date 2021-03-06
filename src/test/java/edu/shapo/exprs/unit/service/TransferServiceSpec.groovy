package edu.shapo.exprs.unit.service

import edu.shapo.exprs.exception.MoneyTransferException
import edu.shapo.exprs.model.Account
import edu.shapo.exprs.model.ErrorCode
import edu.shapo.exprs.model.TransferStatus
import edu.shapo.exprs.model.TransferStatusCode
import edu.shapo.exprs.service.AccountService
import edu.shapo.exprs.service.AuditTransactionLogService
import edu.shapo.exprs.service.TransferServiceImpl
import spock.lang.Specification

class TransferServiceSpec extends Specification {

    TransferServiceImpl transferService = new TransferServiceImpl()

    AccountService accountService = Mock(AccountService)

    AuditTransactionLogService transactionLogService = Mock(AuditTransactionLogService)

    def setup() {
        transferService.accountService = accountService
        transferService.auditTransactionLogService = transactionLogService
    }

    def "check that exception thrown if incorrect source ID"() {
        given:
        accountService.findById(2L) >> new Account(id: 2, currentAmout: new BigDecimal(100))
        accountService.findById(20L) >> null

        when:
        TransferStatus result = transferService.makeTransfer(20L, 2L, new BigDecimal(50), "John Doe")

        then:
        !result
        MoneyTransferException mte = thrown()
        mte.message.contains(ErrorCode.ERROR_002.name())
    }

    def "check that exception thrown if incorrect target ID"() {
        given:
        accountService.findById(1L) >> new Account(id: 1, currentAmout: new BigDecimal(100))
        accountService.findById(33L) >> null

        when:
        TransferStatus result = transferService.makeTransfer(1L, 33L, new BigDecimal(50), "John Doe")

        then:
        !result
        MoneyTransferException mte = thrown()
        mte.message.contains(ErrorCode.ERROR_003.name())
    }

    def "check that exception thrown if we transfer more money than have"() {
        given:
        accountService.findById(1L) >> new Account(id: 1, currentAmout: new BigDecimal(100))
        accountService.findById(2L) >> new Account(id: 2, currentAmout: new BigDecimal(100))

        when:
        TransferStatus result = transferService.makeTransfer(1L, 2L, new BigDecimal(5000000), "John Doe")

        then:
        !result
        MoneyTransferException mte = thrown()
        mte.message.contains(ErrorCode.ERROR_004.name())
    }

    def "check for correct flow process"() {
        given:
        accountService.findById(1L) >> new Account(id: 1, currentAmout: new BigDecimal(100))
        accountService.findById(2L) >> new Account(id: 2, currentAmout: new BigDecimal(100))

        when:
        TransferStatus result = transferService.makeTransfer(1L, 2L, new BigDecimal(50), "John Doe")

        then:
        result
        result.status == TransferStatusCode.SUCCESSFUL
        result.message == "transfer successful"
    }

}
