package com.example.quickvik.chattingapp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by quickvik on 8/25/2015.
 */
public class BackgroundExecutor {

    private static BackgroundExecutor mInstance;
    private static ExecutorService executorService;

    private BackgroundExecutor() {
    }

    public static BackgroundExecutor getInstance() {
        if (mInstance == null) {
            mInstance = new BackgroundExecutor();
        }
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(5);
        }
        return mInstance;
    }

    public void execute(Runnable runnable) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(5);
        }
        executorService.submit(runnable);
    }
}
