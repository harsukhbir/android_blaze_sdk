package com.blazeautomation.connected_ls_sample.localdatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PhotoModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}