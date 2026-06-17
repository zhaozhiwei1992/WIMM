# WIMM 后端运行镜像（纯运行时，jar 通过挂载引入）
#
# 镜像只含 JRE，不含 jar。jar 由 docker-compose 以数据卷挂载进容器（见 docker-compose.yml 的 volumes）。
# 好处：更新 jar 时无需重新 build/导出镜像，替换宿主机 jar + 重启容器即可；镜像可长期复用。
#
# ===== 构建（开发机或任意有 Docker 的机器，一次性）=====
#   cd WIMM/WIMM
#   docker build -f docker/Dockerfile -t wimm-server:latest .
# 然后导出到 NAS:
#   docker save wimm-server:latest -o wimm-server.tar
#   scp wimm-server.tar <NAS>:/opt/wimm/
# NAS 导入:
#   docker load -i /opt/wimm/wimm-server.tar
#
# 备选：若构建机性能足够，可用多阶段构建把编译也纳入镜像（取消下方 builder 段注释，
#       并在 ENTRYPOINT 用 COPY --from=builder ...）。弱机器（NAS）跑 mvn 易 OOM，故默认不内置编译。

# ---------- 备选：多阶段构建（构建机强时用）----------
# FROM maven:3.9-eclipse-temurin-21 AS builder
# WORKDIR /build
# COPY . .
# RUN mvn clean package -DskipTests
# ------------------------------------------------------------------------

# 运行阶段：仅 JRE，镜像小、启动快
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 时区
ENV TZ=Asia/Shanghai
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# jar 不打进镜像，由 compose 挂载到 /app/app.jar（见 docker-compose.yml volumes）。
# 多阶段模式则改用: COPY --from=builder /build/z-public-server/target/z-public-server.jar app.jar

# JVM 参数(可启动时覆盖: -e JAVA_OPTS="-Xmx512m"), prod profile 通过 compose/启动参数传入
ENV JAVA_OPTS=""
EXPOSE 8090

# 挂载点声明（文档化用；实际挂载在 compose 里配）
VOLUME ["/app"]

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
