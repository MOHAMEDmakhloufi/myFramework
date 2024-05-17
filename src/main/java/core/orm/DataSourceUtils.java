package core.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DataSourceUtils{
        private static Connection con;
        private static DataSource dataSource;
        private DataSourceUtils() throws SQLException{

        con= DriverManager.getConnection(dataSource.getUrl(),
                            dataSource.getUsername(),
                            dataSource.getPassword());
        }
        public static Connection getConnection (DataSource dSource){


            try {
                if (con == null || con.isClosed()) {
                    try {
                    dataSource = dSource;
                    new DataSourceUtils();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return con;
        }
}
