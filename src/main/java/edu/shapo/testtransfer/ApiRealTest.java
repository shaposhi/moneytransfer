package edu.shapo.testtransfer;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiRealTest {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);


        for (int i=0; i<1000; i++) {
            System.out.println("Thread " + i + " started");
            SingleCallThread thread = new SingleCallThread();
            executorService.submit(thread);
        }

        executorService.shutdown();

    }





}
