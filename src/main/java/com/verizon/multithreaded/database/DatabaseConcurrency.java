package com.verizon.multithreaded.database;

import com.verizon.model.Statistics;
import com.verizon.singlethreaded.SingleThreadedVideoStatistics;

import java.sql.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatabaseConcurrency {

    private final String connection;
    private final String user;
    private final String password;
    private Connection connect;
    private final SingleThreadedVideoStatistics svs;
    private final int concurrencyFactor;
    private final ExecutorService executorService;

    public DatabaseConcurrency(String connection, String user, String password, int concurrencyFactor) {
        this.connection = connection;
        this.user = user;
        this.password = password;
        this.svs = new SingleThreadedVideoStatistics();
        this.concurrencyFactor = concurrencyFactor;
        this.executorService = Executors.newFixedThreadPool(concurrencyFactor);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connect = DriverManager.getConnection(connection,user,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDatabase( List<String> fileNames ) {



        executorService.submit(new Runnable() {
            @Override
            public void run() {

                PreparedStatement preparedStatement;

                try {

                    List<Statistics> stats = svs.generateStatistics(svs.readAndProcessAllFiles(fileNames));

                    for ( Statistics s : stats ) {

                        preparedStatement = connect.prepareStatement("insert into  verizon.stats (category, title, video_length, average_watch_time, number_of_views) values (?, ?, ?, ?, ?)");
                        preparedStatement.setString(1, s.getCategory());
                        preparedStatement.setString(2, s.getTitle());
                        preparedStatement.setDouble(3, s.getVideoLength());
                        preparedStatement.setDouble(4, s.getAverageWatchTime());
                        preparedStatement.setInt(5, s.getNumberOfViews());
                        preparedStatement.executeUpdate();

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        System.out.println("----------ALL RECORDS INSERTED SUCCESSFULLY----------");

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
