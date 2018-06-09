package io.github.prototypez.savestate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

public class MainActivity extends AppCompatActivity {


    Bundle a;

    @AutoRestore
    int b;

    long c;

    @AutoRestore
    String data;

    @AutoRestore
    List<User> stringList;

    @AutoRestore
    List list;

    @AutoRestore
    User<Integer> user;

    @AutoRestore
    User user2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    class User<T> {
        String name;
        T something;

        public User(String name, T something) {
            this.name = name;
            this.something = something;
        }
    }


    class Response<T> {
        int code;

        T body;

        public Response(int code, T body) {
            this.code = code;
            this.body = body;
        }
    }
}
