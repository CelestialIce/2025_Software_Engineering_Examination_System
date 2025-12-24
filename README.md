# 2025计算机科学技术软件工程大作业

本项目是一个在线测试系统，包含一个 Vue/uni-app 前端和一个 Java Spring Boot 后端。

本文档提供了纯粹通过命令行在本地环境中设置和运行此项目的完整说明。

## 1. 架构概览

- **前端**: `uni-app` 项目，位于 `FrontEnd/demo1`。通过 `vue-cli` 在本地运行。
- **后端**: Spring Boot 项目，位于 `BackEnd/OnlineTestBackEnd`。通过 `Maven` 运行。
- **数据库**: 使用 MySQL。

## 2. 先决条件

在开始之前，请确保您的系统上安装了以下软件。所有操作都通过命令行完成。

### a. 安装 Java 17

推荐使用 [SDKMAN](https://sdkman.io/) 来管理Java版本。

```bash
# 安装 SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 安装 Java 17
sdk install java 17.0.10-tem
sdk use java 17.0.10-tem
```

### b. 安装 Maven

您可以使用 Homebrew (macOS) 或 apt (Debian/Ubuntu) 来安装。

```bash
# macOS 使用 Homebrew
brew install maven

# Ubuntu/Debian 使用 apt
# 更新软件包列表
sudo apt update
# 安装 Maven (这会自动安装 OpenJDK)
sudo apt install maven -y
```

### c. 安装 Node.js 和 npm

推荐使用 [nvm](https://github.com/nvm-sh/nvm) 来管理 Node.js 版本。

```bash
# 安装 nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"

# 安装 Node.js (将同时安装 npm)
nvm install --lts
nvm use --lts
```

### d. 安装 MySQL (备选方案)

如果不使用Docker，你可以选择在本地直接安装MySQL。

```bash
# macOS 使用 Homebrew
brew install mysql
brew services start mysql

# Ubuntu/Debian 使用 apt
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
```

### e. 安装 Docker (推荐方案)
如果你的系统中没有安装MySQL，或希望环境隔离，使用Docker是更便捷的选择。
请访问 Docker 官方网站，根据你的操作系统进行安装: [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/)


## 3. 数据库设置

你有两种方式设置数据库：**a) 使用Docker (推荐)** 或 **b) 本地安装MySQL**。

### a. 数据库设置 (使用Docker)

1.  确保 Docker Desktop 或 Docker Engine 已经启动。
2.  打开终端并运行以下命令来启动一个MySQL容器。此命令会自动创建 `testbase` 数据库并将 `root` 密码设置为 `123456`。

    ```bash
    docker run --name mysql-for-onlinetest -p 4000:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=testbase -d mysql:8.0
    ```
3.  数据库现在已在后台运行于 `localhost:4000`，并准备好接受来自后端服务的连接。后端配置已指向此地址。
4.  (可选) 如果需要停止数据库，可以运行 `docker stop mysql-for-onlinetest`。

### b. 数据库设置 (本地安装MySQL)

1.  确保你的本地MySQL服务正在运行。
2.  登录到您的 MySQL 客户端：

    ```bash
    # 您可能需要 sudo
# -h 指定主机，-P 指定端口（注意是大写 P）
mysql -h 127.0.0.1 -P 4000 -u root -p
    ```
    (根据您的MySQL设置输入密码, 本项目配置为 `123456`)

3.  创建一个新的数据库：

    ```sql
    CREATE DATABASE testbase CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```
3.  退出MySQL客户端。后端服务 (Spring Boot) 将在首次启动时自动处理表的创建（如果配置了 `spring-jpa-hibernate-ddl-auto`）。如果程序启动失败提示表不存在，您需要根据 `database` 文件夹中的 `.csv` 文件手动创建表并导入数据。

## 4. 后端设置并运行

1.  打开一个 **新的终端**。

2.  导航到 `OnlineTestBackEnd` 目录并使用 Maven 启动 Spring Boot 应用：

    ```bash
    cd BackEnd/OnlineTestBackEnd
    mvn spring-boot:run
# mvn clean install && mvn spring-boot:run
    ```

3.  服务将启动在 `http://localhost:81`。请保持此终端运行。

## 5. 前端设置并运行

1.  打开 **另一个新的终端**。

2.  导航到 `demo1` 目录：

    ```bash
    cd FrontEnd/demo1
    ```

3.  安装项目依赖。`node_modules` 目录可能已存在，但最好重新安装以确保所有 `vue-cli` 依赖都已就位。

    ```bash
    npm install
    ```

4.  启动前端开发服务器：

    ```bash
    npm run dev:h5
    ```

5.  应用现在应该可以通过浏览器访问。终端输出会显示确切的地址，通常是 `http://localhost:8080`。

现在，您的浏览器将打开前端应用，该应用会与在 `localhost:81` 上运行的后端服务进行通信。
