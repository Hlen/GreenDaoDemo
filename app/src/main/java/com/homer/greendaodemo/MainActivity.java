package com.homer.greendaodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.homer.greendaodemo.greendao.entity.User;
import com.homer.greendaodemo.greendao.gen.DaoSession;
import com.homer.greendaodemo.greendao.gen.UserDao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
}
