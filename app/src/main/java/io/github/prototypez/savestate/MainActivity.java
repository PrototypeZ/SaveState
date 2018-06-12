package io.github.prototypez.savestate;

import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.util.SizeF;

import java.io.Serializable;
import java.util.List;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

public class MainActivity extends AppCompatActivity {


    Bundle a;

    @AutoRestore
    int testInt;

    @AutoRestore
    Integer testInt2;

    @AutoRestore
    long testLong;

    @AutoRestore
    Long testLong2;

    @AutoRestore
    short testShort;

    @AutoRestore
    Short testShort2;

    @AutoRestore
    boolean testBool;

    @AutoRestore
    Boolean testBool2;

    @AutoRestore
    char testChar;

    @AutoRestore
    Character testChar2;

    @AutoRestore
    byte testByte;

    @AutoRestore
    Byte testByte2;

    @AutoRestore
    float testFloat;

    @AutoRestore
    Float testFloat2;

    @AutoRestore
    double testDouble;

    @AutoRestore
    Double testDouble2;

    @AutoRestore
    Serializable serializable;

    @AutoRestore
    IBinder iBinder;

    @AutoRestore
    Bundle bundle;

    @AutoRestore
    CharSequence charSequence;

    @AutoRestore
    Parcelable parcelable;

    @AutoRestore
    Size size;

    @AutoRestore
    SizeF sizeF;


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
