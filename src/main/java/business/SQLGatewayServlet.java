package business;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.SQLUtil;

@WebServlet("/sqlGateway")
public class SQLGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";

        try {
            // Tải trình điều khiển JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // ---===[ PHẦN THÔNG TIN KẾT NỐI ĐÃ ĐƯỢC CẬP NHẬT ]===---

            // Thông tin server và instance của bạn
            String serverName = "LAPTOP-ME92KU5Q";
            String instanceName = "SQLDEV";

            // Thông tin database và tài khoản của bạn
            String databaseName = "murachdb";
            String username = "WebAppUser";
            String password = "123";

            // Chuỗi kết nối URL được xây dựng cho named instance
            String dbURL = String.format("jdbc:sqlserver://%s;instanceName=%s;databaseName=%s;encrypt=true;trustServerCertificate=true;",
                    serverName, instanceName, databaseName);

            // Thiết lập kết nối
            Connection connection = DriverManager.getConnection(dbURL, username, password);

            // ---===[ KẾT THÚC PHẦN CẬP NHẬT ]===---

            Statement statement = connection.createStatement();

            sqlStatement = sqlStatement.trim();
            if (sqlStatement.length() >= 6) {
                String sqlType = sqlStatement.substring(0, 6);
                if (sqlType.equalsIgnoreCase("select")) {
                    ResultSet resultSet = statement.executeQuery(sqlStatement);
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                    resultSet.close();
                } else {
                    int i = statement.executeUpdate(sqlStatement);
                    if (i == 0) {
                        sqlResult = "<p>Câu lệnh đã được thực thi thành công.</p>";
                    } else {
                        sqlResult = "<p>Câu lệnh đã được thực thi thành công.<br>"
                                + i + " hàng bị ảnh hưởng.</p>";
                    }
                }
            }
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            sqlResult = "<p>Lỗi: Không tìm thấy trình điều khiển JDBC của SQL Server.<br>"
                    + "Hãy đảm bảo tệp mssql-jdbc-....jar đã có trong classpath.</p>";
        } catch (SQLException e) {
            sqlResult = "<p>Lỗi thực thi câu lệnh SQL:<br>"
                    + e.getMessage() + "</p>";
        }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}