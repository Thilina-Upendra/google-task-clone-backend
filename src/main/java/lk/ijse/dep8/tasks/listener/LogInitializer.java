package lk.ijse.dep8.tasks.listener;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@WebListener
public class LogInitializer implements ServletContextListener {

    private Logger logger = Logger.getLogger(LogInitializer.class.getName());
    FileHandler fileHandler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            final Properties prop = new Properties();
            prop.load(this.getClass().getResourceAsStream("/application.properties"));

            String profile = (String) prop.getOrDefault("app.profile.active", "dev");
            String logDir = (String) prop.getOrDefault("app.logging.path", "/var/logs");

            if(!(profile.equals("dev")||profile.equals("prod"))){
                logger.warning("Invalid profile has been activated and fall back to dev");
                profile = "dev";
            }

            if(profile.equals("dev")){
                Logger.getLogger("lk.ijse.dep8.tasks").setLevel(Level.FINE);
            }else{
                Logger.getLogger("lk.ijse.dep8.tasks").setLevel(Level.INFO);
            }

            Path logDirPath = Paths.get(logDir);
            if(Files.notExists(logDirPath)){
                logDir = System.getProperty("java.io.tmpdir");
            }

            logDirPath = Paths.get(logDir, "tasks");

            if(Files.notExists(logDirPath)){
                Files.createDirectory(logDirPath);
            }


            final String path = logDirPath.toAbsolutePath().toString();
            installFileHandler(getPath(path));
            Logger.getLogger("lk.ijse.dep8.tasks").setUseParentHandlers(false);


            /*Create CRON JOBS*/
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleWithFixedDelay(()->{
               installFileHandler(path);
            }, Duration.between(LocalTime.now(), LocalTime.MIDNIGHT).toMillis(), 60 * 60 * 24 * 1000, TimeUnit.MILLISECONDS);


            /*executor.scheduleWithFixedDelay(()->{
                installFileHandler();
            }, Duration.between(LocalTime.now(),LocalTime.parse("12:49:00")).toMinutes(), 1, TimeUnit.MINUTES);*/

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private void installFileHandler(String path){
        /*Story of the */
        if(fileHandler != null){
            fileHandler.close();
            Logger.getLogger("lk.ijse.dep8.tasks").removeHandler(fileHandler);
            try {
                fileHandler = new FileHandler(path, 2 * 1024 * 1024, 20, true);
                fileHandler.setFormatter(new SimpleFormatter());
                fileHandler.setLevel(Logger.getLogger("lk.ijse.dep8.tasks").getLevel());
                Logger.getLogger("lk.ijse.dep8.tasks").addHandler(fileHandler);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private String getPath(String logDirPath){
        return logDirPath + File.separator + LocalDate.now() + "tasks-log-%g.log";
    }
}
