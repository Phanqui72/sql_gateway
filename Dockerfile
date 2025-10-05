# Sử dụng một base image của Tomcat.
# Chọn phiên bản Tomcat hỗ trợ Java 17 như trong file pom.xml của bạn.
# Tomcat 10.1.x là lựa chọn tốt cho Jakarta EE 10 (phù hợp với các dependency jakarta.* của bạn).
FROM tomcat:10.1-jdk17-temurin

# Ghi đè file cấu hình mặc định của Tomcat Manager để tránh các lỗi không cần thiết khi deploy.
# Điều này giúp image gọn nhẹ hơn.
RUN rm -rf /usr/local/tomcat/webapps/*

# Đặt biến môi trường để thông báo cho Render biết ứng dụng sẽ lắng nghe ở cổng nào.
# Tomcat mặc định chạy ở cổng 8080.
ENV PORT 8080

# Sao chép file .war đã được build bởi Maven vào thư mục webapps của Tomcat.
# Maven sẽ build ra file tên là "your-webapp.war" dựa trên <finalName> trong pom.xml.
# Chúng ta đổi tên nó thành "ROOT.war" để ứng dụng chạy ngay tại đường dẫn gốc (/).
COPY target/your-webapp.war /usr/local/tomcat/webapps/ROOT.war

# Lệnh để khởi động server Tomcat khi container được chạy.
CMD ["catalina.sh", "run"]