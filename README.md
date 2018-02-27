## 说明
### 来源：
该项目主要是修改自：https://github.com/zhanxiaokai/Android-AudioPlayer
非常感谢原作者的奉献！
### 目的：
原项目是Eclipse工程，现在改由AndroidStudio工程，并且C代码由CMake进行编译
### 改动
1. Android.mk文件修改为CMakeLists.txt
2. compileSdkVersion修改为25，增加了读取SD卡的权限请求
3. 音频文件放在SD卡的根目录下

		playFilePath = SDCardUtils.getExternalSdCardPath(this) + File.separator + "aotu.mp3";

4. 增加pthread调用方法的返回值，否则在结束线程时会发生崩溃问题
5. 修复了获取当前播放时间的除0问题
6. 由于之前创建工程时使用自己的包名，所以跟原作者不一样，非常抱歉！
7. 其中定然有很多不足之处，请大家斧正！