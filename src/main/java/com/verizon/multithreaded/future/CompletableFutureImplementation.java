package com.verizon.multithreaded.future;

import com.verizon.model.Statistics;
import com.verizon.singlethreaded.SingleThreadedVideoStatistics;

import java.util.List;
import java.util.concurrent.*;

public class CompletableFutureImplementation {

    private final ExecutorService executorService;
    private final int concurrencyFactor;

    public CompletableFutureImplementation(int concurrencyFactor) {
        this.concurrencyFactor = concurrencyFactor;
        this.executorService = Executors.newFixedThreadPool(concurrencyFactor);
    }

    public List<Statistics> sortedByNumberOfViews( List<String> input ) {
        SingleThreadedVideoStatistics svs = new SingleThreadedVideoStatistics();
        Future<List<Statistics>> completableFuture = new CompletableFuture<>();
        completableFuture = executorService.submit(new Callable<List<Statistics>>() {
            @Override
            public List<Statistics> call() throws Exception {
               return svs.sortedByNumberOfViews( svs.generateStatistics( svs.readAndProcessAllFiles(input) ) );
            }
        });

        try {
            return completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Statistics> sortedByCategoryTitleVideoLength( List<String> input ) {
        SingleThreadedVideoStatistics svs = new SingleThreadedVideoStatistics();
        Future<List<Statistics>> completableFuture = new CompletableFuture<>();
        completableFuture = executorService.submit(new Callable<List<Statistics>>() {
            @Override
            public List<Statistics> call() throws Exception {
                return svs.sortedByCategoryTitleVideoLength( svs.generateStatistics( svs.readAndProcessAllFiles(input) ) );
            }
        });

        try {
            return completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Statistics> videosWatchedOver50Percent( List<String> input ) {
        SingleThreadedVideoStatistics svs = new SingleThreadedVideoStatistics();
        Future<List<Statistics>> completableFuture = new CompletableFuture<>();
        completableFuture = executorService.submit(new Callable<List<Statistics>>() {
            @Override
            public List<Statistics> call() throws Exception {
                return svs.videosWatchedOver50Percent( svs.generateStatistics( svs.readAndProcessAllFiles(input) ) );
            }
        });

        try {
            return completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
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
