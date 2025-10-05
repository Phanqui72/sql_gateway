package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // 1. Tải trình điều khiển
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Trình điều khiển JDBC đã được tải thành công.");

            // 2. Thông tin kết nối (sao chép y hệt từ servlet của bạn)
            String serverName = "LAPTOP-ME92KU5Q";
            String instanceName = "SQLDEV";
            String databaseName = "murachdb";
            String username = "WebAppUser";
            String password = "123";

            // 3. Tạo chuỗi URL kết nối
            String dbURL = String.format("jdbc:sqlserver://%s;instanceName=%s;databaseName=%s;encrypt=true;trustServerCertificate=true;loginTimeout=10;",
                    serverName, instanceName, databaseName);
            System.out.println("Đang cố gắng kết nối tới: " + dbURL);

            // 4. Thực hiện kết nối
            connection = DriverManager.getConnection(dbURL, username, password);

            // 5. Kiểm tra kết quả
            if (connection != null) {
                System.out.println("--------------------------------------------------");
                System.out.println("✅ KẾT NỐI THÀNH CÔNG! (CONNECTION SUCCESSFUL!)");
                System.out.println("--------------------------------------------------");
                System.out.println("Điều này có nghĩa là code Java, thông tin đăng nhập và driver của bạn đều chính xác.");
            }

        } catch (SQLException e) {
            System.err.println("--------------------------------------------------");
            System.err.println("❌ KẾT NỐI THẤT BẠI! (CONNECTION FAILED!)");
            System.err.println("--------------------------------------------------");
            System.err.println("Lỗi SQL: " + e.getMessage());
            System.err.println("Đây là dấu hiệu mạnh mẽ của vấn đề mạng (Firewall) hoặc sai thông tin đăng nhập.");
            e.printStackTrace(); // In ra chi tiết lỗi để phân tích
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy lớp trình điều khiển SQL Server.");
            System.err.println("Hãy kiểm tra lại dependency 'mssql-jdbc' trong file pom.xml.");
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Đã đóng kết nối.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}