package com.kaval.android.inflow;

import com.kaval.android.inflow.model.Task;
import com.orm.SugarApp;
import com.orm.SugarDb;

public class Application extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        // create table if not exists
        getDatabase().getDB();
        Task.findById(Task.class, (long) 0);
    }
}
