package animals.helper;

import animals.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.*;

public class CentralLogger {
    public static final Logger logger = Logger.getLogger("");

    static {
        final InputStream inputStream = Main.class.getResourceAsStream("/logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }

        // Disabling programmatic control of logger but leaving code here, in case needed for quick lookup
        /*
        File parentDir = new File(System.getProperty("user.dir") + "/log");
        parentDir.mkdirs();
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(System.getProperty("user.dir") + "/log/GuessTheAnimalApp.log", true);
        } catch (IOException e) {
            System.out.println("Unable to create application logfile:");
            e.printStackTrace();
        }
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        for (Handler h : logger.getHandlers()) {
            if(h.toString().contains("ConsoleHandler")) {
                logger.removeHandler(h);
            }
        }
        logger.setLevel(Level.FINEST);
        logger.setLevel(Level.INFO); */
    }
}
