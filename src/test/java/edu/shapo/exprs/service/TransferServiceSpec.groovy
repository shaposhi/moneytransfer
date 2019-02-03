package edu.shapo.exprs.service

import edu.shapo.exprs.dao.AccountDao
import edu.shapo.exprs.dao.AccountDaoImpl
import edu.shapo.exprs.exception.MoneyTransferException
import edu.shapo.exprs.model.Constant
import edu.shapo.exprs.model.ErrorCode
import edu.shapo.exprs.model.TransferStatus
import spock.lang.Specification

class TransferServiceSpec extends Specification {

    TransferServiceImpl transferService = new TransferServiceImpl()

    def setup() {
        AccountDao accountDao = new AccountDaoImpl()
        transferService.accountDao = accountDao
    }

    def "check that exception thrown if incorrect source ID"(){

        given:

        when:
        TransferStatus result = transferService.makeTransfer(20L, 10L, new BigDecimal(50), "John Doe")

        then:
        !result
        MoneyTransferException mte = thrown()
        mte.message.contains(ErrorCode.ERROR_002.name())


    }

    def "check that exception thrown if incorrect target ID"(){

        given:

        when:
        TransferStatus result = transferService.makeTransfer(10L, 33L, new BigDecimal(50), "John Doe")

        then:
        !result
        MoneyTransferException mte = thrown()
        mte.message.contains(ErrorCode.ERROR_003.name())

    }

    def "check that exception thrown if we transfer more money than have"(){

        given:

        when:
        TransferStatus result = transferService.makeTransfer(10L, 3L, new BigDecimal(5000000), "John Doe")

        then:
        !result
        MoneyTransferException mte = thrown()
        mte.message.contains(ErrorCode.ERROR_004.name())

    }

    def "check for correct flow process"(){

        given:

        when:
        TransferStatus result = transferService.makeTransfer(10L, 3L, new BigDecimal(50), "John Doe")

        then:
        result
        result.status == Constant.TRANSFER_SUCCESSFUL
        result.message == "transfer successful"
    }

}
