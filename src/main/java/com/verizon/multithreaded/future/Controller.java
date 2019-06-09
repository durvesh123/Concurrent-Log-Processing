package com.verizon.multithreaded.future;

import com.verizon.model.Statistics;
import com.verizon.singlethreaded.SingleThreadedVideoStatistics;

import java.util.Arrays;
import java.util.List;

public class Controller {

    public static void main( String args[] ) {

        SingleThreadedVideoStatistics svs = new SingleThreadedVideoStatistics();
        int concurrencyFactor = 3;
        CompletableFutureImplementation cfi = new CompletableFutureImplementation(concurrencyFactor);
        List<String> input = Arrays.asList("log1.txt","log2.txt","log3.txt");
        List<Statistics> stats;
        System.out.println("----------SORTED BY NUMBER OF VIEWS----------");
        stats = cfi.sortedByNumberOfViews(input);
        svs.display(stats);
        System.out.println("----------SORTED BY CATEGORY TITLE VIDEO_LENGTH----------");
        stats = cfi.sortedByCategoryTitleVideoLength(input);
        svs.display(stats);
        System.out.println("----------VIDEOS WATCHED OVER 50% OF ITS LENGTH----------");
        stats = cfi.videosWatchedOver50Percent(input);
        svs.display(stats);

        cfi.stopExecution();

    }
}
