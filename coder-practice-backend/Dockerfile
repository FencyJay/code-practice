# 使用官方 OpenJDK 21 作为基础镜像
FROM eclipse-temurin:21-jdk-jammy

# 设置工作目录
WORKDIR /app

# 复制 Maven 打包生成的 jar 到容器
COPY target/coder-practice-backend-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口（和 Spring Boot 配置一致）
EXPOSE 8001

# 启动命令，激活 prod 配置文件
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
