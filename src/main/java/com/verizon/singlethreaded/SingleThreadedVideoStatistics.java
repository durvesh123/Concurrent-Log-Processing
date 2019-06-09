package com.verizon.singlethreaded;

import com.verizon.model.Statistics;

import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;
import java.net.URL;

public class SingleThreadedVideoStatistics {

    /**
     * @param fileName log file to process
     * @return List of lines read from file
     */
    public List<String> readAndProcessSingleFile( String fileName ) {

        List<String> lines = new ArrayList<>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource(fileName);
            if (resource == null) {
                throw new IllegalArgumentException();
            }
            lines = Files.readAllLines(Paths.get(resource.getFile()),StandardCharsets.UTF_8);
        }
        catch ( IllegalArgumentException iae ) {
            System.out.printf("Cannot find file | %s",iae.toString());
        }
        catch ( IOException ioe ) {
            System.out.printf("Error reading file | %s",ioe.toString());
        }

        return lines;

    }

    /**
     * @param fileNames List of log files to read
     * @return accumulated List of lines read from all files
     */
    public List<String> readAndProcessAllFiles( List<String> fileNames ) {

        List<String> lines = new ArrayList<>();
        List<List<String>> temp = new ArrayList<>();
        for ( String fileName : fileNames ) {
            temp.add(readAndProcessSingleFile(fileName));
        }

        for ( int i = 0 ; i < temp.size(); i++ ){
            List<String> l = temp.get(i);
            for ( int j = 0; j < l.size(); j++ ) {
                lines.add(l.get(j));
            }
        }
        return lines;

    }

    /**
     * @param list List of lines from log files
     * @return List of Statistics objects
     */
    public List<Statistics> generateStatistics( List<String> list ) {

        List<Statistics> data = new ArrayList<>();

        for( String s : list ) {

            String temp[] = s.split(" ");
            String category = temp[0].trim();
            Integer numberOfViews = Integer.parseInt( temp[temp.length - 2].trim() );
            Double averageWatchTime = Double.parseDouble( temp[temp.length - 3].substring(0,temp[temp.length - 3].indexOf("ms")).trim() );
            Double lengthOfVideo = Double.parseDouble( temp[temp.length - 4].substring(0,temp[temp.length - 4].indexOf("ms")).trim() );
            StringBuilder sbr = new StringBuilder();
            for ( int i = 1; i < temp.length-4; i++ ) {
                sbr.append(temp[i].trim()+" ");
            }
            String title = sbr.toString().trim().toLowerCase();
            Statistics stats = new Statistics(category, title, lengthOfVideo, averageWatchTime, numberOfViews);

            data.add(stats);
        }

        return data;
    }


    /**
     * @param stats List of Statistics objects
     * @return List of Statistics objects sorted by Category (sorted in ascending order)
     * Title (sorted in increasing order and ignoring case)
     * Video length (sorted in decreasing order)
     */
    public List<Statistics> sortedByCategoryTitleVideoLength( List<Statistics> stats ) {

        Collections.sort(stats, new Comparator<Statistics>() {
            @Override
            public int compare(Statistics s1, Statistics s2) {
                if (s1.getCategory().compareTo(s2.getCategory()) != 0) {
                    return s1.getCategory().compareTo(s2.getCategory());
                } else if (s1.getTitle().compareTo(s2.getTitle()) != 0) {
                    return s1.getTitle().compareTo(s2.getTitle());
                } else {
                    return Double.compare(s2.getVideoLength(), s1.getVideoLength());
                }
            }
        });
        return stats;
    }

    /**
     * @param stats List of Statistics objects
     * @return List of Statistics objects sorted by number of views (sorted in descending order)
     */
    public List<Statistics> sortedByNumberOfViews( List<Statistics> stats ) {

        Collections.sort(stats, new Comparator<Statistics>() {
            @Override
            public int compare(Statistics s1, Statistics s2) {
                return Double.compare(s2.getNumberOfViews(), s1.getNumberOfViews());
            }
        });

        return stats;
    }

    /**
     * @param stats List of Statistics objects
     * @return List of Statistics objects having videos watched over 50% of its length
     */
    public List<Statistics> videosWatchedOver50Percent( List<Statistics> stats ) {

        List<Statistics> list = new ArrayList<>();

        for ( Statistics s : stats ) {
            if ( s.getAverageWatchTime() / s.getVideoLength() * 100 >= 50 ) {
                list.add(s);
            }
        }

        return list;
    }


    /**
     * @param stats List of Statistics objects
     * displays stats to console
     */
    public void display ( List<Statistics>  stats ) {

        Iterator<Statistics> iterator = stats.iterator();

        while( iterator.hasNext() ) {
            System.out.println( iterator.next().toString() );
        }

    }

}
