# --- STAGE 1: Build aplication with Maven ---
# Sử dụng một image có sẵn Maven và JDK 17 để build dự án
FROM maven:3.8-jdk-17 AS builder

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Sao chép file pom.xml vào trước để tận dụng Docker cache.
# Nếu file pom không đổi, các dependency sẽ không cần tải lại.
COPY pom.xml .

# Sao chép toàn bộ source code vào
COPY src ./src

# Chạy lệnh Maven để build dự án và tạo ra file .war
# -DskipTests để bỏ qua việc chạy test, giúp build nhanh hơn
RUN mvn package -DskipTests


# --- STAGE 2: Run application with Tomcat ---
# Sử dụng image Tomcat mà bạn đã chọn ban đầu
FROM tomcat:10.1-jdk17-temurin

# Xóa các ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Đặt biến môi trường PORT cho Render
ENV PORT 8080

# Sao chép file .war đã được build ở STAGE 1 (builder) vào thư mục webapps của Tomcat
# Docker sẽ tự động tìm file này trong image của stage "builder"
COPY --from=builder /app/target/your-webapp.war /usr/local/tomcat/webapps/ROOT.war

# Lệnh để khởi động server Tomcat
CMD ["catalina.sh", "run"]