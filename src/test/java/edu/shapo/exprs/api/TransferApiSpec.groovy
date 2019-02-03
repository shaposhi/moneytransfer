package edu.shapo.exprs.api

import com.despegar.http.client.GetMethod
import com.despegar.http.client.HttpResponse
import com.despegar.sparkjava.test.SparkServer
import spark.servlet.SparkApplication
import spock.lang.Specification

class TransferApiSpec extends Specification {

    static class TestTransferApiSparkApplication implements SparkApplication {
        @Override
        void init() {
            new MoneyTransferApi(new MoneyTransferApiHandler())
        }
    }

    public static SparkServer<TestTransferApiSparkApplication> testServer

    def setup() {
        testServer = new SparkServer<>(TestTransferApiSparkApplication.class, 8090)
    }

    def "check responce on post request" () {

        given:
        GetMethod request = testServer.get("/", false)

        when:
        HttpResponse httpResponse = testServer.execute(request)

        then:
        httpResponse
        httpResponse.code() == 200
    }

}
