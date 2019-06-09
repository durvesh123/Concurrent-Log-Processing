package com.verizon.singlethreaded;

import com.verizon.model.Statistics;
import java.util.List;
import java.util.Arrays;

public class Controller {

    public static void main( String args [] ) {
        SingleThreadedVideoStatistics svs = new SingleThreadedVideoStatistics();

        //Single file statistics
        /*
        String file = "log1.txt";
        List<String> lines = svs.readAndProcessSingleFile(file);
        List<Statistics> stats = svs.generateStatistics(lines);
        stats =  svs.sortedByCategoryTitleVideoLength(stats);
        System.out.println("----------SORTED BY CATEGORY TITLE VIDEO_LENGTH----------");
        svs.display(stats);
        stats = svs.sortedByNumberOfViews(stats);
        System.out.println("----------SORTED BY NUMBER OF VIEWS----------");
        svs.display(stats);
        stats = svs.videosWatchedOver50Percent(stats);
        System.out.println("----------VIDEOS WATCHED OVER 50% OF ITS LENGTH----------");
        svs.display(stats);
        */



        //Multiple files statistics
        List<String> input = Arrays.asList("log1.txt","log2.txt","log3.txt");
        List<String> lines = svs.readAndProcessAllFiles(input);
        List<Statistics> stats = svs.generateStatistics(lines);
        stats =  svs.sortedByCategoryTitleVideoLength(stats);
        System.out.println("----------SORTED BY CATEGORY TITLE VIDEO_LENGTH----------");
        svs.display(stats);
        stats = svs.sortedByNumberOfViews(stats);
        System.out.println("----------SORTED BY NUMBER OF VIEWS----------");
        svs.display(stats);
        stats = svs.videosWatchedOver50Percent(stats);
        System.out.println("----------VIDEOS WATCHED OVER 50% OF ITS LENGTH----------");
        svs.display(stats);


    }
}
