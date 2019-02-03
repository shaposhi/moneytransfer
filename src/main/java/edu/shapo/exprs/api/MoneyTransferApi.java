package edu.shapo.exprs.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.*;

public class MoneyTransferApi {

    private static final Logger log = LogManager.getLogger(MoneyTransferApi.class);

    private MoneyTransferApiHandler moneyTransferApiHandler;

    public MoneyTransferApi(MoneyTransferApiHandler moneyTransferApiHandler) {
        setMoneyTransferApiHandler(moneyTransferApiHandler);
        setupRoutes();
    }


    private void setupRoutes() {
        log.info("Setting routes");
        log.info(moneyTransferApiHandler);
        port(8080);
        path("/bank/api", () -> {
            before("/*", (req, res) -> log.info("Received bank api call " + req.body() + " " + res));

            path("/transfer", () -> {
                post("/make", ((request, response) -> moneyTransferApiHandler.handleTransferRequest(request, response)));
            });

        });
    }


    public void setMoneyTransferApiHandler(MoneyTransferApiHandler moneyTransferApiHandler) {
        this.moneyTransferApiHandler = moneyTransferApiHandler;
    }
}





