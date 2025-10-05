# --- STAGE 1: Build application with Maven ---
# SỬA LỖI: Sử dụng một image Maven chính thức và ổn định có chứa JDK 17 (Eclipse Temurin).
# Đây là phiên bản đã được sửa lại từ "maven:3.8-jdk-17".
FROM maven:3.9.6-eclipse-temurin-17-focal AS builder

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Sao chép file pom.xml vào trước để tận dụng Docker cache.
COPY pom.xml .

# Tải các dependency trước khi sao chép source code.
# Nếu dependency không đổi, bước này sẽ được cache lại.
RUN mvn dependency:go-offline

# Sao chép toàn bộ source code vào
COPY src ./src

# Chạy lệnh Maven để build dự án và tạo ra file .war
# -DskipTests để bỏ qua việc chạy test, giúp build nhanh hơn
RUN mvn package -DskipTests


# --- STAGE 2: Run application with Tomcat ---
# Stage này vẫn giữ nguyên, nó đã chính xác.
FROM tomcat:10.1-jdk17-temurin

# Xóa các ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Đặt biến môi trường PORT cho Render
ENV PORT 8080

# Sao chép file .war đã được build ở STAGE 1 (builder) vào thư mục webapps của Tomcat
COPY --from=builder /app/target/your-webapp.war /usr/local/tomcat/webapps/ROOT.war

# Lệnh để khởi động server Tomcat
CMD ["catalina.sh", "run"]