package com.homer.greendaodemo;

import android.app.Application;

import com.homer.greendaodemo.greendao.gen.DaoMaster;
import com.homer.greendaodemo.greendao.gen.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Homer on 2016/7/14.
 */
public class App extends Application {
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
