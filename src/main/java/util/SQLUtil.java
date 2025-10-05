package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SQLUtil {

    /**
     * Chuyển đổi một đối tượng ResultSet thành một bảng HTML.
     *
     * @param results Đối tượng ResultSet từ một câu truy vấn SQL.
     * @return Một chuỗi chứa mã HTML cho bảng.
     * @throws SQLException Nếu có lỗi truy cập dữ liệu.
     */
    public static String getHtmlTable(ResultSet results) throws SQLException {

        StringBuilder htmlTable = new StringBuilder();
        ResultSetMetaData metaData = results.getMetaData();
        int columnCount = metaData.getColumnCount();

        htmlTable.append("<table>");

        // Thêm hàng tiêu đề (header row)
        htmlTable.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            htmlTable.append("<th>");
            htmlTable.append(metaData.getColumnName(i));
            htmlTable.append("</th>");
        }
        htmlTable.append("</tr>");

        // Thêm tất cả các hàng dữ liệu khác
        while (results.next()) {
            htmlTable.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                htmlTable.append("<td>");
                // Xử lý giá trị null để tránh hiển thị "null" trong bảng
                String value = results.getString(i);
                htmlTable.append(value == null ? "" : value);
                htmlTable.append("</td>");
            }
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }
}