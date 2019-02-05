package edu.shapo.exprs.unitint;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.shapo.exprs.configuration.AppModule;
import edu.shapo.exprs.to.AccountTO;
import edu.shapo.exprs.to.TransferRequestTO;
import edu.shapo.exprs.unit.api.AccountController;
import edu.shapo.exprs.unit.api.MoneyTransferApi;
import edu.shapo.exprs.unit.api.MoneyTransferController;
import edu.shapo.exprs.unit.api.StatisticController;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import spark.servlet.SparkApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApiIntegrationTest {

    public static class TestTransferApiSparkApplication implements SparkApplication {
        @Override
        public void init() {
            Injector injector = Guice.createInjector(new AppModule());

            MoneyTransferController moneyTransferController = injector.getInstance(MoneyTransferController.class);
            StatisticController statisticController = injector.getInstance(StatisticController.class);
            AccountController accountController = injector.getInstance(AccountController.class);

            new MoneyTransferApi(moneyTransferController, statisticController, accountController);
        }
    }

    @ClassRule
    public static SparkServer<TestTransferApiSparkApplication> testServer = new SparkServer<>(TestTransferApiSparkApplication.class, 8080);

    @Test
    public void serverRespondsSuccessfully() throws HttpClientException {

        TransferRequestTO transferRequestTO = new TransferRequestTO();
        transferRequestTO.setSourceAccountId(5L);
        transferRequestTO.setTargetAccountId(7L);
        transferRequestTO.setTransferAmount(new BigDecimal("100"));
        transferRequestTO.setInitiator("John");
        String bodyContent = new Gson().toJson(transferRequestTO);

        PostMethod request = testServer.post("/bank/api/transfer/make", bodyContent, false);
        HttpResponse httpResponse = testServer.execute(request);
        assertEquals(200, httpResponse.code());

        GetMethod request2 = testServer.get("/bank/api/account/5", false);
        HttpResponse httpResponse2 = testServer.execute(request2);
        assertEquals(200, httpResponse2.code());

        String accAsString = new String(httpResponse2.body());
        AccountTO resultAcc = new Gson().fromJson(accAsString, AccountTO.class);
        assertEquals(resultAcc.getCurrentAmout(), new BigDecimal("900.00"));
    }


    @Test
    public void serverRespondsSuccessfullyMultiThr() throws HttpClientException, InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<Integer>> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Callable<Integer> task = () -> {
                TransferRequestTO transferTO = new TransferRequestTO();
                transferTO.setSourceAccountId(5L);
                transferTO.setTargetAccountId(7L);
                transferTO.setTransferAmount(new BigDecimal("10"));
                transferTO.setInitiator("John");
                String bodyContent = new Gson().toJson(transferTO);

                PostMethod request = testServer.post("/bank/api/transfer/make", bodyContent, false);
                HttpResponse httpResponse = testServer.execute(request);

                assertEquals(200, httpResponse.code());
                return httpResponse.code();
            };
            Future<Integer> future = executorService.submit(task);
            tasks.add(future);
        }

        executorService.shutdown();

        for (Future<Integer> task : tasks) {
            task.get();
        }

        GetMethod request2 = testServer.get("/bank/api/account/5", false);
        HttpResponse httpResponse2 = testServer.execute(request2);
        assertEquals(200, httpResponse2.code());

        String accAsString = new String(httpResponse2.body());
        AccountTO resultAcc = new Gson().fromJson(accAsString, AccountTO.class);
        assertEquals(resultAcc.getCurrentAmout(), new BigDecimal("800.00"));
    }
}
