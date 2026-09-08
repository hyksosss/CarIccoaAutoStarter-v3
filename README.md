# ICCOA 自动启动（Android 10 车机）

当无障碍服务观察到 `com.baony.avm360` 曾在前台、随后窗口切换至 `com.android.launcher` 时，应用在固定 100 ms 后启动 `com.ucarhu.demo/.UCarDemoActivity`。中间出现的非相关窗口不会清除状态；再次回到 360 会取消尚未执行的启动。

所有启动组件均为 Direct-Boot aware，应用默认使用 Device Protected Storage。这样服务可在 Android 10 的用户存储尚未解锁时被系统加载，避免 `Ignoring non-encryption-aware service`。该声明不会、也不能把一个已被系统关闭的无障碍服务重新写入 `Settings.Secure`；该操作需要系统特权。

## 使用

1. 将本应用加入车机开机自启白名单。其启动入口使用 `Theme.NoDisplay`，不会显示窗口。
2. 无障碍服务一旦由系统授权开启，会在重启后由 Android 恢复；前台保活服务则由开机广播及白名单启动。
3. **普通 APK 无法自行授予无障碍权限**。首次未授权时，必须由车机厂商使用系统签名/预置权限，或通过一次 ADB 写入安全设置完成授权。精简 ROM 缺少无障碍设置页时，无法以正常应用代码替代这一系统授权。

## 本地构建

需要 JDK 17、Android SDK Platform 30 和 Build Tools 30.0.3：

```powershell
gradle assembleRelease
```

产物：`app/build/outputs/apk/release/app-release.apk`。

## GitHub Actions

推送工程后在 Actions 中运行 **Build Android APK**，从该工作流的 Artifacts 下载 `ICCOA-AutoStarter-debug.apk`。
