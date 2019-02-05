package edu.shapo.exprs.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import edu.shapo.exprs.api.AccountController;
import edu.shapo.exprs.api.StatisticController;
import edu.shapo.exprs.api.MoneyTransferController;
import edu.shapo.exprs.dao.AccountDao;
import edu.shapo.exprs.dao.AccountDaoImpl;
import edu.shapo.exprs.service.*;
import edu.shapo.exprs.validation.RequestSyntaxValidator;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TransferService.class).to(TransferServiceImpl.class).in(Singleton.class);
        bind(AccountDao.class).to(AccountDaoImpl.class).in(Singleton.class);
        bind(AccountService.class).to(AccountServiceImpl.class).in(Singleton.class);
        bind(TransactionLogService.class).to(TransactionLogServiceImpl.class).in(Singleton.class);
        bind(MoneyTransferController.class).in(Singleton.class);
        bind(AccountController.class).in(Singleton.class);
        bind(StatisticController.class).in(Singleton.class);
        bind(RequestSyntaxValidator.class).in(Singleton.class);

    }
}
