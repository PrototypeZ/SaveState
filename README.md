# SaveState
[ ![Download](https://api.bintray.com/packages/prototypez/maven/save-state/images/download.svg) ](https://bintray.com/prototypez/maven/save-state/_latestVersion)


![](https://raw.githubusercontent.com/PrototypeZ/SaveState/master/logo.png)

[**中文文档**](https://github.com/PrototypeZ/SaveState/blob/master/README_zh.md)

Automatically save and restore states of Activities, Fragments and Views.

No boilerplate code like `onSaveInstanceState` or `onRestoreInstanceState` any more.

## Getting started

Just add the `@AutoRestore` annotation to your fields that need to be saved and restored in Activities, Fragments and Views.

#### In Activities:

```kotlin
class MyActivity : Activity() {

    @AutoRestore
    var myInt: Int = 0;

    @AutoRestore
    var myRpcCall: IBinder? = null;

    @AutoRestore
    var result: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        // Your code here
    }
}
```

#### In Fragments：


```kotlin
class MyFragment : Fragment() {

    @AutoRestore
    var currentLoginUser: User? = null;

    @AutoRestore
    var networkResponse: List<Map<String, Object>>? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Your code here
    }
}
```


#### In Views：


```kotlin
class KotlinView : FrameLayout {

    @AutoRestore
    val someText: String? = null;

    @AutoRestore
    val size: Size? = null;

    @AutoRestore
    val myFloatArray: FloatArray? = null;

    constructor(context: Context) : super(context) {
        // your code here
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        // your code here
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        // your code here
    }

}
```

Yes, that's all.

## Setting up the dependency of SaveState

Add the plugin classpath to the `build.gradle` file in project root as below:

```groovy
buildscript {

    repositories {
        google()
        jcenter()
    }
    dependencies {
        // your other dependencies

        // dependency for save-state
        classpath "io.github.prototypez:save-state:${latest_version}"
    }
}
```

Currently the latest version of SaveState is: [ ![Download](https://api.bintray.com/packages/prototypez/maven/save-state/images/download.svg) ](https://bintray.com/prototypez/maven/save-state/_latestVersion)

Then apply the SaveState plugin in your `build.gradle` file of your **application** module:

```groovy
apply plugin: 'com.android.application'
apply plugin: 'save.state'
```

If your module contains `kotlin` code， please make sure `kotlin-kapt` plugin is also included：
```groovy
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'
apply plugin: 'save.state'
```

If you have **library** modules and they also need SaveState support, then the only thing you need to do is applying the plugin in their `build.gradle` files just as the **application** module.

```groovy
apply plugin: 'com.android.library'
apply plugin: 'save.state'
```

If this **library** module contains `kotlin` code，just follow the same tips in **application** module above：
```groovy
apply plugin: 'com.android.library'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'
apply plugin: 'save.state'
```


> Note：In pure Java module，SaveState use `annotationProcessor` by default;
In module that contains `kotlin` code, SaveState use `kotlin-kapt` plugin instead.
If you are using SaveState plugin in module that contains `kotlin` code,
please make sure other annotation processor framework are using `kapt` plugin too.
For example, Dagger and  DeepLinkDispatch processor should be included as follows：
> ```groovy
> kapt "com.google.dagger:dagger-compiler:${dagger_version}"
> kapt "com.airbnb:deeplinkdispatch-processor:${deeplinkdispatch_version}"
> ```


## Supported Types

If the type of a field annotated by the `@AutoRestore` annotation is one of the types below, then there's no configurations any more. SaveState will save and restore your field automatically.


Primitive types( including boxed types ) | Classes and interfaces | Array types
-----------------------|----------------|-------
int/Integer            | Serializable   | byte[]
long/Long              | IBinder        | short[]
short/Short            | Bundle         | char[]
float/Float            | CharSequence   | float[]
double/Double          | Parcelable     | CharSequence[]
byte/Byte              | Size           | Parcelable[]
char/Character         | SizeF          |
boolean/Boolean        | String         |

## Other types

If the type of a field is not included in what is listed above, but it can be serialized to `JSON` ,
then SaveState could still save and restore it automatically by serializing it to `JSON` and deserializing the `JSON` string back to object.

For example：

```kotlin
class User (
    val name: String,
    val age: Int
)


class NetworkResponse<T> (
    val resultCode: Int,
    val data: T?
)
```

These types are supported by SaveState according to the rule above:

```kotlin
class MyActivity : Activity() {

    @AutoRestore
    var user: User? = null

    @AutoRestore
    var response: NetworkResponse<List<User>>? = null
}
```

But we need extra configurations now：

1. Make sure your have already included one of the dependencies of **supported `JSON` processing library** .
2. Add compile options in the `build.gradle` file ( **application** module or  **library** module ),

For pure Java modules:
```groovy
defaultConfig {

    // your other configs

    // config the JSON processing library
    javaCompileOptions {
        annotationProcessorOptions {
            arguments = [ serializer : "/*Supported JSON processing library*/" ]
        }
    }
}
```

For modules that contain `kotlin` code:
```groovy
kapt {
    arguments {
        arg("serializer", "/*JSON库*/")
    }
}
```


Currently the **supported `JSON` processing library** includes：

+ [gson](https://github.com/google/gson)
+ [fastjson](https://github.com/alibaba/fastjson)
+ [jackson](https://github.com/FasterXML/jackson)

## FAQ

+ Q: Does SaveState support Instant Run?

  A: Yes，it's based on Transform API, so no problem.

+ Q: Do I need to add any Proguard rules for release?

  A: No, there's no reflection, so you are safe to use Proguard.

+ Q: What about the performance of SaveState?

  A: It's based on byte code transforming and annotation processor, so the runtime performance is as good as other compiled code written by yourself.
  It will only have a little influence on the compile time, but I think it can be ignored.

+ Q: What about the size of SaveState? How much will it increase the APK size?

  A: SaveState do all the jobs at compile time，it doesn't have any runtime dependency, so it won't increase your APK size.

## LICENSE

    Copyright (c) 2016-present, SateState Contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.