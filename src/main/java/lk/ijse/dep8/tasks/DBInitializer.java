package lk.ijse.dep8.tasks;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class DBInitializer implements ServletContextListener {

    final Logger logger = Logger.getLogger(DBInitializer.class.getName());

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource pool;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try(Connection connection = pool.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SHOW TABLES");

            while (rst.next()){
                System.out.println(rst.getString(1));
            }
        }catch (SQLException e){
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
