package edu.shapo.exprs.configuration;

import com.google.inject.AbstractModule;
import edu.shapo.exprs.api.MoneyTransferApiHandler;
import edu.shapo.exprs.dao.AccountDao;
import edu.shapo.exprs.dao.AccountDaoImpl;
import edu.shapo.exprs.service.TransferService;
import edu.shapo.exprs.service.TransferServiceImpl;
import edu.shapo.exprs.validation.RequestSyntaxValidator;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TransferService.class).toInstance(new TransferServiceImpl());
        bind(AccountDao.class).toInstance(new AccountDaoImpl());
        bind(MoneyTransferApiHandler.class).toInstance(new MoneyTransferApiHandler());
        bind(RequestSyntaxValidator.class).toInstance(new RequestSyntaxValidator());
    }
}
