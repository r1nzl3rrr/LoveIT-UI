/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group03.loveit.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author duyvu
 */
public abstract class AsyncUtils<T> {

    
   
    /**
     * Execute Asynchronous Statement to delay time processing
     * @param <T>
     * @param sql
     * @param consumer
     * @param extractor
     * @return 
     */
    public final static <T> CompletableFuture<List<T>> executeQueryAsync(String sql, Consumer consumer, Function<ResultSet, T> extractor) {
        return CompletableFuture.supplyAsync(new Supplier<List<T>>() {
            @Override
            public List<T> get() {
                Connection connection = null;
                PreparedStatement statement = null;
                ResultSet resultSet = null;

                // Storing result
                List<T> results = new ArrayList();
                
                try {
                    connection = DBUtils.getConnection();
                    statement = connection.prepareStatement(sql);

                    // Set statement parameters if available
                    consumer.accept(statement);

                    resultSet = statement.executeQuery();

                    if (resultSet != null && !resultSet.isClosed()) {
                        while (resultSet.next()) 
                        {    
                            // return the object after processing returned properties from resultSet
                            T result = (T) extractor.apply(resultSet);
                            results.add(result);
                        }
                        return results;
                    } else {
                        return Collections.emptyList();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(AsyncUtils.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    AsyncUtils.closeQuitely(statement);
                    AsyncUtils.closeQuitely(resultSet);
//                    AsyncUtils.closeQuitely(connection); // Turn back and forward will create overhead
                }
                return results;
            }
        });
    }
    
    // Turn off the connection on the resources
    public final static void closeQuitely(AutoCloseable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (Exception ex) {
                Logger.getLogger(AsyncUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
