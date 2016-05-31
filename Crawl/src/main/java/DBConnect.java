import Domain.Crawling;
import Domain.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by Hakim on 21-May-16.
 */
public class DBConnect {
    public Connection conn = null;

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public DBConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://31.220.53.30/tugasakhir";
            conn = DriverManager.getConnection(url, "hakim", "hakimmarsudi");
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
        String sql = "INSERT INTO crawling_url (domain, url, level) values('" + crawling.getDomain() + "', '" + crawling.getUrl() + "', '"+  crawling.getLevel() + "')";
        PreparedStatement st = conn.prepareStatement(sql);
        st.executeUpdate(sql);
        String last_id = "SELECT LAST_INSERT_ID() as id";
        ResultSet rs = st.executeQuery(last_id);
        rs.next();
        if(Constants.SHOW_SQL) logger.info(sql);
        return rs.getInt("id");
    }

    public int insertParentData (Crawling crawling) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "INSERT INTO crawling_parent_url (url) values('" + crawling.getParentUrl() + "')";
        st.executeUpdate(sql);
        String last_id = "SELECT LAST_INSERT_ID() as id";
        ResultSet rs = st.executeQuery(last_id);
        rs.next();
        if(Constants.SHOW_SQL) logger.info(sql);
        return rs.getInt("id");
    }

    public ResultSet checkParent(String parentUrl) throws SQLException {
        String sql = "SELECT * From crawling_parent_url where url = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, parentUrl);
        return st.executeQuery();
    }

    public ResultSet checkUrl(String url) throws SQLException {
        String sql = "SELECT * From crawling_url where url = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, url);
        if(Constants.SHOW_SQL) logger.info(sql);
        return st.executeQuery();
    }

    public boolean checkMapper(Crawling crawling) throws SQLException {
        boolean isExist = false;
        String sql = "SELECT * From mapping_url_parent_url where url_id = ? and parent_url_id = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, crawling.getId());
        st.setInt(2, crawling.getParentId());
        ResultSet rs = st.executeQuery();
        while (rs.next()){
            isExist = true;
        }
        if(Constants.SHOW_SQL) logger.info(sql);
        return isExist;
    }


    public int insertMapper (Crawling crawling) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "INSERT INTO mapping_url_parent_url (url_id, parent_url_id) values('" + crawling.getId() + "', '" + crawling.getParentId() + "')";
        if(Constants.SHOW_SQL) logger.info(sql);
        return st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public ResultSet selectAll() throws SQLException {
        String sql = "select * from crawling_url";
        return runSql(sql);
    }

    public ResultSet selectByParentId(int parentId) throws SQLException {
        String sql = "select * from mapping_url_parent_url where parent_url_id = " + parentId;
        if(Constants.SHOW_SQL) logger.info(sql);
        return runSql(sql);
    }

    public Double selectInLink(int urlId) throws SQLException {
        int inLink = 0;
        String sql = "select count(*) as total from mapping_url_parent_url where url_id = "+ urlId;
        ResultSet rs = runSql(sql);
        while (rs.next()){
            inLink = rs.getInt("total");
        }

        if(Constants.SHOW_SQL) logger.info(sql);

        return Double.valueOf(inLink);
    }

    public Double selectOutlink(int urlId) throws SQLException {
        int outLink = 0;
        String sql = "select count(*) as total from mapping_url_parent_url where parent_url_id = "+ urlId;
        ResultSet rs = runSql(sql);
        while (rs.next()){
            outLink = rs.getInt("total");
        }

        if(Constants.SHOW_SQL) logger.info(sql);

        return Double.valueOf(outLink);
    }

    public ResultSet selectAllData() throws SQLException {
        String sql = "select cu.id as url_id, cu.url as url, cu.domain as domain, cu.`level` as `level`, cpu.url as parent_url, cpu.id as parent_id from mapping_url_parent_url mpu join crawling_url cu on cu.id = mpu.url_id join crawling_parent_url cpu on cpu.id = mpu.parent_url_id";
        if(Constants.SHOW_SQL) logger.info(sql);
        return runSql(sql);
    }

    public ResultSet selectRankScore(int urlId) throws SQLException {
        String sql = "select * from rank_score where url_id = "+ urlId;
        logger.info(sql);
        return runSql(sql);
    }

    public void insertRank(Rank rank) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "Insert into rank_score (url_id, rank_score) values('" + rank.getUrlId() + "', '" + rank.getRankScore() + "')";
        if(Constants.SHOW_SQL) logger.info(sql);
        st.executeUpdate(sql);
    }

    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();
        }
    }
}
