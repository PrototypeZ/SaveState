# SaveState
[ ![Download](https://api.bintray.com/packages/prototypez/maven/save-state/images/download.svg) ](https://bintray.com/prototypez/maven/save-state/_latestVersion)


![](https://raw.githubusercontent.com/PrototypeZ/SaveState/master/logo.png)


自动恢复 Activity、Fragment 以及 View 的状态。

无需任何类似 `onSaveInstanceState` 以及 `onRestoreInstanceState` 的模板代码。

## 如何使用

在成员变量上标记 `@AutoRestore` 注解。

#### 在 Activity 中使用：

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

#### 在 Fragment 中使用：


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


#### 在自定义 View 中使用：


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

没错，就这么简单，只需要在变量上标记 `@AutoRestore` 注解。

## 现在就接入 SaveState

在项目根目录的 `build.gradle` 中增加以下内容：

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

SaveState 当前最新版本： [ ![Download](https://api.bintray.com/packages/prototypez/maven/save-state/images/download.svg) ](https://bintray.com/prototypez/maven/save-state/_latestVersion)

然后在您的项目的 **application** 模块的 `build.gradle` 中应用插件：
```groovy
apply plugin: 'com.android.application'
apply plugin: 'save.state'
```

如果你的项目中还有其他 **library** 模块也需要使用 SaveState，那么只需要在对应模块的 `build.gradle` 中也应用插件即可：

```groovy
apply plugin: 'com.android.library'
apply plugin: 'save.state'
```

## 支持的变量类型

如果被 `@AutoRestore` 标记的字段是下列类型之一，那么不需要额外的配置, SaveState 就可以帮您自动保存与恢复变量。


基本数据类型（包装类型） | 对象           | 数组
-----------------------|----------------|-------
int/Integer            | Serializable   | byte[]
long/Long              | IBinder        | short[]
short/Short            | Bundle         | char[]
float/Float            | CharSequence   | float[]
double/Double          | Parcelable     | CharSequence[]
byte/Byte              | Size           | Parcelable[]
char/Character         | SizeF          |
boolean/Boolean        | String         |

## 自定义变量类型

如果您需要自动保存与恢复的变量不属于上面类型中的任意一种，并且您的变量类型可以被序列化为 `JSON` ,
那么 SaveState 可以通过把这个对象和与它对应的 `JSON` 字符串之间的序列化和反序列化操作, 来实现变量的自动保存与恢复。

例如自定义对象：

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

类似这样的对象都可以通过 SaveState 来自动恢复：

```java
public class MyActivity extends Activity {

    @AutoRestore
    User user;

    @AutoRestore
    NetworkResponse<List<User>> response;
}
```

额外的配置操作有：

1. 确保项目中已引入 **支持的`JSON`序列化库** 之一
2. 在模块( **com.android.application** / **com.android.library** )的 `build.gradle` 中加入配置：
```groovy
defaultConfig {

    // your other configs

    // 配置 JSON 序列化库
    javaCompileOptions {
        annotationProcessorOptions {
            arguments = [ serializer : "/*JSON库*/" ]
        }
    }
}
```

目前支持的 `JSON` 序列化库有：

+ [gson](https://github.com/google/gson)
+ [fastjson](https://github.com/alibaba/fastjson)
+ [jackson](https://github.com/FasterXML/jackson)

## FAQ

+ Q: SaveState 支持 Instant Run 吗？

  A: 是的，基于 Transform API, 所以支持。

+ Q: 需要配置 Proguard 混淆规则吗？

  A: 没有使用任何反射，不需要额外配置。

+ Q: SaveState 性能如何？

  A: 当前版本基于编译时字节码修改以及 AnnotationProcessor，所以运行时性能几乎和手写是一样的，编译时因为插入了额外的任务，会稍微影响编译速度，但是根据实测，这点影响是可以忽略不计的。

+ Q: SaveState 大吗？会影响打包出来 APK 大小吗？

  A: SaveState 主要工作都在编译时完成，运行时没有任何依赖，不用担心影响 APK 大小。

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