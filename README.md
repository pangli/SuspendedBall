# SuspendedBall Android系统悬浮窗支持横竖屏切换
## 关于集成：
- **在应用模块的`build.gradle`添加：**
```
dependencies {
    implementation 'com.zorro.easyfloat:EasyFloat:0.0.9'
}
```
## 关于初始化：
- 全局初始化为非必须；
- **当浮窗为仅前台;**
- 需要在项目的`Application`中进行全局初始化，进行页面生命周期检测。
```
EasyFloat.init(this)
```
## 关于权限声明：
- 权限声明为非必须；
- **当使用到系统浮窗（`ShowPattern.FOREGROUND`）；**
- 需要在`AndroidManifest.xml`进行权限声明。
```
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

## 一行代码搞定Android浮窗，浮窗从未如此简单：
```
EasyFloat.with(this).show()
```
**点击回调**
```
.registerCallbacks(new OnFloatViewClick() {
    @Override
    public void onClick(View v) {
       Toast.makeText(MainActivity.this, "我被点击了", Toast.LENGTH_LONG).show();
    }
})
```
### 悬浮窗权限的检测、申请
- **无需主动进行权限申请**
```
// 权限检测
PermissionUtils.checkPermission(this)

// 权限申请，参数2为权限回调接口
PermissionUtils.requestPermission(this，OnPermissionResult)
```
### 系统浮窗的相关API：
```
// 关闭浮窗
dismissAppFloat(String tag)

// 隐藏浮窗
hideAppFloat(String tag)

// 显示浮窗
showAppFloat(String tag)
```
## 关于混淆：
```
-keep class com.zorro.easyfloat.** {*;}
```
## 关于感谢：
- **悬浮窗：[EasyFloat](https://github.com/princekin-f/EasyFloat)**

License
-------

   Copyright 2020 Pang Li

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.