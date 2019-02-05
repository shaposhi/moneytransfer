package edu.shapo.exprs.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.*;

public class MoneyTransferApi {

    private static final Logger log = LogManager.getLogger(MoneyTransferApi.class);

    private MoneyTransferController moneyTransferController;

    private StatisticController statisticController;

    private AccountController accountController;

    public MoneyTransferApi(MoneyTransferController moneyTransferController, StatisticController statisticController, AccountController accountController) {
        setMoneyTransferController(moneyTransferController);
        setStatisticController(statisticController);
        setAccountController(accountController);
        setupRoutes();
    }


    private void setupRoutes() {
        log.info("Setting routes");
        port(8080);
        path("/bank/api", () -> {
            before("/*", (req, res) -> log.debug("Received bank api call " + req.body() + " " + res));

            path("/transfer", () -> {
                post("/make", ((request, response) -> moneyTransferController.handleTransferRequest(request, response)));
            });

            path("/stat", () -> {
                get("/allaccstatus", ((request, response) -> statisticController.getAllAccountsStatus()));
                get("/transactioncount", ((request, response) -> statisticController.getTransactionCount()));
            });

            path("/account", () -> {
                get("/:id", ((request, response) -> accountController.findById(request.params("id"))));
            });

        });
    }


    public void setMoneyTransferController(MoneyTransferController moneyTransferController) {
        this.moneyTransferController = moneyTransferController;
    }

    public void setStatisticController(StatisticController statisticController) {
        this.statisticController = statisticController;
    }

    public void setAccountController(AccountController accountController) {
        this.accountController = accountController;
    }
}





