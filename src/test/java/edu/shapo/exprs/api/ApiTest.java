package edu.shapo.exprs.api;

import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.google.gson.Gson;
import edu.shapo.exprs.to.TransferRequestTO;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiTest {

    public static class TestTransferApiSparkApplication implements SparkApplication {
        @Override
        public void init() {

            MoneyTransferApiHandler mockMoneyTransferHandler = mock(MoneyTransferApiHandler.class);
            when(mockMoneyTransferHandler.handleTransferRequest(any(), any())).thenReturn("OK");

            new MoneyTransferApi(mockMoneyTransferHandler);


        }
    }

    @ClassRule
    public static SparkServer<TestTransferApiSparkApplication> testServer = new SparkServer<>(TestTransferApiSparkApplication.class, 8080);

    @Test
    public void serverRespondsSuccessfully() throws HttpClientException {

        TransferRequestTO transferRequestTO = new  TransferRequestTO();
        transferRequestTO.setSourceAccountId(5L);
        transferRequestTO.setTargetAccountId(7L);
        transferRequestTO.setTransferAmount(new BigDecimal(100));
        transferRequestTO.setInitiator("John");
        String bodyContent = new Gson().toJson(transferRequestTO);

        PostMethod request = testServer.post("/bank/api/transfer/make", bodyContent,false);
        HttpResponse httpResponse = testServer.execute(request);
        assertEquals(200, httpResponse.code());
    }

}
