package edu.shapo.exprs;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.shapo.exprs.api.MoneyTransferApi;
import edu.shapo.exprs.api.MoneyTransferApiHandler;
import edu.shapo.exprs.configuration.AppModule;



public class App {
    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new AppModule());

        MoneyTransferApiHandler moneyTransferApiHandler = injector.getInstance(MoneyTransferApiHandler.class);
        new MoneyTransferApi(moneyTransferApiHandler);


    }
}