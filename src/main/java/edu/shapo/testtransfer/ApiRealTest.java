package edu.shapo.testtransfer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiRealTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            System.out.println("Thread " + i + " started");
            SingleTransferCallThread thread = new SingleTransferCallThread();
            executorService.submit(thread);
        }
        executorService.shutdown();

        SingleStatisticCallThread sasct1 = new SingleStatisticCallThread("http://localhost:8080/bank/api/stat/allaccstatus");
        sasct1.start();

        SingleStatisticCallThread sasct2 = new SingleStatisticCallThread("http://localhost:8080/bank/api/stat/transactioncount");
        sasct2.start();
    }
}
