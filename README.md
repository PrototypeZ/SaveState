# SaveState
![](https://raw.githubusercontent.com/PrototypeZ/SaveState/master/logo.png)

自动恢复 Activity、Fragment 以及 View 的状态, 无需任何类似 `onSaveInstanceState` 以及 `onRestoreInstanceState` 的模板代码

## 如何使用

在需要自动恢复的变量上标记 `@AutoRestore` 注解。

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

没错，就这么简单，只需要在你在希望能自动恢复的变量上标记 `@AutoRestore` 注解。

## 现在就接入 SaveState

在项目根目录的 `build.gradle` 中增加以下内容：

```groovy
buildscript {

    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.2'
        classpath 'io.github.prototypez:save-state:1.0'
    }
}
```

在 **application** 模块或 **library** 模块应用插件：

```groovy
apply plugin: 'com.android.application'
// apply plugin: 'com.android.library'
apply plugin: 'save.state'
```

在 **application** 模块或 **library** 增加依赖：

```groovy
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    // your own dependencies here...

    implementation 'io.github.PrototypeZ:SaveState:1.0'
}
```

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