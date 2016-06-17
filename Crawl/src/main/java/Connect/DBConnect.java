package Connect;

import Domain.Crawling;
import Domain.FreqCount;
import Domain.Rank;
import Domain.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakim on 21-May-16.
 */
public class DBConnect {
    public Connection conn = null;

    private Logger logger = LoggerFactory.getLogger(DBConnect.class);

    public DBConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if(Constants.DEBUG){
                String url = "jdbc:mysql://127.0.0.1/tugasakhir_2";
                conn = DriverManager.getConnection(url, "root", "");
            }else{
                String url = "jdbc:mysql://31.220.53.30/tugasakhir_v2";
                conn = DriverManager.getConnection(url, "hakim", "hakimmarsudi");
            }

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
        String sql = "INSERT INTO crawling_url (domain, url, level, title, outlink_size, content_id) values('" + crawling.getDomain() + "', '" + crawling.getUrl() + "', '"+  crawling.getLevel() + "', '"+  crawling.getTitle() + "', '"+  crawling.getOutLinkSize() + "', '"+  crawling.getContentId() +"')";
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

    public ResultSet checkContent(String content) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "SELECT * From url_content where content = '" + content + "'";
        return st.executeQuery(sql);
    }

    public ResultSet checkTerm(String term) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "SELECT * From term where name = '" + term + "'";
        if(Constants.SHOW_SQL) logger.info(sql);
        return st.executeQuery(sql);
    }

    public int insertTerm (Token token) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "INSERT INTO term (name) values('" + token.getContent() + "')";
        st.executeUpdate(sql);
        String last_id = "SELECT LAST_INSERT_ID() as id";
        ResultSet rs = st.executeQuery(last_id);
        rs.next();
        if(Constants.SHOW_SQL) logger.info(sql);
        return rs.getInt("id");
    }

    public void insertTermMapper(int term_id, int url_id) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "insert into url_term_mapping (url_id, term_id) values('" + url_id + "' , '" + term_id + "')";
        if(Constants.SHOW_SQL) logger.info(sql);
        st.executeUpdate(sql);
    }

    public int insertContent(String content) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "insert into url_content (content) values('" + content + "')";
        if(Constants.SHOW_SQL) logger.info(sql);
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

    public Double selectOutlinkSize(int urlId) throws SQLException {
        int outLink = 0;
        String sql = "select outlink_size from crawling_url where id = "+ urlId;
        ResultSet rs = runSql(sql);
        while (rs.next()){
            outLink = rs.getInt("outlink_size");
        }

        if(Constants.SHOW_SQL) logger.info(sql);

        return Double.valueOf(outLink);
    }

    public ResultSet selectAllData() throws SQLException {
        String sql = "select cu.id as url_id, cu.url as url, cu.domain as domain, cu.`level` as `level`, cpu.url as parent_url, cpu.id as parent_id from mapping_url_parent_url mpu join crawling_url cu on cu.id = mpu.url_id join crawling_parent_url cpu on cpu.id = mpu.parent_url_id where not exists (select 1 from rank_score where cu.id = url_id)";
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

    public List<String> getStopList() throws SQLException {
        List<String> list = new ArrayList<String>();
        String sql = "select * from stoplist";
        ResultSet rs = runSql(sql);

        if(Constants.SHOW_SQL) logger.info(sql);

        while (rs.next()){
            String words = rs.getString("text");
            list.add(words);
        }

        return list;
    }

    public ResultSet getContent(int limit, int offset) throws SQLException {
        String sql = "select cu.id as url_id, uc.content as content from url_content uc join crawling_url cu on cu.content_id = uc.id where uc.extract_status = '0' limit " + limit + " offset " + offset;
        return runSql(sql);
    }

    public void updateStatus(int url_id){
        try {
            String sql = "update url_content uc join crawling_url cu on cu.content_id = uc.id set uc.extract_status = '1' where cu.id = " + url_id;
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllContent() throws SQLException {
        String sql = "select * from url_content";
        return runSql(sql);
    }

    public int getTotalContent() throws SQLException {
        int total = 0;
        String sql = "select count(*) as total from url_content";
        ResultSet resultSet = runSql(sql);
        while (resultSet.next()){
            total = resultSet.getInt("total");
        }

        return total;
    }

//    Frequency Count
    public ResultSet frequencyCounting(int urlId) throws SQLException{
        String sql = "select term_id, url_id, term.name, count(*) as tf from url_term_mapping join term on term.id = term_id where url_id = " + urlId + " group by term_id";
        return runSql(sql);
    }

    public int inverseFrequencyCounting(int termId) throws SQLException{
        String sql = "select * from url_term_mapping where term_id = " + termId + " group by url_id";
        ResultSet rs = runSql(sql);
        int idf  = 0;
        while (rs.next()){
            idf++;
        }
        return idf;
    }

    public int getDocumentTotal() throws SQLException {
        String sql = "select count(*) as total from url_content uc join crawling_url cu on cu.content_id = uc.id";
        ResultSet resultSet = runSql(sql);
        resultSet.next();
        return resultSet.getInt("total");
    }

    public boolean checkDataFreq(int urlId, int termId) throws SQLException {
        String sql = "select * from frequency_term_doc where url_id = " + urlId + " and term_id = " + termId;
        logger.info(sql);
        ResultSet resultSet = runSql(sql);

        if(resultSet.next()){
            return true;
        }

        return false;
    }

    public boolean checkDataurl(int urlId) throws SQLException {
        String sql = "select * from crawling_url cu where (select count(*) from (select * from url_term_mapping where url_id = " + urlId + " group by term_id) as url_term where url_id = " + urlId + " group by url_id) <> (select count(*) from frequency_term_doc where url_id = " + urlId +") and cu.id = " + urlId;
        logger.info(sql);
        ResultSet resultSet = runSql(sql);

        if(resultSet.next()){
            return true;
        }
        return false;
    }

    public void insertFrequency(FreqCount fq) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "INSERT INTO frequency_term_doc (url_id, term_id, tf, idf, weight) values('" + fq.getUrlId() + "', '" + fq.getTermId() + "', '" + fq.getTf() + "', '" + fq.getIdf() + "', '" + fq.getWeight() + "')";
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
