#!/bin/bash

# 🚀 GitHub Actions 快速配置脚本

echo "🫘 植记 Android - GitHub Actions 快速配置"
echo "=========================================="
echo ""

# 检查是否在正确的目录
if [ ! -f "build.gradle.kts" ]; then
    echo "❌ 错误：请在 PlantLogAndroid-hilt-backup 目录下运行此脚本"
    exit 1
fi

echo "✅ 1. 初始化 Git 仓库..."
git init

echo "✅ 2. 添加所有文件..."
git add .

echo "✅ 3. 创建初始提交..."
git commit -m "Initial commit: PlantLog Android with GitHub Actions

- Complete Android project structure
- GitHub Actions workflow for auto-build
- Debug & Release APK artifacts
- Build summary in GitHub Actions"

echo ""
echo "=========================================="
echo "✅ Git 仓库初始化完成！"
echo ""
echo "📋 下一步操作："
echo ""
echo "1️⃣  在 GitHub 上创建新仓库（私有或公开）"
echo "   访问：https://github.com/new"
echo "   仓库名：PlantLogAndroid"
echo ""
echo "2️⃣  复制 GitHub 提供的远程仓库地址，然后执行："
echo "   git remote add origin https://github.com/你的用户名/PlantLogAndroid.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
echo "3️⃣  Push 成功后，GitHub Actions 会自动开始构建！"
echo "   查看构建进度：https://github.com/你的用户名/PlantLogAndroid/actions"
echo ""
echo "4️⃣  构建完成后，在 Actions → Artifacts 下载 APK"
echo ""
echo "📖 详细指南：查看 GITHUB-ACTIONS-GUIDE.md"
echo "=========================================="
echo ""
