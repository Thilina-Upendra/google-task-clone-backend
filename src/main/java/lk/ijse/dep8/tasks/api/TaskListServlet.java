package lk.ijse.dep8.tasks.api;

import lk.ijse.dep8.tasks.util.HttpServlet2;
import lk.ijse.dep8.tasks.util.ResponseStatusException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "TaskListServlet")
public class TaskListServlet extends HttpServlet2 {


    private final Logger logger = Logger.getLogger(TaskListServlet.class.getName());
    private AtomicReference<DataSource> pool;

    @Override
    public void init() {
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/pool");
            pool = new AtomicReference<>(ds);
        } catch (NamingException e) {
            logger.log(Level.SEVERE, "Failed to locate the JNDI pool", e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().startsWith("application/json")) {
            System.out.println("here");
            throw new ResponseStatusException(415, "Invalid content type or empty content type");
        }

        String pattern = "/([A-Fa-f0-9\\-]{36})/lists/?";
        if (!req.getPathInfo().matches(pattern)) {
            throw new ResponseStatusException(405, "Invalid end point for post request");
        }

        Matcher matcher = Pattern.compile(pattern).matcher(req.getPathInfo());
        matcher.find();
        String userId = matcher.group(1);

        try (Connection connection = pool.get().getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
