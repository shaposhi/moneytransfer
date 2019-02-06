package edu.shapo.exprs.unit.service

import edu.shapo.exprs.dao.AccountDao
import edu.shapo.exprs.dao.AccountDaoImpl
import edu.shapo.exprs.model.Account
import edu.shapo.exprs.service.AccountServiceImpl
import spock.lang.Shared
import spock.lang.Specification

class AccountServiceSpec extends Specification {

    AccountServiceImpl accountService

    @Shared
    AccountDao accountDao

    def setupSpec() {
        accountDao = new AccountDaoImpl()
    }

    def setup() {
        accountService = new AccountServiceImpl()
        accountService.accountDao = accountDao
    }

    def cleanupSpec() {
        accountDao.closeConnection()
    }

    def "test getting all account"() {
        given:

        when:
        List<Account> result = accountDao.getAllAccounts()

        then:
        result
        result.size() == 11
    }

    def "test that we can get acount by Id"() {
        given:

        when:
        Optional<Account> result = accountDao.findById(3L)

        then:
        result.isPresent()
        result.get().id == 3
        result.get().currentAmout >= 0
    }

    def "test that we get null if acccount not exist"() {
        given:

        when:
        Optional<Account>  result = accountDao.findById(33L)

        then:
        !result.isPresent()
    }

}
