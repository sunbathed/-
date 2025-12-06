# 📚 二手书交易系统 (Second Hand Book Trading System)

欢迎使用二手书交易系统！这是一个基于 **Vue 3** 和 **Spring Boot** 开发的前后端分离项目，专为大学生或社区设计的二手书籍交易平台。

本项目适合初学者学习全栈开发，涵盖了用户管理、图书发布、订单交易、后台审核等核心功能。

---

## 🛠️ 技术栈 (Tech Stack)

### 前端 (Frontend)
- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **语言**: TypeScript
- **样式**: Tailwind CSS
- **状态管理**: Pinia
- **路由**: Vue Router
- **图标库**: Lucide Vue Next
- **HTTP请求**: Axios

### 后端 (Backend)
- **框架**: Spring Boot 3.2.5
- **语言**: Java 17
- **数据库ORM**: Spring Data JPA (Hibernate)
- **数据库**: MySQL 8.0+
- **构建工具**: Maven

---

## 💻 环境准备 (Prerequisites)

在运行项目之前，请确保您的电脑上安装了以下软件：

1.  **Node.js**: (建议 v18+), 用于运行前端。 [下载地址](https://nodejs.org/)
2.  **JDK**: (建议 JDK 17), 用于运行后端。 [下载地址](https://www.oracle.com/java/technologies/downloads/)
3.  **MySQL**: (建议 v8.0), 数据库服务。 [下载地址](https://dev.mysql.com/downloads/mysql/)
4.  **Maven**: (可选，Idea自带), 用于构建后端项目。
5.  **代码编辑器**: 推荐使用 VS Code 或 IntelliJ IDEA。

---

## 🚀 快速开始 (Quick Start)

### 第一步：数据库配置 (Database Setup)

1.  **创建数据库**：
    打开您的 MySQL 客户端（如 Navicat, DBeaver 或 命令行），执行以下 SQL 语句创建一个名为 `second_hand_book` 的数据库：
    ```sql
    CREATE DATABASE second_hand_book CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```

2.  **导入数据（可选但推荐）**：
    项目目录下的 `scripts/` 文件夹中包含了一些初始化脚本。您可以运行 `enrich_data.sql` 来填充一些测试数据（用户、图书、订单等）。
    *   命令行方式：
        ```bash
        mysql -u root -p second_hand_book < scripts/enrich_data.sql
        ```
    *   或者在数据库客户端中打开该文件并运行。

3.  **修改后端配置**：
    打开文件 `backend/src/main/resources/application.yml`，找到以下配置并根据您的实际情况修改（如果您的密码不是 `0421`，请务必修改）：
    ```yaml
    spring:
      datasource:
        username: root      # 数据库用户名
        password: 0421      # 数据库密码 (请修改为您自己的密码)
    ```

### 第二步：启动后端 (Backend Startup)

1.  打开终端（Terminal），进入后端目录：
    ```bash
    cd backend
    ```
2.  运行 Spring Boot 应用：
    *   **Windows (CMD)**:
        ```bash
        mvnw spring-boot:run
        ```
        *(如果提示 mvnw 不是命令，请确保安装了 Maven 并配置了环境变量，或者直接在 IDEA 中运行 `SecondHandBookApplication.java`)*
    *   **或者使用 Maven 命令**:
        ```bash
        mvn spring-boot:run
        ```
3.  当看到日志中出现 `Started SecondHandBookApplication in ...` 时，表示后端已成功启动，默认端口为 **8080**。

### 第三步：启动前端 (Frontend Startup)

1.  打开一个新的终端窗口，进入前端目录：
    ```bash
    cd frontend
    ```
2.  安装依赖（仅第一次需要）：
    ```bash
    npm install
    ```
3.  启动开发服务器：
    ```bash
    npm run dev
    ```
4.  启动成功后，终端会显示访问地址（通常是 `http://localhost:5173`）。按住 `Ctrl` 点击链接即可在浏览器中打开。

---

## 🌟 功能说明 (Features)

### 👤 用户端
1.  **登录/注册**：支持用户注册和登录，登录背景图精美。
2.  **图书浏览**：首页展示所有在售图书，支持搜索。
3.  **图书详情**：查看图书详细信息、作者、价格、卖家信息。
4.  **购买图书**：点击“立即购买”，生成订单。
5.  **个人中心**：
    *   **我发布的**：查看自己发布的图书状态（审核中、在售、已驳回）。
    *   **我卖出的**：查看卖出的订单。
    *   **我买到的**：查看购买记录，支持“确认收货”。
6.  **发布图书**：上传图书信息（书名、价格、封面等），提交管理员审核。

### 🛡️ 管理员端
*   **入口**：通常通过数据库将用户角色 (`role`) 修改为 `admin` 后，在个人中心或专门的入口进入。
*   **图书审核**：审核用户发布的图书，点击“通过”后图书才会上架。
*   **举报处理**：处理用户对图书或他人的举报信息。

---

## ❓ 常见问题 (FAQ)

**Q1: 启动后端时报错 "Access denied for user 'root'..."**
A: 请检查 `backend/src/main/resources/application.yml` 中的数据库密码是否配置正确。

**Q2: 前端显示 "Network Error" 或无法加载数据**
A: 请确保后端服务已经启动（端口 8080），并且没有被防火墙拦截。

**Q3: 图片无法显示**
A: 项目默认使用网络图片或本地 SVG 占位符。如果自行上传图片，请确保图片 URL 有效。前端图片资源存放于 `frontend/public/picture`。

**Q4: 数据库表没有自动创建**
A: 后端配置了 `ddl-auto: update`，正常启动后端后会自动创建表。如果不行，请检查数据库连接是否正常。

---

## 📂 目录结构 (Directory Structure)

```
二手书交易系统/
├── backend/                # 后端项目根目录
│   ├── src/main/java/      # Java 源代码
│   ├── src/main/resources/ # 配置文件 (application.yml)
│   └── pom.xml             # Maven 依赖配置
├── frontend/               # 前端项目根目录
│   ├── src/                # Vue 源代码
│   │   ├── views/          # 页面组件 (Home, BookDetail, UserCenter...)
│   │   ├── stores/         # Pinia 状态管理
│   │   └── router/         # 路由配置
│   ├── public/             # 静态资源 (图片)
│   └── package.json        # npm 依赖配置
├── scripts/                # 数据库初始化脚本和工具脚本
└── README.md               # 项目说明文档
```

祝您使用愉快！如有问题，请随时联系开发者。
