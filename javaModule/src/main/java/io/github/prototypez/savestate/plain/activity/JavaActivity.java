package io.github.prototypez.savestate.plain.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.util.SizeF;
import io.github.prototypez.savestate.core.annotation.AutoRestore;
import io.github.prototypez.savestate.plain.R;
import io.github.prototypez.savestate.plain.databinding.ActivityJavaBinding;
import io.github.prototypez.savestate.plain.entity.User;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JavaActivity extends AppCompatActivity{

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
    HashMap<String, String> serializable;

    @AutoRestore
    IBinder iBinder;

    @AutoRestore
    Bundle bundle;

    @AutoRestore
    CharSequence charSequence;

    @AutoRestore
    Intent parcelable;

    @AutoRestore
    Size size;

    @AutoRestore
    SizeF sizeF;

    @AutoRestore
    String data;

    @AutoRestore
    byte[] byteArray;

    @AutoRestore
    short[] shortArray;

    @AutoRestore
    char[] charArray;

    @AutoRestore
    float[] floatArray;

    @AutoRestore
    CharSequence[] charSequenceArray;

    @AutoRestore
    Parcelable[] parcelableArray;

    @AutoRestore
    User<Integer> user;

    @AutoRestore
    List<User<Integer>> userList;

    ActivityJavaBinding mBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_java);

        refresh();

        mBinding.assignValue.setOnClickListener(v -> {
            testInt = 1;
            testInt2 = 2;
            testLong = 1000;
            testLong2 = 2000L;
            testChar = 'c';
            testChar2 = 'b';
            testBool = true;
            testBool2 = true;
            testShort = 10;
            testShort2 = 20;
            testByte = 51;
            testByte2 = 52;
            testFloat = 1.0f;
            testFloat2 = 2.0f;
            testDouble = 3.0;
            testDouble2 = 4.0;
            serializable = new HashMap<String,String>(){{put("key", "value");}};
//            iBinder =
            bundle = new Bundle();
            bundle.putString("testBundle", "stringInBundle");
            charSequence = "testCharSequence";
            size = new Size(110, 120);
            sizeF = new SizeF(110.110f, 120.120f);
            parcelable = new Intent("testParcelable");
            data = "testString";

            byteArray = new byte[]{1, 2, 3, 4};
            shortArray = new short[]{10, 20, 30, 40};
            charArray = new char[]{'a', 'b', 'c', 'd'};
            floatArray = new float[]{1.1f, 2.2f, 3.3f, 4.4f};

            charSequenceArray = new CharSequence[]{"c1", "c2"};
            parcelableArray = new Parcelable[]{new Point(7, 7), new Point(8, 8)};

            user = new User<>("jack", 29);
            userList = Arrays.asList(user, new User<>("lee", 27));

            refresh();
        });
    }

    private void refresh() {
        mBinding.aTestInt.setText(String.valueOf(testInt));
        mBinding.aTestInt2.setText(String.valueOf(testInt2));
        mBinding.aTestLong.setText(String.valueOf(testLong));
        mBinding.aTestLong2.setText(String.valueOf(testLong2));
        mBinding.aTestShort.setText(String.valueOf(testShort));
        mBinding.aTestShort2.setText(String.valueOf(testShort2));
        mBinding.aTestBool.setText(String.valueOf(testBool));
        mBinding.aTestBool2.setText(String.valueOf(testBool2));
        mBinding.aTestChar.setText(String.valueOf(testChar));
        mBinding.aTestChar2.setText(String.valueOf(testChar2));
        mBinding.aTestByte.setText(String.valueOf(testByte));
        mBinding.aTestByte2.setText(String.valueOf(testByte2));
        mBinding.aTestFloat.setText(String.valueOf(testFloat));
        mBinding.aTestFloat2.setText(String.valueOf(testFloat2));
        mBinding.aTestDouble.setText(String.valueOf(testDouble));
        mBinding.aTestDouble2.setText(String.valueOf(testDouble2));
        mBinding.aTestSerializable.setText(String.valueOf(serializable));
//        mBinding.aTestSerializable.setText(iBinder.toString());
        mBinding.aTestBundle.setText(bundle == null ? "null" : String.valueOf(bundle.getString("testBundle")));
        mBinding.aTestCharSequence.setText(String.valueOf(charSequence));
        mBinding.aTestString.setText(String.valueOf(data));
        mBinding.aTestSize.setText(String.valueOf(size));
        mBinding.aTestSizeF.setText(String.valueOf(sizeF));
        mBinding.aTestParcelable.setText(parcelable == null ? "null" : String.valueOf((parcelable.getAction())));

        mBinding.aTestByteArray.setText(Arrays.toString(byteArray));
        mBinding.aTestCharArray.setText(Arrays.toString(charArray));
        mBinding.aTestShortArray.setText(Arrays.toString(shortArray));
        mBinding.aTestFloatArray.setText(Arrays.toString(floatArray));

        mBinding.aTestParcelableArray.setText(Arrays.toString(parcelableArray));
        mBinding.aTestCharSequenceArray.setText(Arrays.toString(charSequenceArray));

        mBinding.aTestUserList.setText(String.valueOf(userList));
        mBinding.aTestUser.setText(String.valueOf(user));

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
