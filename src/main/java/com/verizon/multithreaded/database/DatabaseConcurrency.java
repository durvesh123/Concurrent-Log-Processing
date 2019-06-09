package com.verizon.multithreaded.database;

import com.verizon.model.Statistics;
import com.verizon.singlethreaded.SingleThreadedVideoStatistics;
import com.verizon.multithreaded.future.CompletableFutureImplementation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DatabaseConcurrency {

    private final String connection;
    private final String user;
    private final String password;
    private Connection connect;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private final SingleThreadedVideoStatistics svs;
    private final int concurrencyFactor;
    private final ExecutorService executorService;
    private final CompletableFutureImplementation completableFutureImplementation;

    public DatabaseConcurrency(String connection, String user, String password, int concurrencyFactor) {
        this.connection = connection;
        this.user = user;
        this.password = password;
        this.svs = new SingleThreadedVideoStatistics();
        this.concurrencyFactor = concurrencyFactor;
        this.executorService = Executors.newFixedThreadPool(concurrencyFactor);
        this.completableFutureImplementation = new CompletableFutureImplementation(this.concurrencyFactor);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connect = DriverManager.getConnection(connection,user,password);
            this.statement = connect.createStatement();

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

    public List<Statistics> getModelFromDatabase() {

        Future<List<Statistics>> completableFuture = new CompletableFuture<>();
        completableFuture = executorService.submit(new Callable<List<Statistics>>() {
            @Override
            public List<Statistics> call() throws Exception {
                List<Statistics> stats = new ArrayList<>();
                try {
                    resultSet = statement.executeQuery("select * from verizon.stats");
                    while ( resultSet.next() ) {
                        String category = resultSet.getString("category");
                        String title = resultSet.getString("title");
                        Double video_length = resultSet.getDouble("video_length");
                        Double average_watch_time = resultSet.getDouble("average_watch_time");
                        Integer number_of_views = resultSet.getInt("number_of_views");
                        Statistics s = new Statistics(category, title, video_length, average_watch_time, number_of_views);
                        stats.add(s);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return stats;
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


    public List<Statistics> sortedByNumberOfViews( List<String> input ) {

        Future<List<Statistics>> completableFuture = new CompletableFuture<>();
         completableFuture = executorService.submit(new Callable<List<Statistics>>() {
             @Override
             public List<Statistics> call() throws Exception {
                 return completableFutureImplementation.sortedByNumberOfViews(input);
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
        Future<List<Statistics>> completableFuture = new CompletableFuture<>();
        completableFuture = executorService.submit(new Callable<List<Statistics>>() {
            @Override
            public List<Statistics> call() throws Exception {
                return completableFutureImplementation.sortedByCategoryTitleVideoLength(input);
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
        Future<List<Statistics>> completableFuture = new CompletableFuture<>();
        completableFuture = executorService.submit(new Callable<List<Statistics>>() {
            @Override
            public List<Statistics> call() throws Exception {
                return completableFutureImplementation.videosWatchedOver50Percent(input);
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
            connect.close();
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }


}
