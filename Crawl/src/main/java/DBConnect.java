import Domain.Crawling;
import edu.uci.ics.crawler4j.url.WebURL;

import java.sql.*;

/**
 * Created by Hakim on 21-May-16.
 */
public class DBConnect {
    public Connection conn = null;

    public DBConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/tugasakhir";
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("conn built");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runSql(String sql) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.executeQuery(sql);
    }

    public int insertData (Crawling crawling) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "INSERT INTO crawling (domain, url, parent_url) values('" + crawling.getDomain() + "', '" + crawling.getUrl() + "', '"+ crawling.getParent_url() +"')";
        return st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public int insertMapper (Crawling crawling) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "INSERT INTO crawling_mapper (url_id, parent_url_id) values('" + crawling.getId() + "', '" + crawling.getParentId() + "')";
        return st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();
        }
    }
}
