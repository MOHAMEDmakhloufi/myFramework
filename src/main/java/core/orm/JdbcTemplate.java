package core.orm;


import java.sql.*;

public class JdbcTemplate {
    private DataSource dataSource;
    private Connection con= null;

    public JdbcTemplate() {
        dataSource = new DataSource();
    }

    public Object update(String sql, String[] args){
        Object result = null;
        openConnection();
        beginTransaction();
        result = executeUpdate(sql, args);
        endTransaction();
        closeConnection();
        return result;
    }

    public ResultSet query(String sql, String[] args){
        ResultSet result = null;
        openConnection();
        beginTransaction();
        result = executeQuery(sql, args);

        return result;
    }

    private void openConnection() {
        con = DataSourceUtils.getConnection(this.dataSource);
    }
    private void beginTransaction() {
        try {
            con.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private ResultSet executeQuery(String sql, String[] args) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            for (int i=0; i<args.length; i++)
                pstmt.setString(i+1, args[i]);

            rs = pstmt.executeQuery();


        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }
    private Object executeUpdate(String sql, String[] args) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Object javaValue = null;
        try {
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i=0; i<args.length; i++)
                pstmt.setString(i+1, args[i]);

            pstmt.executeUpdate();
            rs= pstmt.getGeneratedKeys();
            if (rs.next()){
                int sqlType = rs.getMetaData().getColumnType(1);
                Object value = rs.getObject(1);

                javaValue = UtilisSQL_JAVA.castSQLValue(value, sqlType);

            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return javaValue;
    }

    public void endTransaction() {
        try {
            con.commit();
            con.setAutoCommit(true);
        }catch (SQLException ex){
            try {
                if (con != null)
                    con.rollback();
            }catch (SQLException e){
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        if (con !=null)
            try {
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
    }
}
