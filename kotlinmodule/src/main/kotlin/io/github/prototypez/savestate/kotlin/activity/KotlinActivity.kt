package io.github.prototypez.savestate.kotlin.activity

import android.databinding.DataBindingUtil
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Size
import android.util.SizeF

import java.io.Serializable
import java.util.Arrays
import java.util.HashMap

import io.github.prototypez.savestate.core.annotation.AutoRestore
import io.github.prototypez.savestate.kotlin.R
import io.github.prototypez.savestate.kotlin.entity.Response
import io.github.prototypez.savestate.kotlin.databinding.ActivityKotlinBinding

class KotlinActivity : AppCompatActivity() {

    @AutoRestore
    var testInt: Int = 0

    @AutoRestore
    var testInt2: Int? = null

    @AutoRestore
    var testLong: Long = 0

    @AutoRestore
    var testLong2: Long? = null

    @AutoRestore
    var testShort: Short = 0

    @AutoRestore
    var testShort2: Short? = null

    @AutoRestore
    var testBool: Boolean = false

    @AutoRestore
    var testBool2: Boolean? = null

    @AutoRestore
    var testChar: Char = ' '

    @AutoRestore
    var testChar2: Char? = null

    @AutoRestore
    var testByte: Byte = 0

    @AutoRestore
    var testByte2: Byte? = null

    @AutoRestore
    var testFloat: Float = 0.toFloat()

    @AutoRestore
    var testFloat2: Float? = null

    @AutoRestore
    var testDouble: Double = 0.toDouble()

    @AutoRestore
    var testDouble2: Double? = null

    @AutoRestore
    var serializable: Serializable? = null

    @AutoRestore
    var iBinder: IBinder? = null

    @AutoRestore
    var bundle: Bundle? = null

    @AutoRestore
    var charSequence: CharSequence? = null

    @AutoRestore
    var parcelable: Parcelable? = null

    @AutoRestore
    var size: Size? = null

    @AutoRestore
    var sizeF: SizeF? = null

    @AutoRestore
    var data: String? = null

    @AutoRestore
    var byteArray: ByteArray? = null

    @AutoRestore
    var shortArray: ShortArray? = null

    @AutoRestore
    var charArray: CharArray? = null

    @AutoRestore
    var floatArray: FloatArray? = null

    @AutoRestore
    var charSequenceArray: Array<CharSequence>? = null

    @AutoRestore
    var parcelableArray: Array<Parcelable>? = null

    @AutoRestore
    var response: Response? = null

    @AutoRestore
    var responseList: List<Response>? = null

    lateinit var mBinding: ActivityKotlinBinding

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kotlin)

        refresh()
        Response::class.javaObjectType

        mBinding.assignValue.setOnClickListener({ _ ->
            testInt = 1
            testInt2 = 2
            testLong = 1000
            testLong2 = 2000L
            testChar = 'c'
            testChar2 = 'b'
            testBool = true
            testBool2 = true
            testShort = 10
            testShort2 = 20
            testByte = 51
            testByte2 = 52
            testFloat = 1.0f
            testFloat2 = 2.0f
            testDouble = 3.0
            testDouble2 = 4.0
            serializable = object : HashMap<String, String>() {
                init {
                    put("key", "value")
                }
            }
            //            iBinder =
            bundle = Bundle()
            bundle!!.putString("testBundle", "stringInBundle")
            charSequence = "testCharSequence"
            size = Size(110, 120)
            sizeF = SizeF(110.110f, 120.120f)
            val b = Bundle()
            b.putString("testParcelable", "testParcelable")
            parcelable = b
            data = "testString"

            byteArray = byteArrayOf(1, 2, 3, 4)
            shortArray = shortArrayOf(10, 20, 30, 40)
            charArray = charArrayOf('a', 'b', 'c', 'd')
            floatArray = floatArrayOf(1.1f, 2.2f, 3.3f, 4.4f)

            charSequenceArray = arrayOf("c1", "c2")
            parcelableArray = arrayOf(Point(7, 7), Point(8, 8))

            response = Response(200)
            responseList = Arrays.asList(Response(500), Response(404))

            refresh()
        })
    }

    private fun refresh() {
        mBinding.aTestInt.setText(testInt.toString())
        mBinding.aTestInt2.setText(testInt2.toString())
        mBinding.aTestLong.setText(testLong.toString())
        mBinding.aTestLong2.setText(testLong2.toString())
        mBinding.aTestShort.setText(testShort.toString())
        mBinding.aTestShort2.setText(testShort2.toString())
        mBinding.aTestBool.setText(testBool.toString())
        mBinding.aTestBool2.setText(testBool2.toString())
        mBinding.aTestChar.setText(testChar.toString())
        mBinding.aTestChar2.setText(testChar2.toString())
        mBinding.aTestByte.setText(testByte.toString())
        mBinding.aTestByte2.setText(testByte2.toString())
        mBinding.aTestFloat.setText(testFloat.toString())
        mBinding.aTestFloat2.setText(testFloat2.toString())
        mBinding.aTestDouble.setText(testDouble.toString())
        mBinding.aTestDouble2.setText(testDouble2.toString())
        mBinding.aTestSerializable.setText(serializable.toString())
        //        mBinding.aTestSerializable.setText(iBinder.toString());
        mBinding.aTestBundle.setText(if (bundle == null) "null" else bundle!!.getString("testBundle").toString())
        mBinding.aTestCharSequence.setText(charSequence.toString())
        mBinding.aTestString.setText(data)
        mBinding.aTestSize.setText(size.toString())
        mBinding.aTestSizeF.setText(sizeF.toString())
        mBinding.aTestParcelable.setText(if (parcelable == null) "null" else (parcelable as Bundle).getString("testParcelable").toString())

        mBinding.aTestByteArray.setText(Arrays.toString(byteArray))
        mBinding.aTestCharArray.setText(Arrays.toString(charArray))
        mBinding.aTestShortArray.setText(Arrays.toString(shortArray))
        mBinding.aTestFloatArray.setText(Arrays.toString(floatArray))

        mBinding.aTestParcelableArray.setText(Arrays.toString(parcelableArray))
        mBinding.aTestCharSequenceArray.setText(Arrays.toString(charSequenceArray))

        mBinding.aTestUserList.setText(responseList.toString())
        mBinding.aTestUser.setText(response.toString())

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}
