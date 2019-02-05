package edu.shapo.exprs.unitint.loadtesttool;

import edu.shapo.exprs.service.TransferServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiLoadTestTool {

    private static final Logger log = LogManager.getLogger(TransferServiceImpl.class);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            log.info("Thread " + i + " started");
            Runnable task = new SingleTransferCallThread();
            executorService.submit(task);
        }
        executorService.shutdown();

        SingleStatisticCallThread sasct1 = new SingleStatisticCallThread("http://localhost:8080/bank/api/stat/allaccstatus");
        sasct1.start();

        SingleStatisticCallThread sasct2 = new SingleStatisticCallThread("http://localhost:8080/bank/api/stat/transactioncount");
        sasct2.start();
    }
}
