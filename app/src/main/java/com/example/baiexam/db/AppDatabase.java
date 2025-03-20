package com.example.baiexam.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.baiexam.dao.SachDao;
import com.example.baiexam.dao.TacGiaDao;
import com.example.baiexam.model.Sach;
import com.example.baiexam.model.TacGia;

@Database(entities = {TacGia.class, Sach.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract TacGiaDao tacGiaDao();
    public abstract SachDao sachDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "library_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
