# SaveState
[ ![Download](https://api.bintray.com/packages/prototypez/maven/save-state/images/download.svg) ](https://bintray.com/prototypez/maven/save-state/_latestVersion)


![](https://raw.githubusercontent.com/PrototypeZ/SaveState/master/logo.png)

[**中文文档**](https://github.com/PrototypeZ/SaveState/blob/master/README_zh.md)

Automatically save and restore states of Activities, Fragments and Views.

No boilerplate code like `onSaveInstanceState` or `onRestoreInstanceState` any more.

## Getting started

Just add the `@AutoRestore` annotation to your fields that need to be saved and restored in Activities, Fragments and Views.

#### In Activities:

```java
public class MyActivity extends Activity {

    @AutoRestore
    int myInt;

    @AutoRestore
    IBinder myRpcCall;

    @AutoRestore
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Your code here
    }
}
```

#### In Fragments：


```java
public class MyFragment extends Fragment {

    @AutoRestore
    User currentLoginUser;

    @AutoRestore
    List<Map<String, Object>> networkResponse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Your code here
    }
}
```


#### In Views：


```java
public class MyView extends View {

    @AutoRestore
    String someText;

    @AutoRestore
    Size size;

    @AutoRestore
    float[] myFloatArray;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

If you have **library** modules and they also need SaveState support, then the only thing you need to do is applying the plugin in their `build.gradle` files just as the **application** module.

```groovy
apply plugin: 'com.android.library'
apply plugin: 'save.state'
```

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

```java
public class User {
    String name;
    int age;
}


public class NetworkResponse<T> {
    int resultCode;

    T data
}
```

These types are supported by SaveState according to the rule above:

```java
public class MyActivity extends Activity {

    @AutoRestore
    User user;

    @AutoRestore
    NetworkResponse<List<User>> response;
}
```

But we need extra configurations now：

1. Make sure your have already included one of the dependencies of **supported `JSON` processing library** .
2. Add compile options in the `build.gradle` file ( **application** module or  **library** module ) as below:

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

Currently the **supported `JSON` processing library** includes：

+ [gson](https://github.com/google/gson)
+ [fastjson](https://github.com/alibaba/fastjson)
+ [jackson](https://github.com/FasterXML/jackson)

## FAQ

+ Q: Does SaveState support Instant Run?

  A: Yes，it's based on Transform API, so no problem。

+ Q: Do I need to add any Proguard rules for release?

  A: No, there's no reflection, so you are safe to use Proguard.

+ Q: What about the performance of SaveState?

  A: It's based on byte code transforming and annotation processor, so the runtime performance is as good as other compiled code written by you.
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