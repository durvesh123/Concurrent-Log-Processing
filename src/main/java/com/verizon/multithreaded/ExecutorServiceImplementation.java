package com.verizon.multithreaded;

import com.verizon.singlethreaded.SingleThreadedVideoStatistics;

import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceImplementation {

    private final ExecutorService executorService;
    private final int concurrencyFactor;
    private final SingleThreadedVideoStatistics svs;

    public ExecutorServiceImplementation(int concurrencyFactor) {
        this.concurrencyFactor = concurrencyFactor;
        this.executorService = Executors.newFixedThreadPool(this.concurrencyFactor);
        this.svs = new SingleThreadedVideoStatistics();
    }

    public void sortedByNumberOfViews( List<String> input ) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                svs.display( svs.sortedByNumberOfViews( svs.generateStatistics( svs.readAndProcessAllFiles(input) ) ) );
            }
        });
    }

    public void sortedByCategoryTitleVideoLength( List<String> input ) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                svs.display( svs.sortedByCategoryTitleVideoLength( svs.generateStatistics( svs.readAndProcessAllFiles(input) ) ) );
            }
        });
    }

    public void videosWatchedOver50Percent( List<String> input ) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                svs.display( svs.videosWatchedOver50Percent( svs.generateStatistics( svs.readAndProcessAllFiles(input) ) ) );
            }
        });
    }

    public void stopExecution() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
