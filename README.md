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

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

在使用 SaveState 的 Module ( **application** 模块或 **library** 模块 ) 应用插件：

```groovy
apply plugin: 'com.android.application'
// apply plugin: 'com.android.library'
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
byte/Byte              | Size           |
char/Character         | SizeF          |
boolean/Boolean        |                |

## 自定义变量类型

如果您需要自动保存与恢复的变量不属于上面类型中的任意一种，并且您的变量类型可以被序列化为 `JSON` ,
那么 SaveState 可以通过把这个对象和与它对应的 `JSON` 字符串之间的序列化和反序列化操作, 来实现变量的自动保存与恢复。

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
+ [jackson](https://github.com/FasterXML/jackson) (currently not available)



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