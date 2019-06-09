package com.verizon.multithreaded;

import java.util.Arrays;
import java.util.List;

public class Controller {

    public static void main( String args [] ) {
        int concurrencyFactor = 3;
        ExecutorServiceImplementation esi = new ExecutorServiceImplementation(concurrencyFactor);
        List<String> input = Arrays.asList("log1.txt","log2.txt","log3.txt");
        System.out.println("----------SORTED BY NUMBER OF VIEWS----------");
        esi.sortedByNumberOfViews(input);
        System.out.println("----------SORTED BY CATEGORY TITLE VIDEO_LENGTH----------");
        esi.sortedByCategoryTitleVideoLength(input);
        System.out.println("----------VIDEOS WATCHED OVER 50% OF ITS LENGTH----------");
        esi.videosWatchedOver50Percent(input);
        esi.stopExecution();
    }
}
