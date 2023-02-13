package animals.helper;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class CentralLogger {
    public static final Logger logger = Logger.getLogger("");

    static {
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
            if(h.toString().contains("ConsoleHandler")) { // programmatically switch off logging to display - TODO: configure properties
                logger.removeHandler(h);
            }
        }
        //logger.setLevel(Level.FINEST);
        logger.setLevel(Level.INFO);
    }
}
