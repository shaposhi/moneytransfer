package edu.shapo.exprs;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.shapo.exprs.api.AccountController;
import edu.shapo.exprs.api.MoneyTransferApi;
import edu.shapo.exprs.api.MoneyTransferController;
import edu.shapo.exprs.api.StatisticController;
import edu.shapo.exprs.configuration.AppModule;


public class App {
    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new AppModule());

        MoneyTransferController moneyTransferController = injector.getInstance(MoneyTransferController.class);
        StatisticController statisticController = injector.getInstance(StatisticController.class);
        AccountController accountController = injector.getInstance(AccountController.class);
        new MoneyTransferApi(moneyTransferController, statisticController, accountController);


    }
}