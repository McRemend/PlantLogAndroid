# 🤖 GitHub Actions 自动打包指南

## ✅ 已配置完成

GitHub Actions 工作流文件已创建：`.github/workflows/android-build.yml`

---

## 📋 使用步骤

### 步骤 1：创建 GitHub 仓库（如果还没有）

```bash
cd /root/.openclaw/workspace/PlantLogAndroid-hilt-backup
git init
git add .
git commit -m "Initial commit: PlantLog Android App"
```

### 步骤 2：关联远程仓库

```bash
# 在 GitHub 上创建一个新仓库（私有或公开）
# 然后执行：
git remote add origin https://github.com/你的用户名/PlantLogAndroid.git
git branch -M main
git push -u origin main
```

### 步骤 3：触发自动打包

#### 方式 A：Push 代码自动构建
```bash
# 每次 push 到 main 分支都会自动构建
git add .
git commit -m "更新功能"
git push
```

#### 方式 B：创建 Release Tag（推荐）
```bash
# 创建版本 tag，会自动构建并标记为 release
git tag v1.0.0
git push origin v1.0.0
```

#### 方式 C：手动触发
1. 进入 GitHub 仓库页面
2. 点击 **Actions** 标签
3. 选择 **🤖 Android APK Build** 工作流
4. 点击 **Run workflow** 按钮
5. 选择分支，点击 **Run workflow**

---

## 📦 下载 APK

构建完成后，APK 会保存在 **Actions Artifacts** 中：

1. 进入 GitHub 仓库 → **Actions** 标签
2. 点击最近的构建记录
3. 在页面底部找到 **Artifacts** 区域
4. 点击 `PlantLog-debug` 或 `PlantLog-release` 下载 APK
5. 解压后即可得到 APK 文件

**注意**：Artifacts 保留 30 天，过期自动删除。

---

## 🔐 Release APK 签名（可选）

如果需要生成正式签名的 Release APK（用于 Google Play 上传），需要配置签名：

### 步骤 1：生成签名密钥库（本地执行一次）

```bash
keytool -genkey -v -keystore plantlog-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias plantlog
```

### 步骤 2：将密钥库上传到 GitHub Secrets

1. 进入 GitHub 仓库 → **Settings** → **Secrets and variables** → **Actions**
2. 添加以下 Secrets：
   - `KEYSTORE_BASE64`: 将密钥库文件 base64 编码后的内容
     ```bash
     base64 -w0 plantlog-release-key.jks
     ```
   - `KEYSTORE_PASSWORD`: 密钥库密码
   - `KEY_PASSWORD`: 密钥密码
   - `KEY_ALIAS`: 密钥别名（如 plantlog）

### 步骤 3：修改 build.gradle.kts 添加签名配置

在 `app/build.gradle.kts` 的 `android` 块中添加：

```kotlin
signingConfigs {
    create("release") {
        val keystoreFile = file("${System.getenv("GITHUB_WORKSPACE")}/plantlog-release-key.jks")
        if (keystoreFile.exists()) {
            storeFile = keystoreFile
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
}

buildTypes {
    release {
        signingConfig = signingConfigs.getByName("release")
        minifyEnabled(true)
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
}
```

---

## 📊 构建时长估算

| 步骤 | 预计时长 |
|------|---------|
| Checkout 代码 | 10-20 秒 |
| 设置 JDK 17 | 15-30 秒 |
| 设置 Android SDK | 30-60 秒（有缓存后更快） |
| Gradle 依赖下载 | 1-2 分钟（有缓存后秒下） |
| 编译 APK | 2-4 分钟 |
| 上传 Artifacts | 10-30 秒 |
| **总计** | **5-8 分钟** |

---

## 🆘 常见问题

### Q1: 构建失败怎么办？
查看 Actions 日志，点击失败的步骤查看错误信息。常见问题：
- 依赖下载失败 → 检查网络或重试
- 编译错误 → 查看具体错误信息修复代码

### Q2: 如何下载历史版本的 APK？
进入 **Actions** → 选择历史构建记录 → 下载 Artifacts

### Q3: 免费额度够用吗？
个人账号每月 **2000 分钟** 免费额度，每次构建约 5-8 分钟，可以构建 **250-400 次/月**。

### Q4: 可以自动发布到 Google Play 吗？
可以！需要额外配置 Google Play Developer API 和服务账号密钥。如需配置请告诉我。

---

## 🎯 下一步

1. **立即测试**：push 代码到 GitHub，验证自动构建是否正常工作
2. **配置签名**（可选）：如果需要正式发布的 APK
3. **配置自动发布**（可选）：自动上传 Google Play

有问题随时在群里问我！🫘
