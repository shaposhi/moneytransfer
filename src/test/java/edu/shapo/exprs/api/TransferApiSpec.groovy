package edu.shapo.exprs.api

import com.despegar.http.client.HttpResponse
import com.despegar.http.client.PostMethod
import com.despegar.sparkjava.test.SparkServer
import org.junit.Ignore
import spark.servlet.SparkApplication
import spock.lang.Specification

@Ignore
class TransferApiSpec extends Specification {

    static class TestTransferApiSparkApplication implements SparkApplication {
        @Override
        void init() {
            new MoneyTransferApi(new MoneyTransferApiHandler())
        }
    }

    @org.junit.ClassRule
    public static SparkServer<TestTransferApiSparkApplication> testServer

    def setupSpec() {
        testServer = new SparkServer<>(TestTransferApiSparkApplication.class, 8080)
    }

    def "check responce on post request" () {

        given:
        PostMethod request = testServer.post("/bank/api/transfer/make", " ",false)

        when:
        Thread.sleep(2000)
        HttpResponse httpResponse = testServer.execute(request)

        then:
        httpResponse
        httpResponse.code() == 200
    }

}
