package com.wakefern.global;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Futures {

    private static final Logger logger = LogManager.getLogger(Futures.class);

    public static <T> List<T> getAllCompleted(List<CompletableFuture<T>> futuresList, long timeoutMs) throws Exception {
        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[futuresList.size()]));
        try {
            allFuturesResult.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("Exception: getAllCompleted", e);
            // Throw specific exception messages to avoid exposing sensitive exception details
            if (e instanceof TimeoutException) {
                throw new Exception("Request timed out");
            } else {
                throw new Exception("Unexpected error");
            }
        }
        return futuresList
                .stream()
                .filter(future -> future.isDone() && !future.isCompletedExceptionally()) // keep only the ones completed
                .map(CompletableFuture::join) // get the value from the completed future
                .collect(Collectors.<T>toList()); // collect as a list
    }

}
