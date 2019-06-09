package com.verizon.multithreaded.database;


import java.util.Arrays;
import java.util.List;

public class Controller {

    public static void main ( String args [] ) {

        String connection = "jdbc:mysql://0.0.0.0/verizon?autoReconnect=true&useSSL=false";
        String user = "root";
        String password = "12345678";
        int concurrencyFactor = 3;
        DatabaseConcurrency dbc = new DatabaseConcurrency(connection, user, password, concurrencyFactor );
        List<String> input = Arrays.asList("log1.txt","log2.txt","log3.txt");
        dbc.loadDatabase(input);
        dbc.stopExecution();

    }

}
