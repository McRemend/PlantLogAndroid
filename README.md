# 植记 PlantLog - Android

🌱 一款简洁优雅的植物护理追踪应用

## 项目概述

PlantLog 是一款专为植物爱好者设计的 Android 应用，帮助你轻松追踪和管理家中植物的护理记录。

### 核心功能

- **📋 植物列表** - 查看所有植物，快速了解浇水状态
- **📝 植物详情** - 查看详细信息和护理历史
- **📷 AI 识别** - 拍照自动识别植物品种（ML Kit）
- **💧 浇水提醒** - 定时通知，不再忘记浇水
- **📊 护理记录** - 记录每次浇水、施肥、修剪等操作

### 技术栈

- **UI 框架**: Jetpack Compose + Material Design 3
- **架构模式**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **本地数据库**: Room
- **后台任务**: WorkManager
- **图像识别**: ML Kit Image Labeling
- **异步处理**: Kotlin Coroutines + Flow

## 项目结构

```
PlantLogAndroid/
├── app/
│   ├── src/main/
│   │   ├── java/com/plantlog/app/
│   │   │   ├── data/              # 数据层
│   │   │   │   ├── local/         # Room: Entities, DAOs, Database
│   │   │   │   └── repository/    # Repositories
│   │   │   ├── domain/            # 领域层
│   │   │   │   ├── model/         # 领域模型
│   │   │   │   ├── mapper/        # 实体映射
│   │   │   │   └── usecase/       # 业务用例
│   │   │   ├── presentation/      # 表现层
│   │   │   │   ├── home/          # 首页
│   │   │   │   ├── detail/        # 详情页
│   │   │   │   ├── add/           # 添加植物页
│   │   │   │   └── navigation/    # 导航
│   │   │   ├── service/           # 服务层
│   │   │   │   ├── ML Kit 识别服务
│   │   │   │   └── WorkManager 提醒
│   │   │   ├── di/                # Hilt 依赖注入模块
│   │   │   ├── ui/theme/          # Compose 主题
│   │   │   └── PlantLogApplication.kt
│   │   └── res/                   # 资源文件
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 快速开始

### 环境要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34
- Gradle 8.0+

### 构建步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd PlantLogAndroid
   ```

2. **同步 Gradle**
   - 在 Android Studio 中打开项目
   - 等待 Gradle 同步完成

3. **运行应用**
   - 选择模拟器或真机
   - 点击 Run 按钮 (Shift + F10)

### 配置说明

#### ML Kit 配置

ML Kit 图像识别功能已集成，如需使用云端模型：

1. 在 Firebase 控制台创建项目
2. 下载 `google-services.json` 放到 `app/` 目录
3. 在 `build.gradle.kts` 中添加 Google Services 插件

#### 通知权限

Android 13+ 需要在运行时请求通知权限（代码中已预留）。

## 架构说明

### Clean Architecture 分层

```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│  (Compose UI + ViewModel + State)   │
├─────────────────────────────────────┤
│           Domain Layer              │
│     (Models + UseCases + Mapper)    │
├─────────────────────────────────────┤
│            Data Layer               │
│  (Repository + Room + ML Kit + WM)  │
└─────────────────────────────────────┘
```

### 数据流

```
UI Event → ViewModel → UseCase → Repository → Room/Service
              ↑                                      ↓
              └─────────── State Flow ──────────────┘
```

## 主要功能实现

### 1. 植物识别 (ML Kit)

```kotlin
// service/PlantRecognitionService.kt
suspend fun identifyPlantFromUri(imageUri: Uri): PlantRecognitionResult
```

- 使用 ML Kit Image Labeling
- 置信度阈值：50%
- 支持本地和云端模型

### 2. 浇水提醒 (WorkManager)

```kotlin
// service/ReminderScheduler.kt
fun scheduleDailyReminder() // 每天 9 点检查
```

- 每日定时检查
- 自动发送通知
- 支持后台执行

### 3. 本地数据库 (Room)

```kotlin
// data/local/PlantLogDatabase.kt
@Entity(tableName = "plants")
data class PlantEntity(...)

@Entity(tableName = "care_records")
data class CareRecordEntity(...)
```

- 植物表 + 护理记录表
- 外键关联，级联删除
- Flow 实时数据更新

## 定价策略

**￥12 元 买断制**

- 无订阅费用
- 无内购项目
- 一次购买，永久使用
- 免费更新

## 待办事项

- [ ] 完善通知权限请求逻辑
- [ ] 添加植物编辑功能
- [ ] 实现护理记录完整 CRUD
- [ ] 添加数据统计图表
- [ ] 支持数据导出/备份
- [ ] 多语言支持
- [ ] 深色主题优化
- [ ] 桌面小组件

## 开发规范

### 代码风格

- Kotlin 官方代码风格
- 类名：PascalCase
- 函数/变量：camelCase
- 常量：UPPER_SNAKE_CASE

### 提交规范

```
feat: 添加新功能
fix: 修复 bug
docs: 文档更新
style: 代码格式调整
refactor: 重构代码
test: 测试相关
chore: 构建/工具相关
```

## 许可证

Copyright © 2024 PlantLog. All rights reserved.

商业软件，未经授权禁止复制或分发。

## 联系方式

- Email: support@plantlog.app
- Website: https://plantlog.app

---

🌿 Happy Planting!
