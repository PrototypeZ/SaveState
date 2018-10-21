package io.github.prototypez.savestate.kotlin

import android.databinding.DataBindingUtil
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Size
import android.util.SizeF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.io.Serializable
import java.util.Arrays
import java.util.HashMap

import io.github.prototypez.savestate.core.annotation.AutoRestore
import io.github.prototypez.savestate.kotlin.databinding.FragmentKotlinBinding
import io.github.prototypez.savestate.kotlin.entity.Response
import java.io.File

class KotlinFragment : Fragment() {


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
    var serializable: File? = null

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

    lateinit var mBinding: FragmentKotlinBinding


    private fun refresh() {
        mBinding.aTestInt.text = testInt.toString()
        mBinding.aTestInt2.text = testInt2.toString()
        mBinding.aTestLong.text = testLong.toString()
        mBinding.aTestLong2.text = testLong2.toString()
        mBinding.aTestShort.text = testShort.toString()
        mBinding.aTestShort2.text = testShort2.toString()
        mBinding.aTestBool.text = testBool.toString()
        mBinding.aTestBool2.text = testBool2.toString()
        mBinding.aTestChar.text = testChar.toString()
        mBinding.aTestChar2.text = testChar2.toString()
        mBinding.aTestByte.text = testByte.toString()
        mBinding.aTestByte2.text = testByte2.toString()
        mBinding.aTestFloat.text = testFloat.toString()
        mBinding.aTestFloat2.text = testFloat2.toString()
        mBinding.aTestDouble.text = testDouble.toString()
        mBinding.aTestDouble2.text = testDouble2.toString()
        mBinding.aTestSerializable.text = serializable.toString()
        //        mBinding.aTestSerializable.setText(iBinder.toString());
        mBinding.aTestBundle.text = if (bundle == null) "null" else bundle!!.getString("testBundle").toString()
        mBinding.aTestCharSequence.text = charSequence.toString()
        mBinding.aTestString.text = data
        mBinding.aTestSize.text = size.toString()
        mBinding.aTestSizeF.text = sizeF.toString()
        mBinding.aTestParcelable.text = if (parcelable == null) "null" else (parcelable as Bundle).getString("testParcelable").toString()

        mBinding.aTestByteArray.text = Arrays.toString(byteArray)
        mBinding.aTestCharArray.text = Arrays.toString(charArray)
        mBinding.aTestShortArray.text = Arrays.toString(shortArray)
        mBinding.aTestFloatArray.text = Arrays.toString(floatArray)

        mBinding.aTestParcelableArray.text = Arrays.toString(parcelableArray)
        mBinding.aTestCharSequenceArray.text = Arrays.toString(charSequenceArray)

        mBinding.aTestUserList.text = responseList.toString()
        mBinding.aTestUser.text = response.toString()

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_kotlin, container, false)

        mBinding.assignValue.setOnClickListener { _ ->
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
            serializable = File("serializableFile")
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
        }

        mBinding.root.post { this.refresh() }
        return mBinding.root
    }
}
