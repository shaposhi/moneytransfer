package edu.shapo.exprs.api;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.sparkjava.test.SparkServer;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import static junit.framework.TestCase.assertEquals;

public class ApiTest {

    static class TestTransferApiSparkApplication implements SparkApplication {
        @Override
        public void init() {
            new MoneyTransferApi(new MoneyTransferApiHandler());
        }
    }

    @ClassRule
    public static SparkServer<TestTransferApiSparkApplication> testServer = new SparkServer<>(TestTransferApiSparkApplication.class, 8090);

    @Test
    public void serverRespondsSuccessfully() throws HttpClientException {
        GetMethod request = testServer.get("/", false);
        HttpResponse httpResponse = testServer.execute(request);
        assertEquals(200, httpResponse.code());
    }

}
