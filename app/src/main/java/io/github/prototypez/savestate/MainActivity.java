package io.github.prototypez.savestate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.prototypez.savestate.core.ParameterizedTypeHelper;
import io.github.prototypez.savestate.core.annotation.AutoRestore;

public class MainActivity extends AppCompatActivity {


    Bundle a;

    @AutoRestore
    int b;
    long c;

    @AutoRestore
    String data;

    @AutoRestore
    List<User<Integer>> stringList;

    Response<List<User<Integer>>> response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stringList = new ArrayList<User<Integer>>();
        stringList.add(new User<>("jack", 16));
        stringList.add(new User<>("rose", 18));

        Gson gson = new Gson();

        String json = gson.toJson(stringList);

        Log.i("SaveState", json);

        List<User<Integer>> list = gson.fromJson(json, new ParameterizedTypeHelper(List.class, null, new Class[]{User.class, Integer.class}));

        Log.i("SaveState", list.toString());


        Response<List<User<Integer>>> response = new Response<>(200, stringList);

        json = gson.toJson(response);


//        Log.i("SaveState", parsedResponse.toString());
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
